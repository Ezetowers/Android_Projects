package com.example.etorres.ecgtest;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

class FunctionValue {
    private double mValue;

    FunctionValue(double arg) {
        mValue = arg;
    }

    public double getValue() {
        return mValue;
    }
}

/**
 * Created by etorres on 07/06/15.
 */
public class BackgroundHandler extends Handler {
    private Handler mHandler;
    private GraphView mGraph;
    private TextView mLabel;
    private LineGraphSeries<DataPoint> mSeries;
    private double mX = 0.0;
    // Sample frequency, in Hz
    private double mSampleFrequency = 35;
    private double REFRESH_FREQUENCY = 180;
    private Queue<DataPoint> mBuffer;

    public BackgroundHandler(GraphView graph, 
                             LineGraphSeries<DataPoint> series,
                             TextView label) {
        mHandler = new Handler(Looper.getMainLooper());
        mGraph = graph;
        mLabel = label;
        mSeries = series;
        mBuffer = new LinkedList<DataPoint>();
    }

    public void sendPoint(double point) {
        synchronized (this) {
            // Adds a point to a buffer.
            mBuffer.add(new DataPoint(mX, point));
            // mSeries.appendData(new DataPoint(mX, value.getValue()), true, 1000);
            mX += this.getSamplePeriod();

            // Log.d("SAMPLING", Integer.toString(mBuffer.size()));
            // Log.d("SAMPLING", Double.toString(Math.ceil(this.getSampleFrequency() / REFRESH_FREQUENCY)));

            if (mBuffer.size() >= (int) Math.ceil(this.getSampleFrequency() / REFRESH_FREQUENCY)) {
                Message msg = this.obtainMessage(0, null);
                msg.sendToTarget();
            }
        }
    }

    public double getSampleFrequency() {
        synchronized (this) {
            return mSampleFrequency;
        }
    }

    public double getSamplePeriod() {
        return 1/this.getSampleFrequency();
    }

    public void setSampleFrequency(double frequency) {
        synchronized (this) {
            mSampleFrequency = frequency;
        }
    }

    public void handleMessage(Message msg) {
        synchronized (this) {
            while (!mBuffer.isEmpty()) {
                mSeries.appendData(mBuffer.poll(), true, 500);
            }
        }
    }
}
