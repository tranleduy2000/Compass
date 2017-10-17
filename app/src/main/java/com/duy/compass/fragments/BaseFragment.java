package com.duy.compass.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Duy on 10/17/2017.
 */

public abstract class BaseFragment extends Fragment {
    @Nullable
    private View mRoot;

    public abstract int getLayout();

    @Nullable
    public View findViewById(int id) {
        return mRoot != null ? mRoot.findViewById(id) : null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(getLayout(), container, false);
        return mRoot;
    }
}
