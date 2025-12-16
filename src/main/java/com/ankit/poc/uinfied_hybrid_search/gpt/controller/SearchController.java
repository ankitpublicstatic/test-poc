package com.ankit.poc.uinfied_hybrid_search.gpt.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ankit.poc.request.SearchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

@RestController
@RequestMapping("/api/search")
public class SearchController {

  @Value("${vector.db.url}")
  private String vectorDbUrl;

  @Autowired
  private JdbcTemplate jdbc;

  @PostMapping
  public ResponseEntity<?> search(@RequestBody SearchRequest req) throws IOException {
    // 1) get query embedding (call a local Python embedding service or OpenAI)
    float[] queryEmb = getEmbeddingFromService(req.getQuery());

    // 2) call vector DB with metadata filters
    // Example: Qdrant http call
    Map<String, Object> payload = new HashMap<>();
    payload.put("vector", queryEmb);
    payload.put("top", req.getTopK());
    // example Qdrant filter format
    Map<String, Object> filter = new HashMap<>();
    filter.put("must",
        List.of(Map.of("key", "tenant_id", "match", Map.of("value", req.getTenantId()))));
    // add rights filter if provided
    if (req.getRights() != null) {
      filter.put("must",
          List.of(Map.of("key", "rights_status", "match", Map.of("value", req.getRights()))));
    }
    payload.put("filter", filter);

    OkHttpClient client = new OkHttpClient();
    com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(
        MediaType.parse("application/json"), new ObjectMapper().writeValueAsString(payload));
    Request httpReq = new Request.Builder()
        .url(vectorDbUrl + "/collections/" + req.getCollection() + "/points/search").post(body)
        .build();
    Response resp = client.newCall(httpReq).execute();
    Map respJson = new ObjectMapper().readValue(resp.body().string(), Map.class);

    // 3) map vector results to segments, optionally join with Postgres for enriched metadata
    List<String> segmentIds = extractIds(respJson);
    if (segmentIds.isEmpty()) {
      return ResponseEntity.ok(Map.of("results", List.of()));
    }

    String inClause =
        segmentIds.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
    List<Map<String, Object>> rows =
        jdbc.queryForList("SELECT * FROM segments WHERE id IN (" + inClause + ")");

    // 4) rerank using a cross-encoder reranker if needed (call a Python microservice), else return
    // vector score ordering
    return ResponseEntity.ok(Map.of("results", rows));
  }

  private List<String> extractIds(Map respJson) {
    // TODO Auto-generated method stub
    return null;
  }

  private float[] getEmbeddingFromService(String text) {
    return null;
    // call internal embedding microservice; keep embeddings consistent with index model
    // For brevity: call OpenAI or your Python microservice
  }
}
