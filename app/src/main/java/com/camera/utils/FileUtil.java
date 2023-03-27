package com.camera.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 此类用于:文件工具类
 *
 * @author mrliu
 * @date 2022/6/15
 */
public class FileUtil {
    public static File createTmpFile(Context context) {
        if (true) {
            // 已挂载
            File pic = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (!pic.exists()) {
                try {
                    pic.mkdirs();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "credit_rocket_" + timeStamp + "";
            File tmpFile = new File(pic, fileName + ".png");
            return tmpFile;
        } else {
            File cacheDir = context.getCacheDir();
            if (!cacheDir.exists()) {
                try {
                    cacheDir.mkdirs();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "credit_rocket_" + timeStamp + "";
            File tmpFile = new File(cacheDir, fileName + ".png");
            return tmpFile;
        }
    }

    /**
     * 根据操作系统版本获取URI
     */
    public static Uri getFileUri(final Context context, final File file) {
        //判断是否是AndroidN以及更高的版本
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName(), file);
        } else {
            uri = Uri.fromFile(file);
        }

        return uri;
    }

    public static String getExternalAppCachePath(Context context) {
//        if (!AppUtil.hasSDCard()) {
//            return "";
//        }
        return getAbsolutePath(context.getExternalCacheDir());
    }

    private static String getAbsolutePath(final File file) {
        if (file == null) {
            return "";
        }
        return file.getAbsolutePath();
    }

    /**
     * Compress by quality,  and generate image to the path specified
     *
     * @param imgPath
     * @param outPath
     * @param maxSize target will be compressed to be smaller than this size.(kb)
     */
    public static void compressAndGenImage(String imgPath, String outPath, int maxSize) {
        try {
            compressAndGenImage(ratio(imgPath, 4096, 4096), outPath, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap ratio(String imgPath, float pixelW, float pixelH) {
        Bitmap bitmap = getBitmap(imgPath);
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) pixelW) / width;
        float scaleHeight = ((float) pixelH) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        return newBitmap;
    }

    public static Bitmap getBitmap(String imgPath) {
        int degree = readPictureDegree(imgPath);

        // Get bitmap through image path
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        // Do not compress
        newOpts.inSampleSize = 1;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap retBitmap = BitmapFactory.decodeFile(imgPath, newOpts);

        if (degree != 0) {
            retBitmap = rotaingBitmap(degree, retBitmap);
        }

        return retBitmap;
    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle  被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotaingBitmap(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    /**
     * Compress by quality,  and generate image to the path specified
     *
     * @param image
     * @param outPath
     * @param maxSize target will be compressed to be smaller than this size.(kb)
     * @throws IOException
     */
    public static void compressAndGenImage(Bitmap image, String outPath, int maxSize) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 100;
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        // Compress by loop
        while (os.toByteArray().length / 1024 > maxSize) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;

            if (options < 0) {
                break;
            }
            image.compress(Bitmap.CompressFormat.JPEG, options, os);
        }

        // Generate compressed image file
        File outFile = new File(outPath);
        if (!outFile.exists()) {
            outFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(outFile);
        fos.write(os.toByteArray());
        fos.flush();
        fos.close();
    }

    public static long Length(File file) {//计算文件夹大小方法
        long length = 0;
        if (file.isFile()) {
            length += file.length();
        } else {
            length += Length(file);//如果是文件夹,则递归调用Length方法
        }
        return length;
    }

}