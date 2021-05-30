package com.app.vruddy;

import android.os.AsyncTask;

import java.util.TimerTask;

public class Timer extends AsyncTask<Void, Void, Void> {
    private static int seconds = 0;
    private static Timer instance;

    //Getters & Setters

    public static int getSeconds() {
        return seconds;
    }

    public static void setSeconds(int seconds) {
        Timer.seconds = seconds;
    }

    public void second(){
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                seconds = seconds+1;
                setSeconds(seconds);
            }
        },0, 1000);
    }

    public static synchronized Timer getInstance(){
        if(instance == null){
            instance = new Timer();
        }
        return instance;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        second();
        return null;
    }
}
