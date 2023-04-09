package com.example.scouto.network.response.manage_car;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class ModelDetailsResponseData {
    @SerializedName("Count")
    @Expose
    private Integer count;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("SearchCriteria")
    @Expose
    private String searchCriteria;
    @SerializedName("Results")
    @Expose
    private ArrayList<ModelDetails> results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public ArrayList<ModelDetails> getResults() {
        return results;
    }

    public void setResults(ArrayList<ModelDetails> results) {
        this.results = results;
    }
}
