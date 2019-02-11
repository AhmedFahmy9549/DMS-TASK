package com.example.sweelam.dmstask.Models;

import android.arch.persistence.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Owner{

    @SerializedName("html_url")
    @ColumnInfo(name = "html_url")
    private String htmlUrl;

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

}