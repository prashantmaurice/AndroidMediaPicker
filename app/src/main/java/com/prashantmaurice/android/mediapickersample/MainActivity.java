package com.prashantmaurice.android.mediapickersample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.prashantmaurice.android.mediapicker.MediaPicker;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    View btn_selectmultiple;

    static final int REQUEST_PICK_MULTIPLE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        initializeListeners();
    }

    private void initializeViews() {
        btn_selectmultiple = findViewById(R.id.btn_selectmultiple);
    }

    private void initializeListeners() {
        btn_selectmultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new MediaPicker.IntentBuilder()
                        .selectMultiple(true)
                        .build(MainActivity.this);
                startActivityForResult(intent, REQUEST_PICK_MULTIPLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            switch (requestCode){
                case REQUEST_PICK_MULTIPLE:
                    MediaPicker.IntentBuilder.IntentData dataObject = MediaPicker.IntentBuilder.parseResult(data);
                    List<String> pics = dataObject.getSelectedPics();
                    Utils.showToast(this,"Picked "+pics.size()+" images");
                    break;
            }
        }else{
            Utils.showToast(this,"Cancelled Request");
        }


    }
}
