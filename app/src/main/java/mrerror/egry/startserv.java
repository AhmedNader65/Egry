package mrerror.egry;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Ahmed on 3/29/2016.
 */
public class startserv {
    private StepService mService;
    private  Activity mActivity;
   // home h = new home();
    private TextView mDistanceValueView;
    //private TextView mCaloriesValueView;
    private static float mDistanceValue;
    private float mSpeedValue;
    private static int    mCaloriesValue;
    private int mStepValue;

    private TextView mSpeedValueView;
    TinyDB tiny ;
    public startserv(Activity activity , View v ){
        this.mActivity = activity;
        mDistanceValueView = (TextView) v.findViewById(R.id.dist_val);
//        mCaloriesValueView = (TextView) v.findViewById(R.id.calories_value);
        mSpeedValueView = (TextView)v.findViewById(R.id.speed);
        mSpeedValueView.setText("00");
        tiny = new TinyDB(activity.getApplicationContext());
    }

    public void startStepService() {
        Log.e("started", "started");
        mActivity.startService(new Intent(mActivity,
                StepService.class));

        Log.e("after ",String.valueOf(mDistanceValue));

    }
    public void bindStepService() {
        mActivity.bindService(new Intent(mActivity,
                StepService.class), mConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    public void unbindStepService() {

        mActivity.unbindService(mConnection);
    }

    public void stopStepService() {
        if (mService != null) {
            mActivity.stopService(new Intent(mActivity,
                    StepService.class));
        }
    }



    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = ((StepService.StepBinder)service).getService();

            mService.registerCallback(mCallback);
            mService.reloadSettings();
            if(tiny.getBoolean("finished")){
                mService.resetValues();
               tiny.putBoolean("finished",false);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };
    private StepService.ICallback mCallback = new StepService.ICallback() {
        public void stepsChanged(int value) {
            mHandler.sendMessage(mHandler.obtainMessage(STEPS_MSG, value, 0));
        }
        public void paceChanged(int value) {
            mHandler.sendMessage(mHandler.obtainMessage(PACE_MSG, value, 0));
        }
        public void distanceChanged(float value) {
            mHandler.sendMessage(mHandler.obtainMessage(DISTANCE_MSG, (int)(value*1000), 0));
        }
        public void speedChanged(float value) {
            mHandler.sendMessage(mHandler.obtainMessage(SPEED_MSG, (int) (value * 1000), 0));
        }
        public void caloriesChanged(float value) {
            mHandler.sendMessage(mHandler.obtainMessage(CALORIES_MSG, (int)(value), 0));
        }
    };


    private static final int STEPS_MSG = 1;
    private static final int PACE_MSG = 2;
    private static final int DISTANCE_MSG = 3;
    private static final int SPEED_MSG = 4;
    private static final int CALORIES_MSG = 5;
    private Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                case DISTANCE_MSG:
                    mDistanceValue = ((int)msg.arg1)/1000f;
                    if (mDistanceValue <= 0) {
                        mDistanceValueView.setText("0.00");
                    }
                    else {
                        mDistanceValueView.setText(
                                ("" + (mDistanceValue + 0.000001f)).substring(0, 5)
                        );
                    }
                    break;

                case STEPS_MSG:
                    mStepValue = (int)msg.arg1;
                    tiny.putInt("steps1", (int)mStepValue);
                    break;

                case SPEED_MSG:
                    mSpeedValue = ((int)msg.arg1)/1000f;
                    if (mSpeedValue <= 0) {
                        mSpeedValueView.setText("0");
                    }
                    else {
                        mSpeedValueView.setText(
                                ("" + (mSpeedValue + 0.000001f)).substring(0, 4)
                        );
                    }
                    break;


                case CALORIES_MSG:
                    mCaloriesValue = msg.arg1;

                    if (mCaloriesValue <= 0) {
                        tiny.putInt("cal1", 0);
                    }
                    else {
                        tiny.putInt("cal1", (int)mCaloriesValue);
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

    };

}
