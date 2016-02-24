package com.example.corentin.moneyshot.fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
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

    FloatingActionButton mFabSubmitAccountName;
    CardView mViewAddAccount;
    EditText mEditTextAccountName;

    private FloatingActionButton mFabAdd;
    private HeaderAccount mHeaderAccount;


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

        mViewAddAccount = (CardView) mRootView.findViewById(R.id.viewAddAccount);
        mEditTextAccountName = (EditText) mRootView.findViewById(R.id.editAddAccount);
        mFabSubmitAccountName = (FloatingActionButton) mRootView.findViewById(R.id.fabSubmit);
        mViewAddAccount.setVisibility(View.INVISIBLE);

        mRecyclerView.setHasFixedSize(true);

        mHeaderAccount = new HeaderAccount(inflater.inflate(R.layout.header_account, null), null);
        mRecyclerView.addItemDecoration(mHeaderAccount);

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
        mFabSubmitAccountName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditTextAccountName.getText().toString().length() > 0) {
                    addAccount(mEditTextAccountName.getText().toString());
                    mEditTextAccountName.setText("");
                }
                animateFabIn();

                Animator animator1 = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    animator1 = ViewAnimationUtils.createCircularReveal(mViewAddAccount, mViewAddAccount.getWidth() - mFabSubmitAccountName.getWidth() / 2, mFabSubmitAccountName.getHeight() / 2, mViewAddAccount.getWidth(), 0);
                    animator1.setDuration(300);
                    animator1.setInterpolator(new AccelerateInterpolator(1f));
                    animator1.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mViewAddAccount.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    animator1.start();
                }
            }
        });

        mEditTextAccountName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (start == 0 && after > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mFabSubmitAccountName.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_white_48dp, null));
                    }
                }
                if (after == 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mFabSubmitAccountName.setImageDrawable(getResources().getDrawable(R.drawable.ic_clear_white_48dp, null));
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBankAccountAdapter.notifyDataSetChanged();
        animateFabIn();
    }

    private void animateFabIn() {
        mFabAdd.setVisibility(View.INVISIBLE);
        AnimatorSet animator = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.fab_in_scale);
        animator.setTarget(mFabAdd);
        mFabAdd.setVisibility(View.VISIBLE);
        animator.start();
    }


    private void displayAddScreen() {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        builder.show();*/

        mFabAdd.setVisibility(View.GONE);

        mFabSubmitAccountName.setVisibility(View.INVISIBLE);
        mFabSubmitAccountName.requestLayout();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator animator1 = ViewAnimationUtils.createCircularReveal(mViewAddAccount, mViewAddAccount.getWidth() - mFabSubmitAccountName.getWidth() / 2, mFabSubmitAccountName.getHeight() / 2, 0, mViewAddAccount.getWidth());
            animator1.setDuration(300);
            animator1.setInterpolator(new AccelerateInterpolator(1f));
            animator1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mViewAddAccount.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    AnimatorSet animator = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.fab_in_scale);
                    animator.setTarget(mFabSubmitAccountName);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mFabSubmitAccountName.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    animator.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator1.start();
        }
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
