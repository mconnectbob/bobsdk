package com.bob.bobapp.Home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bob.bobapp.R;
import com.bob.bobapp.activities.BOBActivity;
import com.bob.bobapp.fragments.ProfileFragment;
import com.bob.bobapp.utility.Util;

public class ProfileContainerFragment extends BaseContainerFragment {

    private boolean mIsViewInited;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.container_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        new Util(getActivity()).highlightTabText();

        if (!mIsViewInited) {

            mIsViewInited = true;

            initView();
        }
    }

    private void initView() {

        replaceFragment(new ProfileFragment(), false);
    }
}