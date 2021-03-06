package com.example.etorres.canvastest;

import android.os.Handler;
import android.util.Log;

import java.lang.Math;

/**
 * Created by etorres on 07/06/15.
 */
public class BackgroundTask implements Runnable {
    private double MILLISECONDS_SLEEP = 50;
    private BackgroundHandler mHandler;
    private Thread mThread;
    private int mAmountArmonics = 1;

    public BackgroundTask(BackgroundHandler handler) {
        mHandler = handler;
        mThread = Thread.currentThread();

        // Moves the current Thread into the background
        // android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Override
    public void run() {
        double time = 0.0;

        while(true) {
            mHandler.sendPoint(this.getNextValue(time));
            time += MILLISECONDS_SLEEP/1000;

            // TODO: Notify this value to the Main UI Thread
            // Sleep some seconds
            try {
                mThread.sleep((int)MILLISECONDS_SLEEP, 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public double getNextValue(double time) {
        synchronized(this) {

            double value = 0;
            double arg = 0;
            for (int i = 1; i <= mAmountArmonics; ++i) {
                arg = Math.PI * 2 * (2*i -1) * time;
                value += Math.sin(arg);
            }

            return value;
        }
    }

    public void setAmountArmonics(int amount) {
        synchronized(this) {
            mAmountArmonics = amount;
        }
    }
}
