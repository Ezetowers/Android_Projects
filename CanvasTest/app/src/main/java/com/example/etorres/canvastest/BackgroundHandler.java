package com.example.etorres.canvastest;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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
    Handler mHandler;
    GraphView mGraph;
    TextView mLabel;
    LineGraphSeries<DataPoint> mSeries;
    double mX = 0.0;

    public BackgroundHandler(GraphView graph, 
                             LineGraphSeries<DataPoint> series, 
                             TextView label) {
        mHandler = new Handler(Looper.getMainLooper());
        mGraph = graph;
        mLabel = label;
        mSeries = series;
    }

    public void sendPoint(double point) {
        Message msg = this.obtainMessage(0, new FunctionValue(point));
        msg.sendToTarget();
    }

    public void handleMessage(Message msg) {
        FunctionValue value = (FunctionValue) msg.obj;
        mLabel.setText(Double.toString(value.getValue()));
        mSeries.appendData(new DataPoint(mX,value.getValue()), true, 1000);
        mX += 1;
    }
}
