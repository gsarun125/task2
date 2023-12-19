// PhotoList.java
package com.ka.task1.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotoList {
    @SerializedName("photo")
    private List<Photo> items;

    public List<Photo> getItems() {
        return items;
    }
}
