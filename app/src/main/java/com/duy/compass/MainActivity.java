package com.duy.compass;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.duy.compass.fragments.CompassFragment;
import com.duy.compass.view.CustomViewPager;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.view_pager);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
//                    case 0:
//                        return new WeatherFragment();
                    case 0:
                        return new CompassFragment();
//                    case 2:
//                        return new CompassMapFragment();
//                    case 3:
//                        return new SettingFragment();
                }
                return null;
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }
}
