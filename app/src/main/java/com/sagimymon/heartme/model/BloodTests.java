package com.sagimymon.heartme.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class BloodTests {

    @SerializedName("bloodTestConfig")
    @Expose
    private BloodTestConfig[] bloodTestConfig;

    public BloodTestConfig[] getBloodTestConfig () { return bloodTestConfig; }

    public void setBloodTestConfig (BloodTestConfig[] bloodTestConfig) { this.bloodTestConfig = bloodTestConfig; }

    @Override
    public String toString() { return "BloodTests [bloodTestConfig = "+ Arrays.toString(bloodTestConfig) +"]"; }
}
