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

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.BlockHolder> {
    private Context mContext;
    private List<BlockInfo> mBlockList = new ArrayList<BlockInfo>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public BlockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.block_item, parent, false);
        return new BlockAdapter.BlockHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockHolder holder, int position) {
        BlockInfo block = mBlockList.get(position);

        String blockHash = block.getHash();

        holder.textViewBlockHash.setText(blockHash);
    }


    @Override
    public int getItemCount() {
        return mBlockList.size();
    }


    public BlockAdapter(Context context, ArrayList<BlockInfo> blockList) {
        mContext = context;
        mBlockList = blockList;
    }

    public class BlockHolder extends RecyclerView.ViewHolder {
        private TextView textViewBlockHash;

        public BlockHolder(@NonNull View itemView) {
            super(itemView);
            textViewBlockHash = itemView.findViewById(R.id.text_view_blockHash);
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
