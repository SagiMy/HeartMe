package com.sagimymon.heartme.presenter;

public interface IPresenter {
    void onViewCreated();
    void onErrorInServerResponse();
    void onNetworkError();
    void onSendButtonClicked(String testNameInput, String testResultInput);
    void onTestSetDownloaded();
    void onViewDestroy();
}
