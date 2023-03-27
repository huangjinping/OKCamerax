package com.eastbay.camarsx2022;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.eastbay.camarsx2022.easyBayCamerx.CameraMiniParEastbayam;
import com.eastbay.camarsx2022.easyBayCamerx.Mini1CEastbayamer3aConstant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Mai12nActivEastbayity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView img_action = findViewById(R.id.notoriouspramcompeteclarify);
        img_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asdasd();
            }
        });
    }

    private void asdasd() {
        CameraMiniParEastbayam mCameraParam = new CameraMiniParEastbayam.B9uildEastbayer()
                .setRequestCode(1018)
                .setActivity(Mai12nActivEastbayity.this)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case 1018:
                        String path = data.getStringExtra(Mini1CEastbayamer3aConstant.PICTURE_PATH_KEY);
                        Log.d("onActivityResult","==========="+path);
                        break;
                }
            }
    }
}