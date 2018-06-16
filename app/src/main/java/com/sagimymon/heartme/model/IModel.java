package com.sagimymon.heartme.model;

import java.util.List;
import java.util.Map;

public interface IModel {
    void onPresenterReady();
    Map<String, List<String>> getDataMap();
    BloodTestConfig getTestFromDataSetByName(String testName);
}
