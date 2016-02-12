package com.example.corentin.moneyshot.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.corentin.moneyshot.R;
import com.example.corentin.moneyshot.model.AccountOperation;
import com.example.corentin.moneyshot.model.BankAccount;
import com.example.corentin.moneyshot.sql.MoneyDataBase;
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
    private BankAccount mBankAccount;

    /**
     * @param view
     * @param bankAccount if null, then all accounts
     */
    public HeaderAccount(View view, @Nullable BankAccount bankAccount) {
        mView = view;
        mBankAccount = bankAccount;
        mRadarChart = (LineChart) mView.findViewById(R.id.chart);

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
        int count = list.size();
        for (int i = 0; i < count; i++) {
            entries.add(new Entry((float) list.get(i).getValue(), i));
        }
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        LineDataSet dataset = new LineDataSet(entries, "test");
        LineData data = new LineData();
        dataset.setDrawCubic(true);
        data.addDataSet(dataset);
        mRadarChart.setData(data);
        mRadarChart.setDescription(null);
        mView.layout(parent.getLeft(), 0, parent.getRight(), mView.getMeasuredHeight());
        mView.draw(c);
        c.restore();
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
}
