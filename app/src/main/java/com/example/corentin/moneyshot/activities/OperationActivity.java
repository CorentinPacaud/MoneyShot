package com.example.corentin.moneyshot.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.corentin.moneyshot.R;
import com.example.corentin.moneyshot.fragments.OperationFragment;

public class OperationActivity extends AppCompatActivity {

    private static final String EXTRA_ACCOUNT_ID = OperationFragment.EXTRA_ACCOUNT_ID;

    public static Intent newIntent(Context context, long accountId) {
        Intent intent = new Intent(context, OperationActivity.class);
        intent.putExtra(EXTRA_ACCOUNT_ID, accountId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, OperationFragment.newInstance(getIntent().getLongExtra(EXTRA_ACCOUNT_ID, 0))).commit();
    }

}
