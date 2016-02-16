package com.example.corentin.moneyshot.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.corentin.moneyshot.R;
import com.example.corentin.moneyshot.adapter.OperationAdapter;
import com.example.corentin.moneyshot.model.AccountOperation;

import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class OperationFragment extends Fragment {

    private static final String TAG = OperationFragment.class.getSimpleName();

    public static final String EXTRA_ACCOUNT_ID = "extra_account_id";

    View mRootView;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    RecyclerView mRecyclerView;
    OperationAdapter mOperationAdapter;
    ImageView mImageView;
    AppBarLayout appBarLayout;
    TextView mTextViewTendance;
    LinearLayoutManager mLayoutManager;
    long mAccountId;

    public OperationFragment() {
    }

    public static OperationFragment newInstance(long accountId) {
        OperationFragment fragment = new OperationFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_ACCOUNT_ID, accountId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            mAccountId = getArguments().getLong(EXTRA_ACCOUNT_ID);
        }

        mRootView = inflater.inflate(R.layout.content_operation, container, false);
        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mTextViewTendance = (TextView) mRootView.findViewById(R.id.textTendance);
        appBarLayout = (AppBarLayout) mRootView.findViewById(R.id.appBarLayout);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) mRootView.findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.primary));

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mOperationAdapter = new OperationAdapter();
        mRecyclerView.setAdapter(mOperationAdapter);

        FloatingActionButton fab = (FloatingActionButton) mRootView.findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOperation(view);
            }
        });
        return mRootView;
    }

    private void addOperation(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View viewAlert = LayoutInflater.from(getContext()).inflate(R.layout.alert_add_operation, null);
        builder.setView(viewAlert);
        final EditText editTextName = (EditText) viewAlert.findViewById(R.id.editTextAccountName);
        final EditText editTextValue = (EditText) viewAlert.findViewById(R.id.editTextAccountValue);

        builder.setTitle("Opération");
        builder.setMessage("Ajouter une opération");
        builder.setIcon(R.drawable.ic_add_white_48dp);
        builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addOperation(getContext(), mAccountId, editTextName.getText().toString(), Double.valueOf(editTextValue.getText().toString()));
            }
        });
        builder.setNegativeButton("Annuler", null);
        builder.show();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOperationAdapter.addAll(AccountOperation.getAllOperationFromAccountId(getContext(), mAccountId));
        initUI();
    }

    private void initUI() {
        Log.d(TAG,"account id :"+ mAccountId);
        double count = AccountOperation.getAccountOperationCountFromAccountId(getContext(), mAccountId);
        mCollapsingToolbarLayout.setTitle("" + count + " €");
        if (count > 0) {
            mTextViewTendance.setText("+");
            mTextViewTendance.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else if (count < 0) {
            mTextViewTendance.setText("-");
            mTextViewTendance.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            mTextViewTendance.setText("=");
            mTextViewTendance.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    private void addOperation(Context context, long accountId, String name, double value) {
        mOperationAdapter.add(AccountOperation.addOperation(context, accountId, name, value, new Date().getTime()));
        mOperationAdapter.notifyDataSetChanged();
        initUI();
    }

}
