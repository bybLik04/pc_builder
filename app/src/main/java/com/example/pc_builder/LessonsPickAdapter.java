package com.example.pc_builder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pc_builder.databinding.LessonsPickViewBinding;

import java.util.List;

public class LessonsPickAdapter extends RecyclerView.Adapter<LessonsPickAdapter.ViewHolder> {

    private List<Lesson> mLessons;
    private Context mContext;

    public LessonsPickAdapter(List<Lesson> lessons, Context context) {
        this.mLessons = lessons;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LessonsPickViewBinding binding = LessonsPickViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LessonsPickViewBinding binding;


        public ViewHolder(LessonsPickViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Lesson lesson) {
            binding.lessonNumberText.setText(lesson.getLessonNumber());
            binding.lessonTitleText.setText("Тема урока: " + lesson.getTitle());

            if (lesson.getTitle().contains("Введение")) {
                binding.lessonMarkText.setVisibility(android.view.View.GONE);
            } else {
                if (lesson.getMark() == null) {
                    binding.lessonMarkText.setText("Оценка: еще не пройдено");
                } else {
                    binding.lessonMarkText.setText("Оценка: " + lesson.getMark());
                }
            }

            if (lesson.getChecked() == 0) {
                binding.lessonCheckedText.setVisibility(android.view.View.GONE);
            } else {
                binding.lessonCheckedText.setVisibility(android.view.View.VISIBLE);
            }

            if (!lesson.getImage().isEmpty()) {
                int imageResource = mContext.getResources().getIdentifier(lesson.getImage(), "drawable", mContext.getPackageName());
                binding.lessonImage.setImageResource(imageResource);
            } else {
                binding.lessonImage.setImageResource(R.drawable.default_image);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lesson lesson = mLessons.get(position);
        holder.bind(lesson);
    }

    @Override
    public int getItemCount() {
        return mLessons.size();
    }
}
