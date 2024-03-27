package com.example.pc_builder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pc_builder.databinding.LessonsPickViewBinding;

import java.util.ArrayList;
import java.util.List;

public class LessonsPickAdapter extends RecyclerView.Adapter<LessonsPickAdapter.ViewHolder> {

    private List<Lessons> mLessons;
    private Context mContext;

    public LessonsPickAdapter(List<Lessons> lessons, Context context) {
        this.mLessons = lessons;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LessonsPickViewBinding binding = LessonsPickViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lessons lessons = mLessons.get(position);
        holder.bind(lessons);
    }

    @Override
    public int getItemCount() {
        return mLessons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LessonsPickViewBinding binding;

        public ViewHolder(LessonsPickViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Lessons lessons) {
            binding.lessonNumberText.setText(lessons.getLessonNumber());
            binding.lessonTitleText.setText("Тема урока: " + lessons.getTitle());

            if (lessons.getTitle().contains("Введение")) {
                binding.lessonMarkText.setVisibility(android.view.View.GONE);
            } else {
                if (lessons.getMark() == null) {
                    binding.lessonMarkText.setText("Оценка: еще не пройдено");
                } else {
                    binding.lessonMarkText.setText("Оценка: " + lessons.getMark());
                }
            }

            if (lessons.getChecked() == 0) {
                binding.lessonCheckedText.setVisibility(android.view.View.GONE);
            } else {
                binding.lessonCheckedText.setVisibility(android.view.View.VISIBLE);
            }

            if (!lessons.getImage().isEmpty()) {
                int imageResource = mContext.getResources().getIdentifier(lessons.getImage(), "drawable", mContext.getPackageName());
                binding.lessonImage.setImageResource(imageResource);
            } else {
                binding.lessonImage.setImageResource(R.drawable.default_image);
            }

            binding.getRoot().setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Lessons clickedLessons = mLessons.get(position);
                    Intent intent = new Intent(mContext, LessonDetailActivity.class);
                    intent.putExtra("LESSON_NUMBER", clickedLessons.getLessonNumber());
                    //intent.putExtra("LESSON_LIST", )
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
