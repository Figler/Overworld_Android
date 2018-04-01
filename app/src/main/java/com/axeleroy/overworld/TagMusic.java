package com.axeleroy.overworld;

import android.app.Activity;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.Button;
import android.widget.TextView;

public class TagMusic extends Activity {
    private FloatingActionButton endFab;

    private TextView song;
    private TextView artist;

    private Button weather;
    private Button location;
    private Button time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Bundle bundle = getIntent().getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_music);

        // Grabbing links to all textviews and buttons
        song = (TextView)findViewById(R.id.song_title);
        artist = (TextView)findViewById(R.id.song_artist);
        weather = (Button)findViewById(R.id.weather);
        location = (Button)findViewById(R.id.location);
        time = (Button)findViewById(R.id.time);
        endFab = (FloatingActionButton)findViewById(R.id.endActivity);

        // Metadata from URI

        if (bundle != null) {
            MediaMetadataRetriever mData = new MediaMetadataRetriever();
            mData.setDataSource(this, Uri.parse(bundle.getString("Track")));

            // Setting the textViews
            song.setText(mData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            artist.setText(mData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        }


    }


}
