package com.eastbay.camarsx2022;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import androidx.camera.core.ImageProxy;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class CameraUtils {

    public static Bitmap toBitmap(ImageProxy imageProxy, Context context, boolean isFrontCamera) {
        ByteBuffer yBuffer = imageProxy.getPlanes()[0].getBuffer();
        ByteBuffer uBuffer = imageProxy.getPlanes()[1].getBuffer();
        ByteBuffer vBuffer = imageProxy.getPlanes()[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, imageProxy.getWidth(), imageProxy.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 100, out);
        byte[] imageBytes = out.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        int rotationDegrees = getRotationDegrees(context, imageProxy, isFrontCamera);
        Matrix matrix = new Matrix();

        if (rotationDegrees != 0) {
            matrix.postRotate(rotationDegrees);
        }

        if (isFrontCamera) {
            matrix.postScale(-1f, 1f, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);
        }

        if (!matrix.isIdentity()) {
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        return bitmap;
    }

    private static int getRotationDegrees(Context context, ImageProxy imageProxy, boolean isFrontCamera) {
        try {
            CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            String cameraId = isFrontCamera ? getCameraId(cameraManager, CameraCharacteristics.LENS_FACING_FRONT) : getCameraId(cameraManager, CameraCharacteristics.LENS_FACING_BACK);
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            Integer sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            if (sensorOrientation == null) return 0;

            // 获取屏幕旋转角度（兼容 Android 6.0）
            int displayRotation = getDisplayRotation(context);
            int rotationCompensation;

            if (isFrontCamera) {
                switch (displayRotation) {
                    case Surface.ROTATION_0:
                        rotationCompensation = sensorOrientation;
                        break;
                    case Surface.ROTATION_90:
                        rotationCompensation = (sensorOrientation + 270) % 360;
                        break;
                    case Surface.ROTATION_180:
                        rotationCompensation = (sensorOrientation + 180) % 360;
                        break;
                    case Surface.ROTATION_270:
                        rotationCompensation = (sensorOrientation + 90) % 360;
                        break;
                    default:
                        rotationCompensation = sensorOrientation;
                }
            } else {
                switch (displayRotation) {
                    case Surface.ROTATION_0:
                        rotationCompensation = sensorOrientation;
                        break;
                    case Surface.ROTATION_90:
                        rotationCompensation = (sensorOrientation + 270) % 360;
                        break;
                    case Surface.ROTATION_180:
                        rotationCompensation = (sensorOrientation + 180) % 360;
                        break;
                    case Surface.ROTATION_270:
                        rotationCompensation = (sensorOrientation + 90) % 360;
                        break;
                    default:
                        rotationCompensation = sensorOrientation;
                }
            }

            return rotationCompensation;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 兼容方法：获取屏幕旋转角度
    private static int getDisplayRotation(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        return display.getRotation();
    }

    private static String getCameraId(CameraManager cameraManager, int facing) throws Exception {
        for (String cameraId : cameraManager.getCameraIdList()) {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            Integer lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING);
            if (lensFacing != null && lensFacing == facing) {
                return cameraId;
            }
        }
        throw new Exception("未找到指定方向的摄像头");
    }
}