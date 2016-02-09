package com.example.corentin.moneyshot.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.corentin.moneyshot.R;
import com.example.corentin.moneyshot.model.AccountOperation;
import com.example.corentin.moneyshot.model.BankAccount;

import java.util.ArrayList;

/**
 * Created by Corentin on 06/11/2015.
 */
public class OperationAdapter extends RecyclerView.Adapter<OperationAdapter.ViewHolder> {


    private ArrayList<AccountOperation> mArray;

    public OperationAdapter() {
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
        final AccountOperation operation = mArray.get(position);
        holder.setText(operation.getName());
        holder.textViewValue.setText("" + operation.getValue() + " €");
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AccountOperation.removeOperation(v.getContext(), operation.getId());
                        mArray.remove(operation);
                        OperationAdapter.this.notifyDataSetChanged();
                    }
                });
                builder.setTitle("Opération");
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setMessage("Supprimer cette opération ?");
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

    public void add(AccountOperation accountOperation) {
        mArray.add(accountOperation);
        notifyDataSetChanged();
    }

    public void remove(BankAccount bankAccount) {
        mArray.remove(bankAccount);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<AccountOperation> list) {
        mArray.addAll(list);
        notifyDataSetChanged();
    }


}
