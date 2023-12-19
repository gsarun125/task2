// RecyclerList.java
package com.ka.task1.model;

import com.google.gson.annotations.SerializedName;

public class RecyclerList {
    @SerializedName("photos")
    private PhotoList photos;

    public PhotoList getPhotos() {
        return photos;
    }
}
