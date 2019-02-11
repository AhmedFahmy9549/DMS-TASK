package com.example.sweelam.dmstask.Room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.sweelam.dmstask.Models.Module;

import java.util.List;

public class WordViewModel extends AndroidViewModel{

    private WordRepository mRepository;
        private LiveData<List<Module>> mAllWords;

    public WordViewModel( Application application) {
        super(application);
        mRepository=new WordRepository(application);
        mAllWords=mRepository.getAllWords();
    }
       public LiveData<List<Module>> getAllWords() {
        return mAllWords;
    }

        public void insert(Module word) {
        mRepository.insert(word);
    }

    public void deleteAll(){
        mRepository.deleteAll();
    }


}
