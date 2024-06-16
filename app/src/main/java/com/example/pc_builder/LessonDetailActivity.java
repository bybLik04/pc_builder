package com.example.pc_builder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebViewClient;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pc_builder.databinding.ActivityLessonDetailBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

public class LessonDetailActivity extends AppCompatActivity {
    private ActivityLessonDetailBinding binding;
    private List<Lessons> mLessons;
    private int mCurrentLessonIndex;
    private String query;
    private boolean hasTest;
    private String lessonText;
    private String lessonLink;
    private int part;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLessonDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.LessonDetailAppBar.setNavigationOnClickListener(v -> {
            finish();
        });

        String lessonNumber = getIntent().getStringExtra("LESSON_NUMBER");
        part = getIntent().getIntExtra("LESSON_TYPE", 1);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        if (part == 1) {
            query = "study_titles";
            lessonText = dbHelper.getLessonDetails(lessonNumber, "study_lessons");
            lessonLink = dbHelper.getLessonVideo(lessonNumber, "study_lessons");
            hasTest = dbHelper.hasTest(lessonNumber);
        } else if (part == 2) {
            query = "build_titles";
            lessonText = dbHelper.getLessonDetails(lessonNumber, "build_lessons");
            lessonLink = dbHelper.getLessonVideo(lessonNumber, "build_lessons");
            hasTest = false;
        }
        mLessons = dbHelper.getAllLessons(query);
        dbHelper.close();

        // Находим индекс текущего урока
        for (int i = 0; i < mLessons.size(); i++) {
            if (mLessons.get(i).getLessonNumber().equals(lessonNumber)) {
                mCurrentLessonIndex = i;
                break;
            }
        }

        if (lessonLink != null) {
            configureYouTubePlayer(lessonLink);
            Log.d("link", lessonLink);
        }

        // Загрузка текста урока в WebView
        binding.lessonWebView.loadDataWithBaseURL("file:///android_asset/", lessonText, "text/html; charset=utf-8", "utf-8", null);
        binding.lessonWebView.setWebViewClient(new WebViewClient());

        if (part == 1){
            if (mCurrentLessonIndex == mLessons.size() - 2) {
                binding.lessonNext.setVisibility(View.GONE);
            }
        } else {
            if (mCurrentLessonIndex == mLessons.size() - 1) {
                binding.lessonNext.setVisibility(View.GONE);
            }
        }


        binding.lessonNext.setOnClickListener(v -> {
            if (mCurrentLessonIndex < mLessons.size() - 1) {
                mCurrentLessonIndex++;
                Lessons nextLesson = mLessons.get(mCurrentLessonIndex);
                Intent intent = new Intent(this, LessonDetailActivity.class);
                intent.putExtra("LESSON_NUMBER", nextLesson.getLessonNumber());
                intent.putExtra("LESSON_TYPE", part);
                startActivity(intent);
                finish();
            }
        });

        if (hasTest) {
            binding.lessonTest.setVisibility(View.VISIBLE);
            binding.lessonTest.setOnClickListener(v -> {
                Intent intent = new Intent(this, TestActivity.class);
                intent.putExtra("LESSON_NUMBER", lessonNumber);
                startActivity(intent);
            });
        } else {
            binding.lessonTest.setVisibility(View.GONE);
        }
    }
    private void configureYouTubePlayer(String videoId) {
        YouTubePlayerView youTubePlayerView = binding.youtubePlayerView;
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.setVisibility(View.VISIBLE);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0);
            }
        });

        // Show the YouTubePlayerView after WebView content is loaded
        binding.lessonWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(android.webkit.WebView view, String url) {
                youTubePlayerView.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
}
