package com.example.bitcoinstats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.bitcoinstats.MempoolActivity.EXTRA_INPUT_ADDRESSES;
import static com.example.bitcoinstats.MempoolActivity.EXTRA_INPUT_BTC_VALUE;
import static com.example.bitcoinstats.MempoolActivity.EXTRA_OUTPUT_ADDRESSES;
import static com.example.bitcoinstats.MempoolActivity.EXTRA_OUTPUT_BTC_VALUE;
import static com.example.bitcoinstats.MempoolActivity.EXTRA_TXID;

public class TxDetailActivity extends AppCompatActivity {

    TextView txIdValue, inputValue, outputValue, feeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_detail);

        txIdValue = (TextView)findViewById(R.id.textView_detail_txId_value);
        inputValue = (TextView)findViewById(R.id.textView_detail_input_value);
        outputValue = (TextView)findViewById(R.id.textView_detail_output_value);
        feeValue = (TextView)findViewById(R.id.textView_detail_fee_value);

        Intent intent = getIntent();
        String txID = intent.getStringExtra(EXTRA_TXID);
        Double fee = 0.0;
        ArrayList<String> inputAddresses = intent.getStringArrayListExtra(EXTRA_INPUT_ADDRESSES);
        ArrayList<String> outputAddresses = intent.getStringArrayListExtra(EXTRA_OUTPUT_ADDRESSES);
        ArrayList<String> inputBTCvalue = intent.getStringArrayListExtra(EXTRA_INPUT_BTC_VALUE);
        ArrayList<String> outputBTCValue = intent.getStringArrayListExtra(EXTRA_OUTPUT_BTC_VALUE);
        Double in = 0.0;
        Double out = 0.0;



        txIdValue.setText(txID);

        for (int i = 0; i < inputAddresses.size(); i++) {
            Double temp = Double.valueOf(inputBTCvalue.get(i));
            String address = inputAddresses.get(i);
            String BTCvalue = String.format(Locale.US, " \n [ %.8f BTC] \n", temp);
            inputValue.append(address + BTCvalue);
            in = in + temp;
        }

        for (int i = 0; i < outputAddresses.size(); i++) {
            Double temp = Double.valueOf(outputBTCValue.get(i));
            String address = outputAddresses.get(i);
            String BTCvalue = String.format(Locale.US, " \n [ %.8f BTC] \n", temp);
            outputValue.append(address + BTCvalue);

            out = out + temp;
        }

        fee = in - out;
        feeValue.setText(String.format(Locale.US,"%.8f BTC", fee));

    }
}