package com.eastbay.camarsx2022.easyBayCamerx;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.eastbay.camarsx2022.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.FocusMeteringResult;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.core.ViewPort;
import androidx.camera.core.ZoomState;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;


public class CamerxEastBayActivity extends AppCompatActivity {
    private ImageCapture imageCapture;
    private CameraControl mCameraControl;
    private CameraInfo mCameraInfo;
    private boolean isInfer = true;
    private int flashMode = ImageCapture.FLASH_MODE_OFF;
    private CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
    private ExecutorService cameraExecutor;

    private final int REQUEST_CODE_PERMISSIONS = 10;
    private String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private PreviewView viewFinder;
    private ImageButton camera_switch_button;
    private ImageButton camera_capture_button;
    private View box_prediction;

    private Context context;
    private File outputDirectory;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;


        try {

            Window window = this.getWindow();
            window.setFlags(flag, flag);
            setContentView(R.layout.activity_camerx_eastbay);
            initView();
            outputDirectory = getOutputDirectory();
            context = this;
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        camera_capture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    takePhoto();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        cameraExecutor = Executors.newSingleThreadExecutor();
        camera_switch_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    if (CameraSelector.DEFAULT_FRONT_CAMERA == cameraSelector) {
                        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                    } else {
                        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
                    }
                    startCamera();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }


    public File getOutputDirectory() {


        File file = new File(getExternalCacheDir(), getString(R.string.app_name));
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    private void takePhoto() {

        ImageCapture tImageCapture = imageCapture;
        if (tImageCapture == null) {
            return;
        }
        File photoFile = new File(outputDirectory, "eastbay" + System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {

            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {

                Uri savedUri = Uri.fromFile(photoFile);
                Intent intent = new Intent();
                intent.putExtra(Mini1CameraConstant.PICTURE_PATH_KEY, photoFile.getAbsolutePath());
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {

            }
        });

    }

    public boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS
        ) {

            if (!(ContextCompat.checkSelfPermission(getBaseContext(), permission) == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "没有授权，无法使用！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {


                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    int rotation = getWindowManager().getDefaultDisplay().getRotation();
                    Preview preview = new Preview.Builder().build();
                    preview.setSurfaceProvider(viewFinder.getSurfaceProvider());


                    //设置相机支持拍照
                    imageCapture = new ImageCapture
                            .Builder()
                            .setFlashMode(flashMode)
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY).build();


                    OrientationEventListener orientationEventListener = new OrientationEventListener(context) {
                        @Override
                        public void onOrientationChanged(int orientation) {


                            try {
                                int rotation;

                                // Monitors orientation values to determine the target rotation value
                                if (orientation >= 45 && orientation < 135) {
                                    rotation = Surface.ROTATION_270;
                                } else if (orientation >= 135 && orientation < 225) {
                                    rotation = Surface.ROTATION_180;
                                } else if (orientation >= 225 && orientation < 315) {
                                    rotation = Surface.ROTATION_90;
                                } else {
                                    rotation = Surface.ROTATION_0;
                                }

                                imageCapture.setTargetRotation(rotation);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    orientationEventListener.enable();


                    //设置相机支持图像分析
                    ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3)
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build();

                    //实时获取图像进行分析
                    imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), new ImageAnalysis.Analyzer() {
                        @Override
                        public void analyze(@NonNull ImageProxy image) {
                            if (isInfer) {

                            }
                        }
                    });


                    try {
                        cameraProvider.unbindAll();

//                        ViewPort viewPort = new ViewPort.Builder(
//                                new Rational(108, 55),
//                                rotation).setScaleType(ViewPort.FILL_END).build();

                        ViewPort viewPort = viewFinder.getViewPort();
//                        Rational aspectRatio = viewPort1.getAspectRatio();
//                        Log.d(TAG, aspectRatio.getNumerator() + "====1==" + aspectRatio.getDenominator());

                        UseCaseGroup useCaseGroup = new UseCaseGroup.Builder()
                                .addUseCase(preview)
                                .addUseCase(imageAnalysis)
                                .addUseCase(imageCapture)
                                .setViewPort(viewPort)
                                .build();


                        Camera camera = cameraProvider.bindToLifecycle(CamerxEastBayActivity.this, cameraSelector, useCaseGroup);


//                        Camera camera = cameraProvider.bindToLifecycle(
//                                CameraX1Activity.this,
//                                cameraSelector,
//                                preview,
//                                imageCapture,
//                                imageAnalysis
//                        );
                        // 相机控制，如点击
                        mCameraControl = camera.getCameraControl();
                        mCameraInfo = camera.getCameraInfo();

                        initCameraListener();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }, ContextCompat.getMainExecutor(this));

    }

    private void initCameraListener() {

        LiveData<ZoomState> zoomState = mCameraInfo.getZoomState();
        CameraXPrevieMiniwViewTouchListener cameraXPreviewViewTouchListener = new CameraXPrevieMiniwViewTouchListener(this);
        cameraXPreviewViewTouchListener.setmCustomTouchListener(new CameraXPrevieMiniwViewTouchListener.CustomTouchListener() {
            @Override
            public void zoom(Float delta) {


                try {
                    ZoomState value = zoomState.getValue();
                    float currentZoomRatio = value.getZoomRatio();
                    mCameraControl.setZoomRatio(currentZoomRatio * delta);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void click(Float x, Float y) {
                try {
                    MeteringPointFactory factory = viewFinder.getMeteringPointFactory();
                    MeteringPoint point = factory.createPoint(x, y);
                    FocusMeteringAction action = new FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
                            .setAutoCancelDuration(3, TimeUnit.SECONDS).build();
                    ListenableFuture<FocusMeteringResult> future = mCameraControl.startFocusAndMetering(action);
                    future.addListener(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                try {
                                    final FocusMeteringResult result = future.get();
                                    if (result.isFocusSuccessful()) {
                                    } else {
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, ContextCompat.getMainExecutor(context));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void doubleClick(Float x, Float y) {
                try {
                    float currentZoomRatio = zoomState.getValue().getZoomRatio();
                    if (currentZoomRatio > zoomState.getValue().getMinZoomRatio()) {
                        mCameraControl.setLinearZoom(0f);
                    } else {
                        mCameraControl.setLinearZoom(0.5f);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void longPress(Float x, Float y) {

            }
        });
        viewFinder.setOnTouchListener(cameraXPreviewViewTouchListener);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    private void initView() {

        viewFinder = (PreviewView) findViewById(R.id.gamblerroommatereportersolemnly);
        camera_switch_button = (ImageButton) findViewById(R.id.thrashpremaritaliteapot);
        camera_capture_button = (ImageButton) findViewById(R.id.paydayshootwarrantedstare);
        box_prediction = (View) findViewById(R.id.sandrainforestwardrobepeddler);

    }


}
