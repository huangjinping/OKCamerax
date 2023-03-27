package com.eastbay.camarsx2022.easyBayCamerx;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class CameraMiniParam implements Parcelable {


    private boolean front;
    private Activity mActivity;
    private int requestCode;


    private CameraMiniParam(Builder mBuilder) {
        front = mBuilder.front;
        mActivity = mBuilder.mActivity;
        requestCode = mBuilder.requestCode;
        if (mActivity == null) {
            throw new NullPointerException("Activity param is null");
        }
    }

    protected CameraMiniParam(Parcel in) {
        front = in.readByte() != 0;
        requestCode = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeByte((byte) (front ? 1 : 0));
        dest.writeInt(requestCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<CameraMiniParam> CREATOR = new Creator<CameraMiniParam>() {
        @Override
        public CameraMiniParam createFromParcel(Parcel in) {

            return new CameraMiniParam(in);
        }

        @Override
        public CameraMiniParam[] newArray(int size) {

            return new CameraMiniParam[size];
        }
    };

    private CameraMiniParam startActivity(int requestCode) {

        Intent intent = new Intent(mActivity, CamerxEastBayActivity.class);
        intent.putExtra(Mini1CameraConstant.CAMERA_PARAM_KEY, this);
        mActivity.startActivityForResult(intent, requestCode);

        return this;
    }


    public boolean isFront() {

        return front;
    }


    public static class Builder {
        private boolean front = false;

        private Activity mActivity;//
        private int requestCode = Mini1CameraConstant.REQUEST_CODE;


        public Builder setFront(boolean front) {

            this.front = front;

            return this;
        }


        public Builder setActivity(Activity mActivity) {

            this.mActivity = mActivity;
            return this;
        }


        public Builder setRequestCode(int requestCode) {


            this.requestCode = requestCode;
            return this;
        }

        public CameraMiniParam build() {


            return new CameraMiniParam(this).startActivity(requestCode);
        }
    }

}
