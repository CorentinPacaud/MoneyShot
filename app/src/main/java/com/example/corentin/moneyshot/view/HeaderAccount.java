package com.example.corentin.moneyshot.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.corentin.moneyshot.R;
import com.example.corentin.moneyshot.model.AccountOperation;
import com.example.corentin.moneyshot.model.BankAccount;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by corentinpacaud on 11/02/2016.
 */
public class HeaderAccount extends RecyclerView.ItemDecoration {

    private View mView;
    private LineChart mLineChart;
    private BankAccount mBankAccount;

    /**
     * @param view
     * @param bankAccount if null, then all accounts
     */
    public HeaderAccount(View view, @Nullable BankAccount bankAccount) {
        mView = view;
        mBankAccount = bankAccount;
        mLineChart = (LineChart) mView.findViewById(R.id.chart);

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        ArrayList<AccountOperation> list;
        if (mBankAccount != null) {
            list = AccountOperation.getAllOperationFromAccountId(parent.getContext(), mBankAccount.getId());
        } else {
            list = AccountOperation.getAllOperation(parent.getContext());
        }
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        int count = list.size();
        double total = 0;
        for (int i = 0; i < count; i++) {
            total += list.get(i).getValue();
            Date date = new Date(list.get(i).getCreatedAt());
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy hh:mm");
            xVals.add(format.format(date));
            entries.add(new Entry((float) total, i));
        }
        LineDataSet dataset = new LineDataSet(entries, "Money");
        dataset.setLineWidth(1.5f);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dataset.setColor(parent.getContext().getResources().getColor(R.color.primary, null));
        } else
            dataset.setColor(parent.getContext().getResources().getColor(R.color.primary));

        dataset.setDrawCircles(true);
        dataset.setCircleColor(parent.getContext().getResources().getColor(R.color.primary_dark));
        dataset.setDrawCircleHole(false);
        LineData data = new LineData(xVals, dataset);
        dataset.setDrawCubic(true);
        mLineChart.setData(data);
        mLineChart.setDescription(null);
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setPinchZoom(true);
        mLineChart.setDoubleTapToZoomEnabled(true);
        mLineChart.setAutoScaleMinMaxEnabled(true);
        mView.layout(parent.getLeft(), 0, parent.getRight(), mView.getMeasuredHeight());
        mView.draw(c);
        c.save();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            if (mView.getMeasuredHeight() <= 0) {
                mView.measure(View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(), View.MeasureSpec.AT_MOST),
                        View.MeasureSpec.makeMeasureSpec(parent.getMeasuredHeight(), View.MeasureSpec.AT_MOST));
            }
            outRect.set(0, mView.getMeasuredHeight(), 0, 0);
        } else outRect.setEmpty();
    }

    public LineChart getLineChart() {
        return mLineChart;
    }
}
