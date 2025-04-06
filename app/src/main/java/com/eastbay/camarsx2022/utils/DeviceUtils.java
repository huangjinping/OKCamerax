package com.eastbay.camarsx2022.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingQueue;

public class DeviceUtils {


    public static JSONObject getHardWareInfo(Context context) {


        JSONObject hardWareData = new JSONObject();
        try {
            hardWareData.put("device_name", DataUtil.filterText(getDriverBrand()));
            hardWareData.put("brand", DataUtil.filterText(getDriverBrand()));
            hardWareData.put("board", DataUtil.filterText(getBoard()));
            hardWareData.put("sdk_version", DataUtil.filterText(getDriverSDKVersion()));
            hardWareData.put("model", DataUtil.filterText(getDriverModel()));
            hardWareData.put("release", DataUtil.filterText(getDriverOsVersion()));
            hardWareData.put("serial_number", DataUtil.filterText(getSerialNumber()));
            hardWareData.put("physical_size", DataUtil.filterText(getScreenPhysicalSize(context)));
            hardWareData.put("production_date", DataUtil.filterText(getDriverTime()));
            hardWareData.put("device_height", DataUtil.filterText(getDisplayMetrics(context).heightPixels + ""));
            hardWareData.put("device_width", DataUtil.filterText(getDisplayMetrics(context).widthPixels + ""));
            hardWareData.put("cpu_num", getCpuNum() + "");
            hardWareData.put("imei1", DataUtil.filterText(getIMEI1(context)));
            hardWareData.put("imei2", TextUtils.isEmpty(DataUtil.filterText(getIMEI1(context))) ? DataUtil.filterText(getIMEI2(context)) : DataUtil.filterText(getIMEI1(context)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hardWareData;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }


    public static int getCpuNum() {
        try {
            return Runtime.getRuntime().availableProcessors();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getDriverTime() {
        try {
            long l = Build.TIME;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(l);
            stringBuilder.append("");
            return stringBuilder.toString();
        } catch (Exception var3) {
            return "";
        }
    }

    private static String getScreenPhysicalSize(Context paramContext) {
        try {
            Display display = ((WindowManager) paramContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);
            return Double.toString(Math.sqrt(Math.pow((double) ((float) displayMetrics.heightPixels / displayMetrics.ydpi), 2.0D) + Math.pow((double) ((float) displayMetrics.widthPixels / displayMetrics.xdpi), 2.0D)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public static String getBoard() {
        return Build.BOARD;
    }


    private static String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        String strTz = tz.getDisplayName(false, 0);
        return strTz;
    }

    private static String getDriverBrand() {
        try {
            return Build.BRAND;
        } catch (Exception var1) {
            return "";
        }
    }

    private static String getDriverSDKVersion() {
        try {
            return Build.VERSION.SDK_INT + "";
        } catch (Exception var1) {
            return "";
        }
    }

    private static String getDriverModel() {
        try {
            return Build.MODEL;
        } catch (Exception var1) {
            return "";
        }
    }

    private static String getDriverOsVersion() {
        try {
            return Build.VERSION.RELEASE;
        } catch (Exception var1) {
            return "";
        }
    }

    private static String getSerialNumber1() {
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");

            return (String) clazz.getMethod("get", String.class).invoke(clazz, "ro.serialno");
        } catch (Exception var1) {
            return "";
        }
    }

    private static String getSerialNumber() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialnocustom");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (serial == null) {
            serial = getSerialNumber1();
        }
        return serial;
    }


    /**
     * <p>手机操作系统版本</p>
     *
     * @return
     * @author
     * @date 2013-1-4
     */
    public static String getSoftSDKVersion() {
        return Build.VERSION.RELEASE;//Firmware/OS 版本号
    }


    public static String getScreenDisplay(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return width + "*" + height;
    }


    public static String getIMEI1(Context context) {
        String imei1 = "";
        try {
            imei1 = getImeiOrMeid(context, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(imei1)) {
            imei1 = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        if (!TextUtils.isEmpty(imei1)) {
            return imei1;
        }

        return "";
    }


    public static String getIMEI2(Context context) {

        try {
            //imei2必须与 imei1不一样
            String imeiDefault = getIMEI1(context);
            if (TextUtils.isEmpty(imeiDefault)) {
                //默认的 imei 竟然为空，说明权限还没拿到，或者是平板
                //这种情况下，返回 imei2也应该是空串
                return "";
            }

            //注意，拿第一个 IMEI 是传0，第2个 IMEI 是传1，别搞错了
            String imei1 = getImeiOrMeid(context, 0);
            String imei2 = getImeiOrMeid(context, 1);
            //sim 卡换卡位时，imei1与 imei2有可能互换，而 imeidefault 有可能不变
            if (!TextUtils.equals(imei2, imeiDefault)) {
                //返回与 imeiDefault 不一样的
                return imei2;
            }
            if (!TextUtils.equals(imei1, imeiDefault)) {
                return imei1;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return "";
    }


    public static String getImeiOrMeid(Context context, int slotId) {
        String imei = "";

        //Android 6.0 以后需要获取动态权限  检查权限
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return imei;
        }
        Log.d("okhttps2", "=========3=========");

        try {
            TelephonyManager manager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (manager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {// android 8 即以后建议用getImei 方法获取 不会获取到MEID
                    Method method = manager.getClass().getMethod("getImei", int.class);
                    imei = (String) method.invoke(manager, slotId);
                    Log.d("okhttps2", "=========0=========" + imei);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //5.0的系统如果想获取MEID/IMEI1/IMEI2  ----framework层提供了两个属性值“ril.cdma.meid"和“ril.gsm.imei"获取
                    imei = getSystemPropertyByReflect("ril.gsm.imei");
                    //如果获取不到 就调用 getDeviceId 方法获取
                    Log.d("okhttps2", "=========1=========" + imei);

                } else {//5.0以下获取imei/meid只能通过 getDeviceId  方法去取

                }
            }
        } catch (Exception e) {
        }

        if (TextUtils.isEmpty(imei)) {
            imei = getDeviceId(context, slotId);
            Log.d("okhttps2", "=========4=========" + imei);

        }
        return imei;
    }


    private static String getSystemPropertyByReflect(String key) {
        try {
            @SuppressLint("PrivateApi") Class<?> clz = Class.forName("android.os.SystemProperties");
            Method getMethod = clz.getMethod("get", String.class, String.class);
            return (String) getMethod.invoke(clz, key, "");
        } catch (Exception e) {/**/}
        return "";
    }


    public static String getDeviceId(Context context, int slotId) {
        String imei = "";
        imei = getDeviceIdFromSystemApi(context, slotId);
        Log.d("okhttps2", "=========5=========" + imei);
        if (TextUtils.isEmpty(imei)) {
            imei = getDeviceIdByReflect(context, slotId);
            Log.d("okhttps2", "=========6=========" + imei);
        }
        return imei;
    }


    public static String getDeviceIdFromSystemApi(Context context, int slotId) {
        String imei = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                imei = telephonyManager.getDeviceId(slotId);
            }
        } catch (Throwable e) {
        }
        return imei;
    }


    /**
     * 反射获取 deviceId
     *
     * @param context
     * @param slotId  slotId为卡槽Id，它的值为 0、1；
     * @return
     */
    public static String getDeviceIdByReflect(Context context, int slotId) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            Method method = tm.getClass().getMethod("getDeviceId", int.class);
            return method.invoke(tm, slotId).toString();
        } catch (Throwable e) {
        }
        return "";
    }


    /**
     * 这个方法是耗时的，不能在主线程调用
     */
    public static String getGoogleAdId(Context context) throws Exception {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            return "Cannot call in the main thread, You must call in the other thread";
        }
        PackageManager pm = context.getPackageManager();
        pm.getPackageInfo("com.android.vending", 0);
        AdvertisingConnection connection = new AdvertisingConnection();
        Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
        intent.setPackage("com.google.android.gms");
        if (context.bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
            try {
                AdvertisingInterface adInterface = new AdvertisingInterface(connection.getBinder());
                return adInterface.getId();
            } finally {
                context.unbindService(connection);
            }
        }
        return "";
    }

    private static final class AdvertisingConnection implements ServiceConnection {
        private final LinkedBlockingQueue<IBinder> queue = new LinkedBlockingQueue<>(1);
        boolean retrieved = false;

        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                this.queue.put(service);
            } catch (InterruptedException localInterruptedException) {
            }
        }

        public void onServiceDisconnected(ComponentName name) {
        }

        public IBinder getBinder() throws InterruptedException {
            if (this.retrieved) throw new IllegalStateException();
            this.retrieved = true;
            return this.queue.take();
        }
    }

    private static final class AdvertisingInterface implements IInterface {
        private IBinder binder;

        public AdvertisingInterface(IBinder pBinder) {
            binder = pBinder;
        }

        public IBinder asBinder() {
            return binder;
        }

        public String getId() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            String id;
            try {
                data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                binder.transact(1, data, reply, 0);
                reply.readException();
                id = reply.readString();
            } finally {
                reply.recycle();
                data.recycle();
            }
            return id;
        }
    }


}
