package com.example.pc_builder;

public class Lessons {
    private int id;
    private String mLessonNumber;
    private String mTitle;
    private String image;
    private String mMark;
    private int mChecked;

    public Lessons(int id, String mLessonNumber, String mTitle, String image, String mMark, int mChecked) {
        this.id = id;
        this.mLessonNumber = mLessonNumber;
        this.mTitle = mTitle;
        this.image = image;
        this.mMark = mMark;
        this.mChecked = mChecked;
    }

    public int getId() {
        return id;
    }

    public String getLessonNumber() {
        return mLessonNumber;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImage() {
        return image;
    }

    public String getMark() {
        return mMark;
    }

    public int getChecked() {
        return mChecked;
    }
}