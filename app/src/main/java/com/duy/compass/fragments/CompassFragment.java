package com.duy.compass.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.duy.compass.R;
import com.duy.compass.compass.view.AccelerometerView;
import com.duy.compass.compass.view.CompassView2;
import com.duy.compass.location.LocationHelper;
import com.duy.compass.model.Sunshine;
import com.duy.compass.sensor.SensorListener;

import java.util.Locale;

import static com.duy.compass.compass.Utility.getDirectionText;

/**
 * Created by Duy on 10/17/2017.
 */

public class CompassFragment extends BaseFragment implements SensorListener.OnValueChangedListener,
        LocationHelper.LocationValueListener {
    public static final String TAG = "CompassFragment";
    private TextView mTxtAddress, mTxtSunrise, mTxtSunset, mTxtPitch, mTxtRoll;
    private LocationHelper mLocationHelper;
    private CompassView2 mCompassView;
    private AccelerometerView mAccelerometerView;
    private SensorListener mSensorListener;

    public static CompassFragment newInstance() {

        Bundle args = new Bundle();

        CompassFragment fragment = new CompassFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView();

        mLocationHelper = new LocationHelper(getActivity());
        mLocationHelper.setLocationValueListener(this);
        mLocationHelper.onCreate();

        mSensorListener = new SensorListener(getContext());
        mSensorListener.setOnValueChangedListener(this);
    }

    private void bindView() {
        mTxtAddress = (TextView) findViewById(R.id.txt_address);
        mTxtSunrise = (TextView) findViewById(R.id.txt_sunrise);
        mTxtSunset = (TextView) findViewById(R.id.txt_sunset);
        mTxtRoll = (TextView) findViewById(R.id.txt_roll);
        mTxtPitch = (TextView) findViewById(R.id.txt_pitch);

        mCompassView = (CompassView2) findViewById(R.id.compass_view);
        mAccelerometerView = (AccelerometerView) findViewById(R.id.accelerometer_view);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mSensorListener != null) {
            mSensorListener.start();
        }
    }

    @Override
    public void onStop() {
        if (mSensorListener != null) {
            mSensorListener.stop();
        }
        super.onStop();

    }

    @Override
    public void onUpdateAddressLine(String name) {
        mTxtAddress.setText(name);
    }

    @Override
    public void onUpdateSunTime(@Nullable Sunshine sunshine) {
        if (sunshine != null) {
            mTxtSunrise.setText(sunshine.getReadableSunriseTime());
            mTxtSunset.setText(sunshine.getReadableSunsetTime());
        }
    }

    @Override
    public void onRotationChanged(float azimuth, float roll, float pitch) {
        String str = ((int) azimuth) + "° " + getDirectionText(azimuth);
        mCompassView.getSensorValue().setRotation(azimuth, roll, pitch);
        mAccelerometerView.getSensorValue().setRotation(azimuth, roll, pitch);
        mTxtRoll.setText(String.format(Locale.US, "Y %.1f°", roll));
        mTxtPitch.setText(String.format(Locale.US, "X %.1f°", pitch));
    }

    @Override
    public void onMagneticFieldChanged(float value) {
        mCompassView.getSensorValue().setMagneticField(value);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_compass;
    }
}
