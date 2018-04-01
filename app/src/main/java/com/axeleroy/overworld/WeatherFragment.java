package com.axeleroy.overworld;


import android.os.Bundle;
import android.app.DialogFragment;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;

import org.greenrobot.eventbus.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends DialogFragment {

    private ImageButton rainy;
    private ImageButton sunny;
    private ImageButton windy;

    private int result;

    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        result = -1;
        View fragView = inflater.inflate(R.layout.fragment_weather, container, false);
        rainy = (ImageButton) fragView.findViewById(R.id.buttonRainy);
        sunny = (ImageButton) fragView.findViewById(R.id.buttonSunny);
        windy = (ImageButton) fragView.findViewById(R.id.buttonWindy);

        rainy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = 0;
            }
        });

        sunny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = 1;
            }
        });

        windy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = 2;
            }
        });

        setRetainInstance(true);
        return fragView;
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().post(new WeatherReturn(result));
    }
}
