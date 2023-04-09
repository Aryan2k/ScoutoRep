package com.example.scouto.network.response.manage_car;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class MakeDetails {
    @SerializedName("Make_ID")
    @Expose
    private Long makeID;
    @SerializedName("Make_Name")
    @Expose
    private String makeName;

    public Long getMakeID() {
        return makeID;
    }

    public String getMakeName() {
        return makeName;
    }

    public void setMakeID(Long makeID) {
        this.makeID = makeID;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    @NonNull
    @Override
    public String toString() {
        return makeName;
    }
}
