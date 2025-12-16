package com.ankit.poc.uinfied_hybrid_search.gemini.service;


}
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Points.Filter;
import io.qdrant.client.grpc.Points.ScoredPoint;
import io.qdrant.client.grpc.Points.SearchPoints;
import io.qdrant.client.grpc.Collections.Distance;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.ExecutionException;
@Service
public class UnifiedHybridSearchService {
 

    private final QdrantClient qdrantClient;
    private final EmbeddingService embeddingService; // A helper to turn query text into vector

    public UnifiedHybridSearchService(QdrantClient qdrantClient, EmbeddingService embeddingService) {
        this.qdrantClient = qdrantClient;
        this.embeddingService = embeddingService;
    }

    /**
     * The Holy Grail: Unified Semantic + Structured Search
     */
    public List<SearchResult> searchAssets(String queryText, SearchFilters filters) throws ExecutionException, InterruptedException {
        
        // 1. Generate Query Vector (Semantic)
        // This calls a Python microservice or runs a local ONNX model to get the CLIP text embedding
        List<Float> queryVector = embeddingService.getTextEmbedding(queryText);

        // 2. Build Structured Filters (The "Structured" part)
        // We filter DOWN the search space using metadata tags (Rights, Categories)
        Filter.Builder filterBuilder = Filter.newBuilder();

        if (filters.getCategory() != null) {
            filterBuilder.addMust(io.qdrant.client.PointIdFactory.matchKeyword("category", filters.getCategory()));
        }

        if (filters.getDrmStatus() != null) {
            filterBuilder.addMust(io.qdrant.client.PointIdFactory.matchKeyword("drm_status", filters.getDrmStatus()));
        }
        
        // 3. Execute Hybrid Query
        // Qdrant performs pre-filtering (HNSW graph traversal restricted by filter)
        SearchPoints searchRequest = SearchPoints.newBuilder()
                .setCollectionName("zoom_assets")
                .addAllVector(queryVector)
                .setFilter(filterBuilder.build())
                .setLimit(20)
                .setWithPayload(io.qdrant.client.grpc.Points.WithPayloadSelector.newBuilder().setEnable(true).build())
                .build();

        List<ScoredPoint> results = qdrantClient.searchAsync(searchRequest).get();

        // 4. Map to Domain Objects
        return results.stream().map(this::mapToSearchResult).toList();
    }

    private SearchResult mapToSearchResult(ScoredPoint point) {
        // Extract payload to return context
        var payload = point.getPayloadMap();
        return new SearchResult(
            payload.get("asset_id").getStringValue(),
            point.getScore(), // Similarity Score
            payload.get("timestamp_start").getDoubleValue(), // Jump-to-time functionality
            payload.get("timestamp_end").getDoubleValue()
        );
    }
    
//    . Implementing the "Similarity" (Image-to-Image) Search
//    The JD mentions "Similarity." This usually means "Find me shots that look like this shot."
//
//    In the Java Service, you would add an endpoint that doesn't take text, but takes an existing asset_id + timestamp.
    public List<SearchResult> findSimilarScenes(String assetId, double timestamp) {
      // 1. Retrieve the vector of the specific frame from Qdrant by ID
      List<Float> sourceVector = qdrantClient.retrieveVector(assetId, timestamp);
      
      // 2. Search using that vector as the query
      // This finds visual duplicates or stylistically similar shots
      SearchPoints searchRequest = SearchPoints.newBuilder()
              .setCollectionName("zoom_assets")
              .addAllVector(sourceVector)
              .setLimit(10)
              .build();
              
      // ... execute and return
  }
}