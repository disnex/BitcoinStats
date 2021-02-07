package com.example.bitcoinstats;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient;

import static com.example.bitcoinstats.MainActivity.host;
import static com.example.bitcoinstats.MainActivity.password;
import static com.example.bitcoinstats.MainActivity.port;
import static com.example.bitcoinstats.MainActivity.user;

public class BlockchainInfoActivity extends AppCompatActivity {

    BitcoinJSONRPCClient bitcoinClient = null;
    TextView chain, blocks, difficulty, version, localServices, warnings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blockchain_info);

        chain = (TextView)findViewById(R.id.textView_blockchain_chain_value);
        blocks = (TextView)findViewById(R.id.textView_blockchain_blocks_value);
        difficulty = (TextView)findViewById(R.id.textView_blockchain_difficulty_value);
        version = (TextView)findViewById(R.id.textView_network_version_value);
        localServices = (TextView)findViewById(R.id.textView_network_localServices_value);
        warnings = (TextView)findViewById(R.id.textView_network_warnings_value);


        try {
            URL url = new URL("http://" + user + ':' + password + "@" + host + ":" + port + "/");
            bitcoinClient = new BitcoinJSONRPCClient(url);

            if (bitcoinClient == null) {
                Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
            } else {
                GetBlockchainInfo task = new GetBlockchainInfo();
                task.execute();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private class GetBlockchainInfo extends AsyncTask<URL, Integer, String> {
        protected String doInBackground(URL... urls) {
            final BitcoindRpcClient.BlockChainInfo blockChainInfo = bitcoinClient.getBlockChainInfo();
            final BitcoindRpcClient.NetworkInfo networkInfo =  bitcoinClient.getNetworkInfo();
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    chain.setText(blockChainInfo.chain());
                    blocks.setText(String.valueOf(blockChainInfo.blocks()));
                    difficulty.setText(String.valueOf(blockChainInfo.difficulty()));
                    version.setText(String.valueOf(networkInfo.subversion()));
                    localServices.setText(String.valueOf(networkInfo.localServices()));
                    warnings.setText(String.valueOf(networkInfo.warnings()));
                }
            });

            return "Finished";
        }

        protected void onPostExecute(String result) {
            Toast.makeText(BlockchainInfoActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}