package com.bignerdranch.android.nasaapod.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Apod {

  @expose
  private Date date;

  @expose
  private String title;

  @expose
  private String explanation;

  @expose
  private String copyright;

  @expose
  private String url;

  @expose
  @SerializedName("media_type")
  private String mediaType;

  @expose
  @SerializedName("hdurl")
  private String hdUrl;

  @expose
  @SerializedName("service_version")
  private String serviceVersion;

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getExplanation() {
    return explanation;
  }

  public void setExplanation(String explanation) {
    this.explanation = explanation;
  }

  public String getCopyright() {
    return copyright;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getMediaType() {
    return mediaType;
  }

  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }

  public String getHdUrl() {
    return hdUrl;
  }

  public void setHdUrl(String hdUrl) {
    this.hdUrl = hdUrl;
  }

  public String getServiceVersion() {
    return serviceVersion;
  }

  public void setServiceVersion(String serviceVersion) {
    this.serviceVersion = serviceVersion;
  }
}
