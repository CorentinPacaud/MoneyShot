package com.example.corentin.moneyshot.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.corentin.moneyshot.R;
import com.example.corentin.moneyshot.activities.MainActivity;
import com.example.corentin.moneyshot.adapter.BankAccountAdapter;
import com.example.corentin.moneyshot.model.AccountOperation;
import com.example.corentin.moneyshot.model.BankAccount;
import com.example.corentin.moneyshot.view.HeaderAccount;
import com.github.mikephil.charting.charts.RadarChart;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    View mRootView;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    RecyclerView mRecyclerView;
    BankAccountAdapter mBankAccountAdapter;
    ImageView mImageView;
    AppBarLayout appBarLayout;
    TextView mTextViewTendance;
    LinearLayoutManager mLayoutManager;
    RadarChart mRadarChart;

    private FloatingActionButton mFabAdd;


    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccountFragment.
     */
    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_first, container, false);
        // Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //getActivity().setActionBar(toolbar);


        mFabAdd = (FloatingActionButton) mRootView.findViewById(R.id.fabAdd);
        appBarLayout = (AppBarLayout) mRootView.findViewById(R.id.appBarLayout);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) mRootView.findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.primary));

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addItemDecoration(new HeaderAccount(inflater.inflate(R.layout.header_account, null)));

        mImageView = (ImageView) mRootView.findViewById(R.id.backgroundImageView);

        mImageView.setImageResource(R.drawable.paris);

        mTextViewTendance = (TextView) mRootView.findViewById(R.id.textTendance);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mBankAccountAdapter = new BankAccountAdapter();
        mRecyclerView.setAdapter(mBankAccountAdapter);


        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAddScreen();
            }
        });


        return mRootView;
    }

    private void displayAddScreen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.alert_add_account, null);
        builder.setView(view);
        final EditText editTextName = (EditText) view.findViewById(R.id.editTextAccountName);

        builder.setTitle("Compte");
        builder.setMessage("Ajouter un compte");
        builder.setIcon(R.drawable.ic_add_white_48dp);
        builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addAccount(editTextName.getText().toString());
            }
        });
        builder.setNegativeButton("Annuler", null);
        builder.show();
    }

    private void addAccount(String name) {
        BankAccount bankAccount = BankAccount.addAccount(getContext(), name);
        mBankAccountAdapter.add(bankAccount);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).attachToolbar((android.support.v7.widget.Toolbar) mRootView.findViewById(R.id.toolbar));
        mBankAccountAdapter.addAll(BankAccount.getAllAccount(getContext()));
        double count = AccountOperation.getCountAllOperation(getContext());
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

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
