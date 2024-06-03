package com.example.pc_builder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

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
        this.getReadableDatabase();
        try {
            copyDataBase();
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = mContext.getAssets().open(DATABASE_NAME);
        String outFileName = DB_PATH + DATABASE_NAME;
        OutputStream myOutput = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

    public String getNextLesson(String currentLessonNumber) {
        String lessonText = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM study_lessons WHERE LessonNumber > ? ORDER BY LessonNumber ASC LIMIT 1", new String[]{currentLessonNumber});
            if (cursor != null && cursor.moveToFirst()) {
                lessonText = cursor.getString(cursor.getColumnIndex("LessonText"));
                Log.d(TAG, lessonText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lessonText;
    }

    public String getLessonDetails(String lessonNumber){
        String lessonText = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM study_lessons WHERE LessonNumber = ?", new String[]{lessonNumber});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    lessonText = cursor.getString(cursor.getColumnIndex("LessonText"));
                    Log.d("да", lessonText);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lessonText;
    }

    public String getLessonVideo(String lessonNumber){
        String lessonLink = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM study_lessons WHERE LessonNumber = ?", new String[]{lessonNumber});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    lessonLink = cursor.getString(cursor.getColumnIndex("link"));
                    Log.d("да", lessonLink);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lessonLink;
    }

    public List<Lessons> getAllLessons() {
        List<Lessons> lessons = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM study_titles", null);
            if (cursor != null) {
                logCursorInfo(cursor);
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String lessonNumber = cursor.getString(cursor.getColumnIndex("LessonNumber"));
                    String title = cursor.getString(cursor.getColumnIndex("Title"));
                    String image = cursor.getString(cursor.getColumnIndex("image"));
                    Lessons lesson = new Lessons(id, lessonNumber, title, image);
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
    public boolean hasTest(String lessonNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM study_tests WHERE LessonNumber = ?", new String[]{lessonNumber});
        boolean hasTest = cursor.getCount() > 0;
        cursor.close();
        return hasTest;
    }
    public List<Question> getQuestionsForLesson(String lessonNumber) {
        List<Question> questionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM study_questions WHERE TestID IN (SELECT id FROM study_tests WHERE LessonNumber = ?)", new String[]{lessonNumber});
        if (cursor.moveToFirst()) {
            do {
                String questionType = cursor.getString(cursor.getColumnIndex("QuestionType"));
                String questionText = cursor.getString(cursor.getColumnIndex("Question"));
                String option1 = cursor.getString(cursor.getColumnIndex("Option1"));
                String option2 = cursor.getString(cursor.getColumnIndex("Option2"));
                String option3 = cursor.getString(cursor.getColumnIndex("Option3"));
                String option4 = cursor.getString(cursor.getColumnIndex("Option4"));
                String correctAnswer = cursor.getString(cursor.getColumnIndex("CorrectAnswer"));
                Question question = new Question(questionType, questionText, option1, option2, option3, option4, correctAnswer);
                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }

    private void logCursorInfo(Cursor cursor) {
        String[] columnNames = cursor.getColumnNames();
        for (String columnName : columnNames) {
            Log.d(TAG, "Column name: " + columnName);
        }
    }
}
