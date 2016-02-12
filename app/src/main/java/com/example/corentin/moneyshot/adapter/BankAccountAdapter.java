package com.example.corentin.moneyshot.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.corentin.moneyshot.R;
import com.example.corentin.moneyshot.activities.OperationActivity;
import com.example.corentin.moneyshot.model.AccountOperation;
import com.example.corentin.moneyshot.model.BankAccount;

import java.util.ArrayList;

/**
 * Created by Corentin on 22/09/2015.
 */
public class BankAccountAdapter extends RecyclerView.Adapter<BankAccountAdapter.ViewHolder> {

    private static final String TAG = BankAccountAdapter.class.getSimpleName();

    private ArrayList<BankAccount> mArray;

    public BankAccountAdapter() {
        this.mArray = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_entry, null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BankAccount bankAccount = mArray.get(position);
        holder.setText(bankAccount.getName());
        Log.d(TAG,"account id :"+ bankAccount.getId());
        holder.textViewValue.setText("" + AccountOperation.getAccountOperationCountFromAccountId(holder.rootView.getContext(), bankAccount.getId()) + " €");
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  AlertDialog.Builder builder = new AlertDialog.Builder(holder.rootView.getContext());
                View view = LayoutInflater.from(holder.rootView.getContext()).inflate(R.layout.alert_add_operation, null);
                builder.setView(view);
                final EditText editTextName = (EditText) view.findViewById(R.id.editTextAccountName);
                final EditText editTextValue = (EditText) view.findViewById(R.id.editTextAccountValue);

                builder.setTitle("Opération");
                builder.setMessage("Ajouter une opératin");
                builder.setIcon(R.drawable.ic_add_white_48dp);
                builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addOperation(holder.rootView.getContext(), bankAccount.getId(), editTextName.getText().toString(), Double.valueOf(editTextValue.getText().toString()));
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Annuler", null);
                builder.show();*/
                v.getContext().startActivity(OperationActivity.newIntent(v.getContext(), bankAccount.getId()));
            }
        });
        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.rootView.getContext());

                builder.setTitle("Opération");
                builder.setMessage("Supprimer le compte");
                builder.setIcon(R.drawable.ic_add_white_48dp);
                builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount(holder.rootView.getContext(),bankAccount);
                    }
                });
                builder.setNegativeButton("Annuler", null);
                builder.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView rootView;
        TextView textViewName;
        TextView textViewValue;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = (CardView) itemView.findViewById(R.id.card);
            textViewName = (TextView) itemView.findViewById(R.id.text1);
            textViewValue = (TextView) itemView.findViewById(R.id.textValue);
        }

        public void setText(String str) {
            textViewName.setText(str);
        }
    }

    public void add(BankAccount bankAccount) {
        mArray.add(bankAccount);
        notifyDataSetChanged();
    }

    public void remove(BankAccount bankAccount) {
        mArray.remove(bankAccount);
        notifyDataSetChanged();
    }

    private void deleteAccount(Context context,BankAccount bankAccount){
        if(BankAccount.removeAccount(context,bankAccount.getId())>0){
            remove(bankAccount);
        }
    }

    public void addAll(ArrayList<BankAccount> list) {
        mArray.addAll(list);
        notifyDataSetChanged();
    }
}
