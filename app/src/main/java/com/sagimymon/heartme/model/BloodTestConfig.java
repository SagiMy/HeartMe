package com.sagimymon.heartme.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BloodTestConfig {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("threshold")
    @Expose
    private String threshold;

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getThreshold ()
    {
        return threshold;
    }

    public void setThreshold (String threshold)
    {
        this.threshold = threshold;
    }

    @Override
    public String toString() { return "BloodTestConfig [name = "+name+", threshold = "+threshold+"]"; }
}
