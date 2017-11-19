package com.example.zyy.wordsapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by zyy on 2016/9/17.
 */
public class WordsApplication extends Application {

    private static Context context;
    public static Context getContext(){
        return WordsApplication.context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        WordsApplication.context=getApplicationContext();
    }

}
