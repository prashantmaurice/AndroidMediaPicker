package com.prashantmaurice.android.mediapickersample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.prashantmaurice.android.mediapicker.ExternalInterface.ResultData;
import com.prashantmaurice.android.mediapicker.MediaPicker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    View btn_selectmultiple, btn_selectdefault;
    LinearLayout imageGallery;

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
        imageGallery = (LinearLayout) findViewById(R.id.imageGallery);
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
//                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("image/*");
//                startActivityForResult(intent, REQUEST_PICK_DEFAULTGALLERY);
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
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
                    List<Uri> picUris = dataObject.getSelectedUri();
                    addImagesToThegallery(picUris);
                    if(picUris.size()>0){
                        Uri uri = picUris.get(0);
                        Utils.showToast(this,"Picked "+uri.getPath());
                    }
                    Utils.showToast(this,"Picked "+picUris.size()+" images");
                    break;
                case REQUEST_PICK_DEFAULTGALLERY:
                    Uri uri = data.getData();
                    List<Uri> picUris2 = new ArrayList<>();
                    picUris2.add(uri);
                    addImagesToThegallery(picUris2);
                    Utils.showToast(this,"Picked "+uri.getPath());
            }
        }else{
            Utils.showToast(this,"Cancelled Request");
        }
    }

    private void addImagesToThegallery(List<Uri> uriList) {
        imageGallery.removeAllViews();
        for (Uri image : uriList) {
            try {
                imageGallery.addView(getImageView(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private View getImageView(Uri image) throws IOException {
        ImageView imageView = new ImageView(getApplicationContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200, 200);
        lp.setMargins(0, 0, 10, 0);
        imageView.setLayoutParams(lp);
        imageView.setImageBitmap(getImage(image));
        return imageView;
    }

    private Bitmap getImage(Uri imageUri) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        InputStream inputStream = getContentResolver().openInputStream( imageUri );
        Bitmap bmp = BitmapFactory.decodeStream(inputStream, null, options);
        if( inputStream != null ) inputStream.close();
        return bmp;
    }
}
