package com.sagimymon.heartme.presenter;

import android.content.Context;

import com.sagimymon.heartme.R;
import com.sagimymon.heartme.model.BloodTestConfig;
import com.sagimymon.heartme.model.HeartMeModel;
import com.sagimymon.heartme.model.IModel;
import com.sagimymon.heartme.view.IView;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import info.debatty.java.stringsimilarity.JaroWinkler;

public class HeartMePresenter implements IPresenter {

    public enum TestResultOutput{

        UNKNOWN(R.mipmap.unknown), GOOD(R.mipmap.good), BAD(R.mipmap.bad);

        private final int resDrawableId;

        TestResultOutput(int resDrawable) { this.resDrawableId = resDrawable; }

        public int getResDrawableId() { return resDrawableId;}
    }

    private static final double TEST_THRESHOLD = 0.65;
    private static final double WORD_THRESHOLD = 0.8;

    private IView view;
    private IModel model;
    private JaroWinkler jaroWinkler;
    private Context context;

    public HeartMePresenter(IView iView, Context context){
        this.view = iView;
        this.context = context;
        model = new HeartMeModel(this);
        jaroWinkler = new JaroWinkler();
    }

    @Override
    public void onViewCreated() {
        model.onPresenterReady();
    }

    @Override
    public void onErrorInServerResponse() {
        view.onBadResponse();
    }

    @Override
    public void onNetworkError() {
        view.onNetworkError();
    }

    @Override
    public void onSendButtonClicked(String testNameInput, String testResultInput) {
        String testName = getMostSimilarTestNameToUserInput(testNameInput);
        TestResultOutput retOutput = TestResultOutput.UNKNOWN;

        if(testName != null){
            retOutput = getTestResults(testName, testResultInput);
        }else{
            testName = context.getString(R.string.unknown_test);
        }

        view.onOutputPublished(retOutput, testName);
    }

    private TestResultOutput getTestResults(String testName, String testResultInput) {
        TestResultOutput retOutput = TestResultOutput.UNKNOWN;

        BloodTestConfig config = model.getTestFromDataSetByName(testName);
        if(config != null){
            if(Integer.valueOf(testResultInput) > Integer.valueOf(config.getThreshold())){
                retOutput = TestResultOutput.BAD;
            }else{
                retOutput = TestResultOutput.GOOD;
            }
        }

        return retOutput;
    }

    private String getMostSimilarTestNameToUserInput(String testNameInput) {
        String maxSimilarityKey = null;

        if(testNameInput != null && !testNameInput.isEmpty()) {
            Map<String, List<String>> dataMap =  model.getDataMap();

            maxSimilarityKey = findSimilarTestsByWholeInput(testNameInput, dataMap);

            if(maxSimilarityKey == null) {
                maxSimilarityKey = findSimilarTestsByWords(testNameInput, dataMap);
            }
        }
        return maxSimilarityKey;
    }

    private String findSimilarTestsByWholeInput(String testNameInput, Map<String, List<String>> dataMap) {
        String maxSimilarityKey = null;
        double maxSimilarity = 0;

        for (String currentKey : dataMap.keySet()) {
            double similarity = findStringSimilarity(currentKey, testNameInput);

            if (similarity > maxSimilarity && similarity > TEST_THRESHOLD) {
                maxSimilarity = similarity;
                maxSimilarityKey = currentKey;
            }
        }
        return maxSimilarityKey;
    }

    private String findSimilarTestsByWords(String testNameInput, Map<String, List<String>> dataMap) {
        String maxSimilarityKey = null;
        String[] words = HeartMeModel.normalizeString(testNameInput);
        if(words.length > 0) {

            Iterator<String> keyIterator = dataMap.keySet().iterator();
            int maxSimilarWordsCount = 0;
            while (keyIterator.hasNext()) {

                String currentKey = keyIterator.next();
                int similarWordsCount = 0;
                List<String> dataWords = dataMap.get(currentKey);

                for (String dataWord : dataWords) {
                    for (String word : words) {
                        double similarity = findStringSimilarity(dataWord, word);
                        if (similarity > WORD_THRESHOLD) {
                            similarWordsCount++;
                        }
                    }
                }

                if (similarWordsCount > maxSimilarWordsCount) {
                    maxSimilarWordsCount = similarWordsCount;
                    maxSimilarityKey = currentKey;
                }
            }

        }
        return maxSimilarityKey;
    }

    @Override
    public void onTestSetDownloaded() {
        view.onDataLoadedFromServer();
    }

    @Override
    public void onViewDestroy() {
        context = null;
    }

    private double findStringSimilarity(String s1, String s2){
        return jaroWinkler.similarity(s2, s1);
    }
}