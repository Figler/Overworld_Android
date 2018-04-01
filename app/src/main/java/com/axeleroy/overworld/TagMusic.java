package com.axeleroy.overworld;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.Time;

public class TagMusic extends Activity {
    private FloatingActionButton endFab;

    private TextView song;
    private TextView artist;

    private ImageButton weather;
    private ImageButton location;
    private ImageButton time;

    // Results from each of the fragments
    private int weatherResult;  // -1 = Nothing, 0 = Rainy, 1 = Sunny, 2 = Windy
    private String timeResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_music);

        // Grabbing links to all TextViews and Buttons
        song = (TextView)findViewById(R.id.song_title);
        artist = (TextView)findViewById(R.id.song_artist);
        weather = (ImageButton)findViewById(R.id.weather);
        location = (ImageButton)findViewById(R.id.location);
        time = (ImageButton)findViewById(R.id.time);
        endFab = (FloatingActionButton)findViewById(R.id.endActivity);

        // Metadata from URI
        if (bundle != null) {
            MediaMetadataRetriever mData = new MediaMetadataRetriever();
            mData.setDataSource(this, Uri.parse(bundle.getString("Track")));
            // Setting the textViews
            song.setText(mData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            artist.setText(mData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        }

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DialogFragment newFragment = new TimeFragment();
                newFragment.show(fm, "abc");
            }
        });

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DialogFragment newFragment = new WeatherFragment();
                newFragment.show(fm, "abc");
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a map activity using a new intent, then be able to return the Geofenced coordinates within a bundle
                // Bundle should contain something like ArrayList <Geofence Coordinates>
                // Activity should let you set geofences (ONE SHOULD BE FINE)
                Intent intent = new Intent(TagMusic.this, MapsActivity.class);
                startActivity(intent);
            }
        });

    }

    // Weather results
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(WeatherReturn event) {
        // process event here
        weatherResult = event.getData();
        System.out.println("weatherResult: " + weatherResult);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(TimeEvent event) {
        // process event here
        timeResult = event.getData();
        System.out.println("timeResult: " + timeResult);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


}
