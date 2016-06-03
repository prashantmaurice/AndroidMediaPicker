package com.prashantmaurice.android.mediapickersample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.prashantmaurice.android.mediapicker.ExternalInterface.ResultData;
import com.prashantmaurice.android.mediapicker.ExternalInterface.SelectionObject;
import com.prashantmaurice.android.mediapicker.MediaPicker;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    View btn_selectmultiple, btn_selectdefault;

    static final int REQUEST_PICK_MULTIPLE = 1001;
    static final int REQUEST_PICK_DEFAULTGALLERY = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        initializeListeners();
    }

    private void initializeViews() {
        btn_selectmultiple = findViewById(R.id.btn_selectmultiple);
        btn_selectdefault = findViewById(R.id.btn_selectdefault);
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
        btn_selectdefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_PICK_DEFAULTGALLERY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            switch (requestCode){
                case REQUEST_PICK_MULTIPLE:
                    ResultData dataObject = MediaPicker.getResultData(data);
                    List<SelectionObject> pics = dataObject.getSelectedPics();
                    if(pics.size()>0){
                        Uri uri = pics.get(0).getUri();
                        Utils.showToast(this,"Picked "+uri.getPath());
                    }
                    Utils.showToast(this,"Picked "+pics.size()+" images");
                    break;
                case REQUEST_PICK_DEFAULTGALLERY:
                    Uri uri = data.getData();
                    Utils.showToast(this,"Picked "+uri.getPath());
            }
        }else{
            Utils.showToast(this,"Cancelled Request");
        }
    }
}
