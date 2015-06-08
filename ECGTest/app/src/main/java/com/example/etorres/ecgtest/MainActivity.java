package com.example.etorres.ecgtest;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    ListView mArmonicsView;
    ArrayAdapter mArrayAdapter;
    ArrayList mNameList = new ArrayList();
    BackgroundTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        TextView textView = (TextView) findViewById(R.id.phaseLabel);

        LineGraphSeries<DataPoint> series =
                new LineGraphSeries<DataPoint>(new DataPoint[]{});
        graph.addSeries(series);

        // Create a Handler to update the Graph
        BackgroundHandler handler = new BackgroundHandler(graph, series, textView);
        mTask = new BackgroundTask(handler);

        // Change the axis scale
        /*graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(4);
        graph.getViewport().setMinY(-4);*/

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(3);
        graph.getViewport().setScrollable(true);

        // graph.setBackgroundColor(Color.BLACK);
        // graph.getGridLabelRenderer().setGridColor(Color.RED);

        series.setThickness(1);
        series.setColor(Color.GREEN);

        // ListView SetUp
        mArmonicsView = (ListView) findViewById(R.id.armonicsView);

        // Create an ArrayAdapter for the ListView
        mArrayAdapter = new ArrayAdapter(this,
                                         android.R.layout.simple_list_item_1,
                                         mNameList);

        // Fill the ListView
        for (int i = 1; i <= 10; ++i) {
            mNameList.add(Integer.toString(i));
        }
        mArrayAdapter.notifyDataSetChanged();

        mArmonicsView.setAdapter(mArrayAdapter);
        mArmonicsView.setOnItemClickListener(this);

        // Add the Background Task to calculate the cosine
        new Thread(mTask).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Log.d("omg android", position + ": " + mNameList.get(position));
        // The position is the armonic - 1
        mTask.setAmountArmonics(i+1);
    }
}
