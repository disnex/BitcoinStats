package com.example.bitcoinstats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient;

import static com.example.bitcoinstats.MainActivity.host;
import static com.example.bitcoinstats.MainActivity.password;
import static com.example.bitcoinstats.MainActivity.port;
import static com.example.bitcoinstats.MainActivity.user;

public class LastBlocksActivity extends AppCompatActivity implements BlockAdapter.OnItemClickListener {

    public static final String EXTRA_HASH = "hash";
    public static final String EXTRA_SIZE = "size";
    public static final String EXTRA_HEIGHT = "height";
    public static final String EXTRA_CONFIRMATIONS = "confirmations";
    public static final String EXTRA_TXS = "txs";

    private RecyclerView mRecyclerView;
    private BlockAdapter blockAdapter;
    BitcoinJSONRPCClient bitcoinClient = null;
    ArrayList<BlockInfo> blocks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_blocks);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_last_blocks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
            bitcoinClient = new BitcoinJSONRPCClient(url);

            if (bitcoinClient == null) {
                Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
            } else {
                GetLastTenBlocks task = new GetLastTenBlocks();
                task.execute();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, BlockDetailActivity.class);
        BlockInfo clickedItem = blocks.get(position);

        String hash = clickedItem.getHash();
        Integer confirmations = clickedItem.getConfirmations();
        Integer size = clickedItem.getSize();
        Integer height = clickedItem.getHeight();
        List<String> txIDs = clickedItem.getTxIDs();

 /*

        List<BitcoindRpcClient.RawTransaction.In> input;
        List<BitcoindRpcClient.RawTransaction.Out> output;

        ArrayList<String> inputBTCValue = new ArrayList<>();
        ArrayList<String> outputBTCValue = new ArrayList<>();

        List<String> inputAddresses = new ArrayList<>();
        ArrayList<String> outputAddresses = new ArrayList<>();

        input = clickedItem.getInputs();
        output = clickedItem.getOutputs();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            for (int i = 0; i < input.size(); i++) {
                BitcoindRpcClient.RawTransaction.Out txOut;
                txOut = input.get(i).getTransactionOutput();
                inputBTCValue.add(String.valueOf(txOut.value().doubleValue()));
                inputAddresses.addAll(txOut.scriptPubKey().addresses());
            }

            for (int i = 0; i < output.size(); i++) {
                outputBTCValue.add(String.valueOf(output.get(i).value().doubleValue()));
                outputAddresses.addAll(output.get(i).scriptPubKey().addresses());
            }
        }
*/
        detailIntent.putExtra(EXTRA_HASH, hash);
        detailIntent.putExtra(EXTRA_SIZE, size);
        detailIntent.putExtra(EXTRA_CONFIRMATIONS, confirmations);
        detailIntent.putExtra(EXTRA_HEIGHT, height);
        detailIntent.putStringArrayListExtra(EXTRA_TXS, (ArrayList<String>) txIDs);

        startActivity(detailIntent);
    }

    private class GetLastTenBlocks extends AsyncTask<URL, Integer, String> {
        protected String doInBackground(URL... urls) {
            Integer lastBlock = bitcoinClient.getBlockCount();
            for (int i = lastBlock - 9; i < lastBlock + 1; i++) {
                BitcoindRpcClient.Block block = bitcoinClient.getBlock(i);

                String hash = block.hash();
                Integer confirmations = block.confirmations();
                Integer size = block.size();
                Integer height = block.height();
                List<String> txIds = block.tx();

                blocks.add(new BlockInfo(hash, confirmations, size, height, txIds));
            }
            blockAdapter = new BlockAdapter( LastBlocksActivity.this, blocks);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    mRecyclerView.setAdapter(blockAdapter);
                    blockAdapter.setOnItemClickListener(LastBlocksActivity.this);
                }
            });

            return "Finished";
        }

        protected void onPostExecute(String result) {
            Toast.makeText(LastBlocksActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}