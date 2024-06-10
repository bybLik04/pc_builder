package com.example.pc_builder;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pc_builder.databinding.ActivityTestBinding;
import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestActivity extends AppCompatActivity {
    private ActivityTestBinding binding;
    private LinearLayout questionContainer;
    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int correctAnswersCount = 0;
    private int score;
    private String testTitle;
    private String lessonNumber;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.testAppBar.setNavigationOnClickListener(v -> {
            finish();
        });

        questionContainer = findViewById(R.id.question_container);
        lessonNumber = getIntent().getStringExtra("LESSON_NUMBER");

        dbHelper = new DatabaseHelper(this);
        questionList = dbHelper.getQuestionsForLesson(lessonNumber);
        Map<String, Object> result = dbHelper.getTestScore(lessonNumber);

        testTitle = (String) result.get("TestTitle");
        binding.resultText.setText(testTitle);
        score = (int) result.get("Score");
        if (score > 0 ) {
            showQuizResults(true);
        } else {
            showNextQuestion();
        }

        binding.actionButton.setOnClickListener(v -> {
            checkAnswer();
            showNextQuestion();
        });
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
            showQuizResults(false);
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

    private void checkAnswer() {
        Question currentQuestion = questionList.get(currentQuestionIndex - 1);
        switch (currentQuestion.getQuestionType()) {
            case "single_choice":
                RadioButton selectedOption = findViewById(R.id.option1);
                if (selectedOption.isChecked() && selectedOption.getText().toString().equals(currentQuestion.getAnswer()[0])) {
                    correctAnswersCount++;
                }
                selectedOption = findViewById(R.id.option2);
                if (selectedOption.isChecked() && selectedOption.getText().toString().equals(currentQuestion.getAnswer()[0])) {
                    correctAnswersCount++;
                }
                selectedOption = findViewById(R.id.option3);
                if (selectedOption.isChecked() && selectedOption.getText().toString().equals(currentQuestion.getAnswer()[0])) {
                    correctAnswersCount++;
                }
                selectedOption = findViewById(R.id.option4);
                if (selectedOption.isChecked() && selectedOption.getText().toString().equals(currentQuestion.getAnswer()[0])) {
                    correctAnswersCount++;
                }
                break;
            case "multiple_choice":
                CheckBox option1 = findViewById(R.id.option1);
                CheckBox option2 = findViewById(R.id.option2);
                CheckBox option3 = findViewById(R.id.option3);
                CheckBox option4 = findViewById(R.id.option4);

                String[] correctAnswers = currentQuestion.getAnswer();
                boolean allCorrect = true;

                if (option1.isChecked() != Arrays.asList(correctAnswers).contains(option1.getText().toString())) allCorrect = false;
                if (option2.isChecked() != Arrays.asList(correctAnswers).contains(option2.getText().toString())) allCorrect = false;
                if (option3.isChecked() != Arrays.asList(correctAnswers).contains(option3.getText().toString())) allCorrect = false;
                if (option4.isChecked() != Arrays.asList(correctAnswers).contains(option4.getText().toString())) allCorrect = false;

                if (allCorrect) correctAnswersCount++;
                break;
            case "text_input":
                EditText answerInput = findViewById(R.id.answer_input);
                if (answerInput.getText().toString().trim().equals(currentQuestion.getAnswer()[0])) {
                    correctAnswersCount++;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown question type");
        }
    }

    private void showQuizResults(boolean passed) {
        if (passed){
            String resultMessage = "Тест пройден.\nВаш результат: оценка " + score;
            initResultView(resultMessage);
            dbHelper.close();
        } else {
            int totalQuestions = questionList.size();
            double percentage = (double) correctAnswersCount / totalQuestions * 100;
            int testScore;
            if (!lessonNumber.equals("ИТОГОВЫЙ")){
                if (correctAnswersCount < 3){
                    testScore = 2;
                } else if (correctAnswersCount == 3) {
                    testScore = 3;
                } else if (correctAnswersCount == 4) {
                    testScore = 4;
                } else {
                    testScore = 5;
                }
            } else {
                if (correctAnswersCount <= 6){
                    testScore = 2;
                } else if (correctAnswersCount <= 9) {
                    testScore = 3;
                } else if (correctAnswersCount <= 12) {
                    testScore = 4;
                } else {
                    testScore = 5;
                }
            }

            String resultMessage = "Результат теста: " + correctAnswersCount + " из " + totalQuestions + " вопросов верно (" + percentage + "%)\n" + "Ваша оценка: " + testScore;
            initResultView(resultMessage);
            dbHelper.saveTestResult(lessonNumber, testScore);
            dbHelper.close();
        }
    }
    private void initResultView(String resultMessage) {
        binding.resultText.setVisibility(View.VISIBLE);
        binding.resultText.setText(resultMessage);
        binding.actionButton.setVisibility(View.GONE);

        MaterialButton retakeButton = new MaterialButton(this);
        retakeButton.setText("Пройти заново");
        retakeButton.setTextColor(Color.WHITE);
        retakeButton.setOnClickListener(v -> {
            currentQuestionIndex = 0;
            correctAnswersCount = 0;
            questionContainer.removeAllViews();
            binding.resultText.setVisibility(View.GONE);
            binding.actionButton.setVisibility(View.VISIBLE);
            showNextQuestion();
        });

        MaterialButton returnButton = new MaterialButton(this);
        returnButton.setText("Вернуться");
        returnButton.setTextColor(Color.WHITE);
        returnButton.setOnClickListener(v -> finish());

        questionContainer.removeAllViews();
        questionContainer.addView(retakeButton);
        questionContainer.addView(returnButton);
    }
}
