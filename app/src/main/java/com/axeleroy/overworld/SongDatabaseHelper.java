package com.axeleroy.overworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

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

    public void touchedFavFood(String food) {
        if(checkIfExisted(food) == true){
            Instance.getWritableDatabase().delete("FavFood", " Food = ?", new String[] { food });
        }else {
            ContentValues values = new ContentValues();
            values.put("Food", food);
            Instance.getWritableDatabase().insert("FavFood", null, values);
        }
       // NotifyListeners();
    }

    public int checkMenuFav(String menu){
        int result = 0;
        SQLiteDatabase db = Instance.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from FavFood", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex("Food"));
                if(menu.contains(name)){
                    result++;
                }
                cursor.moveToNext();
            }
        }
        return result;
    }

    public boolean checkIfExisted(String food) {
        SQLiteDatabase db = Instance.getReadableDatabase();
        String Query = "Select * from FavFood WHERE Food = ?";
        Cursor cursor = db.rawQuery(Query, new String[] { food });
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
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
