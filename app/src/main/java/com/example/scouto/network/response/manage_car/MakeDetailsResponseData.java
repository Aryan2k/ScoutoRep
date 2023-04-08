package com.example.scouto.network.response.manage_car;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MakeDetailsResponseData {
    @SerializedName("Count")
    @Expose
    private Integer count;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Results")
    @Expose
    private ArrayList<MakeDetails> results;
    @SerializedName("SearchCriteria")
    @Expose
    private String searchCriteria;

    public Integer getCount() {
        return count;
    }


    public String getMessage() {
        return message;
    }


    public ArrayList<MakeDetails> getResults() {
        return results;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResults(ArrayList<MakeDetails> results) {
        this.results = results;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }


}
