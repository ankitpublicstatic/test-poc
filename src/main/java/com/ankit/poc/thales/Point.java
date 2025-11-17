package com.ankit.poc.thales;

import java.util.Date;

public class Point {
  // 2 dimension

  private Integer count;
  private Integer dimension;
  private String name;
  private String description;

  // Audit fields
  private String createdBy;
  private Date createdOn;
  private String updatedBy;
  private Date lastUpdatedOn;
  private Integer isActive;

  public Point() {

  }

  public Point(Integer count, Integer dimension, String name, String description) {
    super();
    this.count = count;
    this.dimension = dimension;
    this.name = name;
    this.description = description;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Integer getDimension() {
    return dimension;
  }

  public void setDimension(Integer dimension) {
    this.dimension = dimension;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public Date getLastUpdatedOn() {
    return lastUpdatedOn;
  }

  public void setLastUpdatedOn(Date lastUpdatedOn) {
    this.lastUpdatedOn = lastUpdatedOn;
  }

  public Integer getIsActive() {
    return isActive;
  }

  public void setIsActive(Integer isActive) {
    this.isActive = isActive;
  }


}
