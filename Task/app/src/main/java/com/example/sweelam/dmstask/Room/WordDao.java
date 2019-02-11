package com.example.sweelam.dmstask.Room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.sweelam.dmstask.Models.Module;

import java.util.List;

@Dao
public interface WordDao {

    @Insert
    void insert(Module word);

    @Query("DELETE FROM word_table")
    void deleteAll();

    @Query("SELECT * from word_table ")
    LiveData<List<Module>>getAllWords();
}

/***
 If the table has more than one column, you can use

 @Insert(onConflict = OnConflictStrategy.REPLACE)

 to replace a row.


 ***/