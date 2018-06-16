package com.sagimymon.heartme.view;

import com.sagimymon.heartme.presenter.HeartMePresenter;

public interface IView {
    void onBadResponse();
    void onNetworkError();
    void onDataLoadedFromServer();
    void onOutputPublished(HeartMePresenter.TestResultOutput retOutput, String testName);
}
