package com.example.bitcoinstats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.bitcoinstats.LastBlocksActivity.EXTRA_CONFIRMATIONS;
import static com.example.bitcoinstats.LastBlocksActivity.EXTRA_HASH;
import static com.example.bitcoinstats.LastBlocksActivity.EXTRA_HEIGHT;
import static com.example.bitcoinstats.LastBlocksActivity.EXTRA_SIZE;
import static com.example.bitcoinstats.LastBlocksActivity.EXTRA_TXS;

public class BlockDetailActivity extends AppCompatActivity {

    TextView hash, size, confirmations, height, txIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_detail);

        hash = (TextView)findViewById(R.id.textView_block_detail_hash_value);
        size = (TextView)findViewById(R.id.textView_block_detail_size_value);
        confirmations = (TextView)findViewById(R.id.textView_block_detail_confirmations_value);
        height = (TextView)findViewById(R.id.textView_block_detail_height_value);
        txIds = (TextView)findViewById(R.id.textView_block_detail_txs_value);

        Intent intent = getIntent();
        String hashValue = intent.getStringExtra(EXTRA_HASH);
        Integer sizeValue = intent.getIntExtra(EXTRA_SIZE, 0);
        Integer confirmationsValue = intent.getIntExtra(EXTRA_CONFIRMATIONS, 0);
        Integer heightValue = intent.getIntExtra(EXTRA_HEIGHT, 0);
        ArrayList<String> txs = intent.getStringArrayListExtra(EXTRA_TXS);

        hash.setText(hashValue);
        size.setText(String.valueOf(sizeValue));
        confirmations.setText(String.valueOf(confirmationsValue));
        height.setText(String.valueOf(heightValue));

        for (int i = 0; i < txs.size(); i++) {
            txIds.append(txs.get(i) + "\n\n");
        }

    }
}