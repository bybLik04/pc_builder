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
    private String currentLessonNumber;
    private int totalLessons;
    private List<Lessons> mLessons;
    private int mCurrentLessonIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLessonDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.LessonDetailAppBar.setNavigationOnClickListener(v -> {
            finish();
        });

        String lessonNumber = getIntent().getStringExtra("LESSON_NUMBER");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String lessonText = dbHelper.getLessonDetails(lessonNumber);
        String lessonLink = dbHelper.getLessonVideo(lessonNumber);
        mLessons = dbHelper.getAllLessons();
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

        // Проверка и скрытие кнопки, если текущий урок последний
        if (mCurrentLessonIndex == mLessons.size() - 1) {
            binding.lessonNext.setVisibility(View.GONE);
        }

        binding.lessonNext.setOnClickListener(v -> {
            if (mCurrentLessonIndex < mLessons.size() - 1) {
                mCurrentLessonIndex++;
                Lessons nextLesson = mLessons.get(mCurrentLessonIndex);
                Intent intent = new Intent(this, LessonDetailActivity.class);
                intent.putExtra("LESSON_NUMBER", nextLesson.getLessonNumber());
                startActivity(intent);
                finish();
            }
        });
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

    private void loadLesson(String lessonNumber) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String lessonText = dbHelper.getLessonDetails(lessonNumber);

        dbHelper.close();


        binding.lessonWebView.loadDataWithBaseURL("file:///android_asset/", lessonText, "text/html; charset=utf-8", "utf-8", null);
        binding.lessonWebView.setWebViewClient(new WebViewClient());
    }

    private String getNextLesson() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String nextLessonText = dbHelper.getNextLesson(currentLessonNumber);
        dbHelper.close();
        return nextLessonText;
    }

    private String getNextLessonNumber(String currentLessonNumber) {
        // Extract the numeric part from the lesson number and increment it
        int currentNumber = Integer.parseInt(currentLessonNumber.replaceAll("\\D+", ""));
        int nextNumber = currentNumber + 1;
        return "Урок " + nextNumber;
    }

    private void updateNextButtonVisibility() {
        if (currentLessonNumber.contains("Введение") || Integer.parseInt(currentLessonNumber.replaceAll("\\D+", "")) >= totalLessons) {
            binding.lessonNext.setVisibility(View.GONE);
        } else {
            binding.lessonNext.setVisibility(View.VISIBLE);
        }
    }


}
