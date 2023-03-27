package com.eastbay.camarsx2022.easyBayCamerx;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class CameraMiniParEastbayam implements Parcelable {


    public static final Creator<CameraMiniParEastbayam> CREATOR = new Creator<CameraMiniParEastbayam>() {
        @Override
        public CameraMiniParEastbayam createFromParcel(Parcel in) {

            return new CameraMiniParEastbayam(in);
        }

        @Override
        public CameraMiniParEastbayam[] newArray(int size) {

            return new CameraMiniParEastbayam[size];
        }
    };
    private int requestCode;
    private Activity mActivity;
    private boolean front;


    private CameraMiniParEastbayam(B9uildEastbayer mBuilder) {
        front = mBuilder.front;
        mActivity = mBuilder.mActivity;
        requestCode = mBuilder.requestCode;
        if (mActivity == null) {
            throw new NullPointerException("Activity param is null");
        }
    }

    protected CameraMiniParEastbayam(Parcel in) {
        front = in.readByte() != 0;
        requestCode = in.readInt();
    }


    public boolean isFront() {

        return front;
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

    private CameraMiniParEastbayam startActivity(int requestCode) {

        Intent intent = new Intent(mActivity, CamerxEas11tBayActivEastbayity.class);
        intent.putExtra(Mini1CEastbayamer3aConstant.CAMERA_PARAM_KEY, this);
        mActivity.startActivityForResult(intent, requestCode);

        return this;
    }


    public static class B9uildEastbayer {
        private boolean front = false;

        private Activity mActivity;//
        private int requestCode = Mini1CEastbayamer3aConstant.REQUEST_CODE;


        public B9uildEastbayer setFront(boolean front) {

            this.front = front;

            return this;
        }


        public B9uildEastbayer setActivity(Activity mActivity) {

            this.mActivity = mActivity;
            return this;
        }


        public B9uildEastbayer setRequestCode(int requestCode) {


            this.requestCode = requestCode;
            return this;
        }

        public CameraMiniParEastbayam build() {


            return new CameraMiniParEastbayam(this).startActivity(requestCode);
        }
    }

}
