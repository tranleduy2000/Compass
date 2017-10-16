package com.duy.compass;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.compass.compass.view.CompassView2;
import com.duy.compass.location.LocationHelper;
import com.duy.compass.model.Sunshine;

public class MainActivity extends AppCompatActivity implements LocationHelper.LocationValueListener {

    private TextView mTxtAddress, mTxtSunrise, mTxtSunset;

    private LocationHelper mLocationHelper;
    private CompassView2 mCompassView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createMainView();
        bindView();

        mLocationHelper = new LocationHelper(this);
        mLocationHelper.setLocationValueListener(this);
        mLocationHelper.onCreate();
    }

    private void bindView() {
        mTxtAddress = (TextView) findViewById(R.id.txt_address);
        mTxtSunrise = (TextView) findViewById(R.id.txt_sunrise);
        mTxtSunset = (TextView) findViewById(R.id.txt_sunset);
    }

    private void createMainView() {
        ViewGroup content = (ViewGroup) findViewById(R.id.content);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mCompassView = new CompassView2(this);
        content.addView(mCompassView, params);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onUpdateAddressLine(String name) {
        mTxtAddress.setText(name);
    }

    @Override
    public void onUpdateSunTime(Sunshine sunshine) {
        mTxtSunrise.setText(sunshine.getReadableSunriseTime());
        mTxtSunset.setText(sunshine.getReadableSunsetTime());
    }
}
