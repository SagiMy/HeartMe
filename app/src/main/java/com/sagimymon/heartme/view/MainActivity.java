package com.sagimymon.heartme.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sagimymon.heartme.R;
import com.sagimymon.heartme.presenter.HeartMePresenter;
import com.sagimymon.heartme.presenter.IPresenter;

public class MainActivity extends AppCompatActivity implements IView {

    private IPresenter presenter;

    private EditText editTextTestName, editTextTestResult;
    private Button buttonSend;
    private ImageView imageViewTestResult;
    private TextView textViewOutputTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new HeartMePresenter(this, this);
        bindViews();
    }

    private void bindViews() {
        editTextTestName = findViewById(R.id.editTextTestName);
        editTextTestResult = findViewById(R.id.editTextTestResult);
        imageViewTestResult = findViewById(R.id.imageViewTestResult);
        textViewOutputTest = findViewById(R.id.textViewOutputTest);
        buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testNameInput = editTextTestName.getText().toString();
                String testResultInput = editTextTestResult.getText().toString();

                if(!testNameInput.isEmpty() && !testResultInput.isEmpty()) {
                    presenter.onSendButtonClicked(testNameInput, testResultInput);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewCreated();
    }

    @Override
    public void onBadResponse() {
        Toast.makeText(this, R.string.toast_bad_response, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNetworkError() {
        Toast.makeText(this, R.string.toast_no_network, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDataLoadedFromServer() {
        buttonSend.setEnabled(true);
        buttonSend.setClickable(true);
    }

    @Override
    public void onOutputPublished(HeartMePresenter.TestResultOutput retOutput, String testName) {
        hideKeyboard();
        imageViewTestResult.setImageResource(retOutput.getResDrawableId());
        textViewOutputTest.setText(testName);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(imm != null) {
            View view = getCurrentFocus();
            if (view == null) {
                view = new View(this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onViewDestroy();
    }
}
