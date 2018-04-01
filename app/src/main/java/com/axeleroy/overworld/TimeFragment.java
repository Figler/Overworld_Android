package com.axeleroy.overworld;


import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.greenrobot.eventbus.EventBus;

public class TimeFragment extends DialogFragment {

    private ImageButton daytime;
    private ImageButton nighttime;


    public TimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_time, container, false);
        daytime = (ImageButton)fragView.findViewById(R.id.daybutton);
        nighttime = (ImageButton) fragView.findViewById(R.id.nightbutton);
        return fragView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        daytime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "daytime";
                EventBus.getDefault().post(new TimeEvent(result));
            }
        });

        nighttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "nighttime";
                EventBus.getDefault().post(new TimeEvent(result));

            }
        });
    }
}
