package com.axeleroy.overworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SongDatabaseHelper extends SQLiteOpenHelper {

    private static final String CreateFavoriteTable = "CREATE TABLE Songs (Id integer PRIMARY KEY AUTOINCREMENT, SongUri text NOT NULL, TagTime text, TagWeather text, TagLocation text);";
    private Context mContext;
    private static final String DatabaseName = "Songs.db";
    private static SongDatabaseHelper Instance;
    private List<OnDatabaseChangeListener> Listeners;

    public SongDatabaseHelper(Context context){
        super(context, DatabaseName, null, 1);
        mContext = context;
        Listeners = new ArrayList<>();
    }

    public static void Initialize(Context context) {
        Instance = new SongDatabaseHelper(context);
    }

    public static SongDatabaseHelper GetInstance(){
        return Instance;
    }


    public void Subscribe(OnDatabaseChangeListener listener) {
        Listeners.add(listener);
    }


    public Song getCorrectSong(HashMap<String, String> requests){
        HashMap<Song, Integer> fitSongs = new HashMap<Song, Integer>() {
        };

        if(requests.get("tag_weather")!= null) {
            String tag_weather = requests.get("tag_weather");
            SQLiteDatabase db = Instance.getReadableDatabase();
            String Query = "Select * from Songs WHERE TagWeather = ?";
            Cursor cursor = db.rawQuery(Query, new String[]{tag_weather});
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String SongUri = cursor.getString(cursor.getColumnIndex("SongUri"));
                    String tagWeather = cursor.getString(cursor.getColumnIndex("TagWeather"));
                    String tagTime = cursor.getString(cursor.getColumnIndex("TagTime"));
                    String tagLocation = cursor.getString(cursor.getColumnIndex("TagLocation"));
                    Song song = new Song(Uri.parse(SongUri), tagWeather, tagTime, tagLocation);
                    if (!fitSongs.containsKey(song)) {
                       fitSongs.put(song,1);
                    }else{
                        int count = fitSongs.get(song);
                        fitSongs.put(song,count++);
                    }
                    cursor.moveToNext();
                }
            }
        }

        if(requests.get("tag_time")!= null) {
            String tag_time = requests.get("tag_time");
            SQLiteDatabase db = Instance.getReadableDatabase();
            String Query = "Select * from Songs WHERE TagTime = ?";
            Cursor cursor = db.rawQuery(Query, new String[]{tag_time});
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String SongUri = cursor.getString(cursor.getColumnIndex("SongUri"));
                    String tagWeather = cursor.getString(cursor.getColumnIndex("TagWeather"));
                    String tagTime = cursor.getString(cursor.getColumnIndex("TagTime"));
                    String tagLocation = cursor.getString(cursor.getColumnIndex("TagLocation"));
                    Song song = new Song(Uri.parse(SongUri), tagWeather, tagTime, tagLocation);
                    if (!fitSongs.containsKey(song)) {
                        fitSongs.put(song,1);
                    }else{
                        int count = fitSongs.get(song);
                        fitSongs.put(song,count++);
                    }
                    cursor.moveToNext();
                }
            }
        }

        if(requests.get("tag_location")!= null) {
            String tag_location = requests.get("tag_location");
            SQLiteDatabase db = Instance.getReadableDatabase();
            String Query = "Select * from Songs WHERE TagTime = ?";
            Cursor cursor = db.rawQuery(Query, new String[]{tag_location});
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String SongUri = cursor.getString(cursor.getColumnIndex("SongUri"));
                    String tagWeather = cursor.getString(cursor.getColumnIndex("TagWeather"));
                    String tagTime = cursor.getString(cursor.getColumnIndex("TagTime"));
                    String tagLocation = cursor.getString(cursor.getColumnIndex("TagLocation"));
                    Song song = new Song(Uri.parse(SongUri), tagWeather, tagTime, tagLocation);
                    if (!fitSongs.containsKey(song)) {
                        fitSongs.put(song,1);
                    }else{
                        int count = fitSongs.get(song);
                        fitSongs.put(song,count++);
                    }
                    cursor.moveToNext();
                }
            }
        }

        return sortByValue(fitSongs).getKey();
    }

    private static <K, V> Map.Entry<K,V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Object>() {
            @SuppressWarnings("unchecked")
            public int compare(Object o1, Object o2) {
                return ((Comparable<V>) ((Map.Entry<K, V>) (o1)).getValue()).compareTo(((Map.Entry<K, V>) (o2)).getValue());
            }
        });

//        Map<K, V> result = new LinkedHashMap<>();
//        for (Iterator<Map.Entry<K, V>> it = list.iterator(); it.hasNext();) {
//            Map.Entry<K, V> entry = (Map.Entry<K, V>) it.next();
//            result.put(entry.getKey(), entry.getValue());
//        }

        return list.get(0);
    }



    public void insertSong(Song song){
        ContentValues values = new ContentValues();
        values.put("SongUri", song.getUri().toString());
        values.put("TagTime", song.getTag_time());
        values.put("TagWeather", song.getTag_weather());
        values.put("TagLocation",song.getTag_location());
        Instance.getWritableDatabase().insert("FavFood", null, values);
    }


    public ArrayList<Song> getAllSongs(){
        ArrayList<Song> results = new ArrayList<>();
        SQLiteDatabase db = Instance.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Songs", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String SongUri = cursor.getString(cursor.getColumnIndex("SongUri"));
                String tagWeather = cursor.getString(cursor.getColumnIndex("TagWeather"));
                String tagTime = cursor.getString(cursor.getColumnIndex("TagTime"));
                String tagLocation = cursor.getString(cursor.getColumnIndex("TagLocation"));
                results.add(new Song(Uri.parse(SongUri), tagWeather, tagTime, tagLocation));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return results;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateFavoriteTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public interface OnDatabaseChangeListener {
        void OnDatabaseChange();
    }
}
