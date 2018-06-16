package com.sagimymon.heartme.model;

import android.support.annotation.NonNull;

import com.sagimymon.heartme.model.network.ApiService;
import com.sagimymon.heartme.model.network.RetroClient;
import com.sagimymon.heartme.presenter.HeartMePresenter;
import com.sagimymon.heartme.presenter.IPresenter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeartMeModel implements IModel {

    private static final String NON_WORD_CHARACTERS = "\\W";
    private static final String SPACE_RGX = "\\u0020";
    private static final String SPACE = " ";
    private static final String DOUBLE_SPACE = "\\s+";

    private IPresenter presenter;
    private Map<String, List<String>> bloodTestNameConfigsMap;
    private BloodTestConfig[] bloodTestConfigs;

    public HeartMeModel(HeartMePresenter heartMePresenter) {
        presenter = heartMePresenter;
    }

    @Override
    public void onPresenterReady() {
        getBloodTestsConfigFromServer();
    }

    @Override
    public Map<String, List<String>> getDataMap() {
        return bloodTestNameConfigsMap;
    }

    private void getBloodTestsConfigFromServer() {
        ApiService api = RetroClient.getApiService();
        Call<BloodTests> call = api.getBloodTest();
        call.enqueue(new Callback<BloodTests>() {
            @Override
            public void onResponse(Call<BloodTests> call, Response<BloodTests> response) {
                if(response.isSuccessful()) {
                    processServerResponse(response);
                    presenter.onTestSetDownloaded();
                } else {
                    presenter.onErrorInServerResponse();
                }
            }

            @Override
            public void onFailure(Call<BloodTests> call, Throwable t) {
                presenter.onNetworkError();
            }
        });
    }

    private void processServerResponse(Response<BloodTests> response) {
        bloodTestConfigs = response.body().getBloodTestConfig();

        if(bloodTestConfigs != null && bloodTestConfigs.length > 0) {
            bloodTestNameConfigsMap = new HashMap<>();
            for (BloodTestConfig config : bloodTestConfigs) {
                String testName = config.getName();
                String[] words = normalizeString(testName);

                bloodTestNameConfigsMap.put(testName, Arrays.asList(words));
            }
        }
    }

    @NonNull
    public static String[] normalizeString(String test) {
        test = test.replaceAll(NON_WORD_CHARACTERS, SPACE);
        test = test.replaceAll(DOUBLE_SPACE, SPACE);
        test = test.toLowerCase();
        return test.split(SPACE_RGX);
    }

    @Override
    public BloodTestConfig getTestFromDataSetByName(String testName){

        BloodTestConfig retConfig = null;
        for(BloodTestConfig config : bloodTestConfigs){
            if(config.getName().equals(testName)){
                retConfig = config;
                break;
            }
        }
        return retConfig;
    }
}
