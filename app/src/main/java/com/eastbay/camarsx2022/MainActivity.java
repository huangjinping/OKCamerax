package com.eastbay.camarsx2022;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.FileUtils;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.FileUtils;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.eastbay.camarsx2022.R;
import com.eastbay.camarsx2022.luban.Luban;
import com.eastbay.camarsx2022.luban.OnCompressListener;
import com.eastbay.camarsx2022.utils.CameraConstant;
import com.eastbay.camarsx2022.utils.CameraParam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * https://developer.android.com/jetpack/androidx/releases/camera?hl=zh-cn
 */


public class MainActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_STORAGE_WRITE_ACCESS_PERMISSION1 = 1111; //相册权限 正
    private static final int REQUEST_GALLERY1 = 1010; //相册 正
    static String msms = "";
    ImageView notoriouspramcompeteclarify;
    TextView txtaubm;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        notoriouspramcompeteclarify = findViewById(R.id.notoriouspramcompeteclarify);

        notoriouspramcompeteclarify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onTEST();
                onCliew();

            }
        });

        txtaubm = findViewById(R.id.txtaubm);
        txtaubm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openSelected(100);
//                openSelected02();
                toAlbumPopCash(106);
            }
        });

//        onTimeDisposable();
    }

    private void onTimeDisposable() {
        try {
            countDownTimer = new CountDownTimer(10 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    try {
                        int value = (int) (millisUntilFinished / 1000);
                        txtaubm.setText(String.format("%02d", value));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFinish() {
                    try {
                        onCliew();
                        finish();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            countDownTimer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //厄瓜多尔01
    public void openSelected02() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_GALLERY1);
    }

    //popcash
    public void openSelected03(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    private void openSelected(int requestCode) {
        try {
//            Intent intent = new Intent(Intent.ACTION_PICK);
//            intent.setType("image/*");
//            startActivityForResult(intent, requestCode);
            try {

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 1001);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @SuppressLint("Range")
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            if (requestCode == 1001) {
//                if (resultCode == Activity.RESULT_OK) {
//
//                    if (true) {
//                        Uri uri = data.getData();
//                        String contractPhone = null;
//                        String contactName = null;
//                        ContentResolver contentResolver = getContentResolver();
//                        Cursor cursor = null;
//                        if (uri != null) {
//                            cursor = contentResolver.query(uri, null, null, null, null);
//                        }
//                        while (cursor.moveToNext()) {
//                            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                            contractPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        }
//                        cursor.close();
//                        if (contractPhone != null) {
//                            contractPhone = contractPhone.replaceAll("-", "");
//                            contractPhone = contractPhone.replaceAll(" ", "");
//                        }
//
//
//                        Log.d("okhttp", contactName + "=========" + contractPhone);
////                Map<String, String> parms = new HashMap<>();
////                parms.put("contractPhone", "" + contractPhone);
////                parms.put("contactName", "" + contactName);
////                methodChannel.invokeMethod(changeMethod, parms);
//
//
//                    }
//                }
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//    }

    private void toAlbumPopCash(int requestCode) {
        try {
//            Intent intent = new Intent(Intent.ACTION_PICK);
//            intent.setType("image/*");
//            startActivityForResult(intent, requestCode);

            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        //相册 身份证正面
        if (data != null) {
            String stringExtra = data.getStringExtra(CameraConstant.PICTURE_PATH_KEY);
            onLuban(stringExtra);
        }
//
//        if (106 == requestCode && resultCode == RESULT_OK) {
//            Uri data1 = data.getData();
//
//            notoriouspramcompeteclarify.setImageURI(data1);
//            File file = uriToFileApiQ(data1, this);
//            Log.d("onActivityResult", "===1===" + file.getAbsolutePath());
//
//            onChcnage(file.getAbsolutePath());
//
//            File fileOut = new File(getExternalCacheDir() + "/test_" + System.currentTimeMillis() + ".jpg");
//            FileHelperUtil.compressBmpFileToTargetSize(new File(file.getAbsolutePath()), fileOut, 1024 * 1024);
//
//            Log.d("onActivityResult", "=====2==" + fileOut.getAbsolutePath());
//
//
//            File outFile = new File(getExternalCacheDir() + "/rocket_" + System.currentTimeMillis() + ".jpg");
//
//            FileUtil.compressAndGenImage(file.getAbsolutePath(), outFile.getAbsolutePath(), 500);
//
//            onLuban(file.getAbsolutePath());
////            Uri originalUri = data.getData();        //获得图片的uri
////            String[] proj = {MediaStore.Images.Media.DATA};
////            Cursor cursor = managedQuery(originalUri, proj, null, null, null);
////            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
////            cursor.moveToFirst();
////            String picturePath = cursor.getString(column_index);
////
////            Log.d("onActivityResult", "======" + picturePath);
////            onChcnage(picturePath);
//
//        } else if (REQUEST_GALLERY1 == requestCode && resultCode == RESULT_OK)
////
////            //相册 身份证正面
////            if (data != null) {
////                Uri imageData = data.getData();
////                notoriouspramcompeteclarify.setImageURI(imageData);
////
////                String path1 = FileHelperUtil.getPath(this, imageData);
////                //获取正面裁剪后的路径
////                Log.d("onActivityResult", "======" + path1);
////
////
//            }

    }


    public File uriToFileApiQ(Uri uri, Context context) {
        File file = null;
        if (uri == null) return file;
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
            String displayName = System.currentTimeMillis() + Math.round((Math.random() + 1) * 1000)
                    + "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri));

            try {
                InputStream is = contentResolver.openInputStream(uri);
                File cache = new File(context.getCacheDir().getAbsolutePath(), displayName);
                FileOutputStream fos = new FileOutputStream(cache);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    FileUtils.copy(is, fos);
                }
                file = cache;
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    public void onChcnage(String path) {
        Log.d("onActivityResult", "0000000000000000001" + path);

        new Compressor(this)
                .compressToFileAsFlowable(new File(path))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) {
                        try {
                            Log.d("onActivityResult", "0000000000000000005");
                            Log.d("onActivityResult", "0000000000000000005" + file.getAbsolutePath());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.d("onActivityResult", "0000000000000000006");

                        throwable.printStackTrace();
//                        DisplayToast("压缩失败了");
                    }
                });
    }

    private void onTEST() {
        onahisv();
        onahisv();
        onahisv();

    }


    public void onLuban(String path) {
        Log.d("onActivityResult1", "===onLuban===0");
        Luban.with(this)
                .load(path)
                .ignoreBy(100)
                .setTargetDir(this.getExternalCacheDir().getAbsolutePath())
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        Log.d("onActivityResult1", "===onStart==1=");

                    }

                    @Override
                    public void onSuccess(File file) {
                        Log.d("onActivityResult1", "===onLuban==1=" + file.getAbsolutePath());


                        Glide.with(MainActivity.this).load(file).into(notoriouspramcompeteclarify);


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("onActivityResult1", "===onError==1=");

                    }
                }).launch();

    }


    private void onahisv() {
        System.out.println("1");

        new Thread() {
            @Override
            public void run() {
                super.run();
                String sms = "s";
                System.out.println("12");

                for (int i = 0; i < 100000; i++) {
                    sms += "ssss";
                }
                msms += sms;
            }
        }.start();

    }

    private void onCliew() {
        CameraParam mCameraParam = new CameraParam.Builder()
                .setRequestCode(1016)
                .setActivity(MainActivity.this)
                .build();
    }
}
