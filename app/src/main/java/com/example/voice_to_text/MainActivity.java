package com.example.voice_to_text;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn;
    TextView tv;
    Intent it;
    SpeechRecognizer mRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn);
        tv = (TextView) findViewById(R.id.tv);
        btn.setOnClickListener(this);


        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO},1);
        }

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(recognitionListener);
        it = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        it.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        it.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == 1){
            if(grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "이 앱을 실행하려면 RECORD_AUDIO 권한이 필요!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
    public void onClick(View view){
        btn.setText("말 하는 중....");
        mRecognizer.startListening(it);
    }
    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            btn.setText("음성인식 대기중....");
            tv.setText("");
        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {
            btn.setText("버튼을 누르고 말하세요");
        }

        @Override
        public void onError(int i) {
            tv.setText("말이 없네요");
            btn.setText("버튼을 누르고 말하세요");
        }

        @Override
        public void onResults(Bundle bundle) {
            ArrayList<String> mResult = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            tv.setText(rs[0]);
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };

}