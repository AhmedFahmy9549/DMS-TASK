package com.example.sweelam.dmstask.Room;

import android.arch.persistence.room.TypeConverter;

import com.example.sweelam.dmstask.Models.Owner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class DataConverter {

    @TypeConverter // note this annotation
    public String fromOwner(Owner owner) {
        if (owner == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<Owner>() {
        }.getType();
        String json = gson.toJson(owner, type);
        return json;
    }

    @TypeConverter
    public Owner toOwner(String ownerString) {
        if (ownerString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Owner>() {
        }.getType();
        Owner owner = gson.fromJson(ownerString, type);
        return owner;
    }

}
