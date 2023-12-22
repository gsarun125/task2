// Photo.java
package com.ka.task1.model;

import com.google.gson.annotations.SerializedName;

public class Photo {
    @SerializedName("id")
    private String id;

    @SerializedName("url_s")
    private String url;

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
    private int page;

    public int getPage() {
        return page;
    }
}
