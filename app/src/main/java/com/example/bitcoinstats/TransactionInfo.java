package com.example.bitcoinstats;

import java.util.List;

import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient;

public class TransactionInfo {

    private String txId;
    private List<BitcoindRpcClient.RawTransaction.In> inputs;
    private List<BitcoindRpcClient.RawTransaction.Out> outputs;

    public TransactionInfo() {}

    public TransactionInfo(String txId, List<BitcoindRpcClient.RawTransaction.In> inputs, List<BitcoindRpcClient.RawTransaction.Out> outputs) {
        this.txId = txId;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public List<BitcoindRpcClient.RawTransaction.In> getInputs() {
        return inputs;
    }

    public void setInputs(List<BitcoindRpcClient.RawTransaction.In> inputs) {
        this.inputs = inputs;
    }

    public List<BitcoindRpcClient.RawTransaction.Out> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<BitcoindRpcClient.RawTransaction.Out> outputs) {
        this.outputs = outputs;
    }
}
