package com.example.bitcoinstats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient;

public class MempoolActivity extends AppCompatActivity implements TransactionAdapter.OnItemClickListener {

    public static final String EXTRA_TXID = "txId";
    public static final String EXTRA_INPUT_ADDRESSES = "inputAddresses";
    public static final String EXTRA_OUTPUT_ADDRESSES = "outputAddresses";
    public static final String EXTRA_INPUT_BTC_VALUE = "inputBTCValue";
    public static final String EXTRA_OUTPUT_BTC_VALUE = "outputBTCValue";

    private RecyclerView mRecyclerView;
    private TransactionAdapter mTransactionAdapter;
    BitcoinJSONRPCClient bitcoinClient = null;
    ArrayList<TransactionInfo> transactions = new ArrayList<TransactionInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mempool);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String user = "_";
        String password = "_";
        String host = "_";
        String port = "_";

        try {
            URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
            bitcoinClient = new BitcoinJSONRPCClient(url);

            if (bitcoinClient == null) {
                Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
            } else {
                GetMempool task = new GetMempool();
                task.execute();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, TxDetailActivity.class);
        TransactionInfo clickedItem = transactions.get(position);

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

        detailIntent.putExtra(EXTRA_TXID, clickedItem.getTxId());
        detailIntent.putStringArrayListExtra(EXTRA_INPUT_BTC_VALUE, inputBTCValue);
        detailIntent.putStringArrayListExtra(EXTRA_OUTPUT_BTC_VALUE, outputBTCValue);
        detailIntent.putStringArrayListExtra(EXTRA_INPUT_ADDRESSES, (ArrayList<String>) inputAddresses);
        detailIntent.putStringArrayListExtra(EXTRA_OUTPUT_ADDRESSES, (ArrayList<String>) outputAddresses);

        startActivity(detailIntent);
    }

    private class GetMempool extends AsyncTask<URL, Integer, String> {
        protected String doInBackground(URL... urls) {
            List<String> mempoolTransactions = bitcoinClient.getRawMemPool();
            for (int i = 0; i < mempoolTransactions.size(); i++) {
                BitcoinJSONRPCClient.RawTransaction currentTransaction = bitcoinClient.getRawTransaction(mempoolTransactions.get(i));
                String txId = currentTransaction.txId();
                List<BitcoindRpcClient.RawTransaction.In> inputs = currentTransaction.vIn();
                List<BitcoindRpcClient.RawTransaction.Out> outputs = currentTransaction.vOut();
                transactions.add(new TransactionInfo(txId, inputs, outputs));
            }
            mTransactionAdapter = new TransactionAdapter( MempoolActivity.this, transactions);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    mRecyclerView.setAdapter(mTransactionAdapter);
                    mTransactionAdapter.setOnItemClickListener(MempoolActivity.this);
                }
            });

            return "Finished";
        }

        protected void onPostExecute(String result) {
            Toast.makeText(MempoolActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}