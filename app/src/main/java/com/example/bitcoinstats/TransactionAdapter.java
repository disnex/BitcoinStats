package com.example.bitcoinstats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {
    private Context mContext;
    private List<TransactionInfo> mTransationList = new ArrayList<TransactionInfo>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public TransactionAdapter(Context context, ArrayList<TransactionInfo> transactionList) {
        mContext = context;
        mTransationList = transactionList;
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.transaction_item, parent, false);
        return new TransactionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        TransactionInfo transaction = mTransationList.get(position);

        String txId = transaction.getTxId();

        holder.textViewTxId.setText(txId);
    }

    @Override
    public int getItemCount() {
        return mTransationList.size();
    }

    public class TransactionHolder extends RecyclerView.ViewHolder {
        private TextView textViewTxId;

        public TransactionHolder(@NonNull View itemView) {
            super(itemView);
            textViewTxId = itemView.findViewById(R.id.text_view_txId);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
