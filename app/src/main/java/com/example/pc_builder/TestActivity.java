package com.example.pc_builder;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pc_builder.databinding.ActivityTestBinding;

import java.util.List;

public class TestActivity extends AppCompatActivity {
    private ActivityTestBinding binding;
    private LinearLayout questionContainer;
    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int correctAnswersCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        questionContainer = findViewById(R.id.question_container);

        String lessonNumber = getIntent().getStringExtra("LESSON_NUMBER");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        questionList = dbHelper.getQuestionsForLesson(lessonNumber);
        dbHelper.close();

        showNextQuestion();
        binding.nextQuestionButton.setOnClickListener(v -> showNextQuestion());
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            Question currentQuestion = questionList.get(currentQuestionIndex);
            questionContainer.removeAllViews();

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View questionView;

            switch (currentQuestion.getQuestionType()) {
                case "single_choice":
                    questionView = inflater.inflate(R.layout.single_choice_question, null);
                    setupSingleChoiceQuestion(questionView, currentQuestion);
                    break;
                case "multiple_choice":
                    questionView = inflater.inflate(R.layout.multiple_choice_question, null);
                    setupMultipleChoiceQuestion(questionView, currentQuestion);
                    break;
                case "text_input":
                    questionView = inflater.inflate(R.layout.text_input_question, null);
                    setupTextInputQuestion(questionView, currentQuestion);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown question type");
            }

            questionContainer.addView(questionView);
            currentQuestionIndex++;
        } else {
            // Квиз завершен
            showQuizResults();
        }
    }

    private void setupSingleChoiceQuestion(View view, Question question) {
        TextView questionText = view.findViewById(R.id.question_text);
        questionText.setText(question.getQuestion());

        RadioButton option1 = view.findViewById(R.id.option1);
        option1.setText(question.getOption1());

        RadioButton option2 = view.findViewById(R.id.option2);
        option2.setText(question.getOption2());

        RadioButton option3 = view.findViewById(R.id.option3);
        option3.setText(question.getOption3());

        RadioButton option4 = view.findViewById(R.id.option4);
        option4.setText(question.getOption4());
    }

    private void setupMultipleChoiceQuestion(View view, Question question) {
        TextView questionText = view.findViewById(R.id.question_text);
        questionText.setText(question.getQuestion());

        CheckBox option1 = view.findViewById(R.id.option1);
        option1.setText(question.getOption1());


        CheckBox option2 = view.findViewById(R.id.option2);
        option2.setText(question.getOption2());

        CheckBox option3 = view.findViewById(R.id.option3);
        option3.setText(question.getOption3());

        CheckBox option4 = view.findViewById(R.id.option4);
        option4.setText(question.getOption4());
    }

    private void setupTextInputQuestion(View view, Question question) {
        TextView questionText = view.findViewById(R.id.question_text);
        questionText.setText(question.getQuestion());
    }

    private void showQuizResults() {
        int totalQuestions = questionList.size();
        double percentage = (double) correctAnswersCount / totalQuestions * 100;
        String resultMessage = "Результат теста: " + correctAnswersCount + " из " + totalQuestions + " вопросов верно (" + percentage + "%)";
        binding.resultText.setText(resultMessage);
    }
}