package com.example.pc_builder;

public class Lessons {
    private int id;
    private String mLessonNumber;
    private String mTitle;
    private String image;


    public Lessons(int id, String mLessonNumber, String mTitle, String image) {
        this.id = id;
        this.mLessonNumber = mLessonNumber;
        this.mTitle = mTitle;
        this.image = image;
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

}