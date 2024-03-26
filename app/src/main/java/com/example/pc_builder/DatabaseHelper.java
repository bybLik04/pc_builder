package com.example.pc_builder;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pcbuilder.db";
    private static final int DATABASE_VERSION = 1;
    private final Context mContext;
    private static String DB_PATH;
    private SQLiteDatabase db;
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        DB_PATH = mContext.getApplicationInfo().dataDir + "/databases/";

        try {
            db = SQLiteDatabase.openDatabase(context.getDatabasePath("pcbuilder.db").getPath(), null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createDataBase() throws IOException {
        /*boolean dbExist = checkDataBase();
        if (!dbExist) {*/
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
//        }
    }

/*    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DATABASE_NAME);
        return dbFile.exists();
    }*/

    private void copyDataBase() throws IOException {
        InputStream myInput = mContext.getAssets().open(DATABASE_NAME);
        String outFileName = DB_PATH + DATABASE_NAME;
        OutputStream myOutput = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myOutput = Files.newOutputStream(Paths.get(outFileName));
        }

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Lesson> getAllLessons() {
        List<Lesson> lessons = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM study_titles", null);
            if (cursor != null) {
                logCursorInfo(cursor);
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String lessonNumber = cursor.getString(cursor.getColumnIndex("LessonNumber"));
                    String title = cursor.getString(cursor.getColumnIndex("Title"));
                    String image = cursor.getString(cursor.getColumnIndex("Image"));
                    String mark = cursor.getString(cursor.getColumnIndex("Mark"));
                    int checked = cursor.getInt(cursor.getColumnIndex("Cheked"));

                    Lesson lesson = new Lesson(id, lessonNumber, title, image, mark, checked);
                    lessons.add(lesson);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lessons;
    }

    private void logCursorInfo(Cursor cursor) {
        String[] columnNames = cursor.getColumnNames();
        for (String columnName : columnNames) {
            Log.d(TAG, "Column name: " + columnName);
        }
    }
}
