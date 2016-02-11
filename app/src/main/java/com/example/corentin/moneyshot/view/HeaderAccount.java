package com.example.corentin.moneyshot.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.corentin.moneyshot.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;

import java.util.ArrayList;

/**
 * Created by corentinpacaud on 11/02/2016.
 */
public class HeaderAccount extends RecyclerView.ItemDecoration {

    private View mView;
    private LineChart mRadarChart;

    public HeaderAccount(View view) {
        mView = view;
        mRadarChart = (LineChart) mView.findViewById(R.id.chart);
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));
        LineDataSet dataset = new LineDataSet(entries, "# of Calls");
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        LineData data = new LineData(labels, dataset);
        dataset.setDrawCubic(true);
        mRadarChart.setData(data);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        mView.layout(parent.getLeft(), 0, parent.getRight(), mView.getMeasuredHeight());
        mView.draw(c);
        c.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        Log.d("Measure", "W: " + mView.getMeasuredWidth() + " w:" + mView.getMeasuredHeight());
        if (mView.getMeasuredHeight() <= 0) {
            mView.measure(View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(), View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(parent.getMeasuredHeight(), View.MeasureSpec.AT_MOST));
        }
        outRect.set(0, mView.getMeasuredHeight(), 0, 0);
    }
}
