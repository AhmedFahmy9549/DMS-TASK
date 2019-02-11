package com.example.sweelam.dmstask.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.sweelam.dmstask.Room.DataConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "word_table")
public class Module {


    @PrimaryKey(autoGenerate = true)
    public int uid;

    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String name;


    public Module() {
        }

    @SerializedName("full_name")
    @ColumnInfo(name = "fullName")
    private String fullName;

    @SerializedName("html_url")
    @ColumnInfo(name = "htmlUrl")
    private String htmlUrl;

    @SerializedName("description")
    @ColumnInfo(name = "description")
    private String description;


    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "owner")
    @SerializedName("owner")
    private Owner owner;



    @SerializedName("fork")
    private boolean fork;

    public String getName() {
        return name;
    }


    public String getFullName() {
        return fullName;
    }


    public Owner getOwner() {
        return owner;
    }
    public void setOwner(Owner owner) {
        this.owner = owner;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }


    public String getDescription() {
        return description;
    }

    public boolean getFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }
}