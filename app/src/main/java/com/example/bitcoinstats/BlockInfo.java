package com.example.bitcoinstats;

import java.util.List;

public class BlockInfo {

    private String hash;
    private Integer confirmations;
    private Integer size;
    private Integer height;
    private List<String> txIDs;

    public BlockInfo(String hash, Integer confirmations, Integer size, Integer height, List<String> txIDs) {
        this.hash = hash;
        this.confirmations = confirmations;
        this.size = size;
        this.height = height;
        this.txIDs = txIDs;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Integer confirmations) {
        this.confirmations = confirmations;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public List<String> getTxIDs() {
        return txIDs;
    }

    public void setTxIDs(List<String> txIDs) {
        this.txIDs = txIDs;
    }
}
