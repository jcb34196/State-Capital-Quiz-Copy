package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizQuestionFragment extends Fragment {

    private static final String ARG_QUESTION = "question";

    private Question question;
    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;

    public static QuizQuestionFragment newInstance(Question question) {
        QuizQuestionFragment fragment = new QuizQuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUESTION, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = (Question) getArguments().getSerializable(ARG_QUESTION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_question, container, false);

        questionTextView = view.findViewById(R.id.questionTextView);
        optionsRadioGroup = view.findViewById(R.id.optionsRadioGroup);

        // Display question and options
        questionTextView.setText("What is the capital city for the state of " + question.getStateName() + "?");

        List<String> options = new ArrayList<>();
        options.add(question.getCapitalCity()); // Correct answer
        options.add(question.getCity1());       // Incorrect answer 1
        options.add(question.getCity2());       // Incorrect answer 2

        // Shuffle the options
        Collections.shuffle(options);

        // Set options text
        ((RadioButton) optionsRadioGroup.getChildAt(0)).setText(options.get(0));
        ((RadioButton) optionsRadioGroup.getChildAt(1)).setText(options.get(1));
        ((RadioButton) optionsRadioGroup.getChildAt(2)).setText(options.get(2));

        // Listener to capture answer selection
        optionsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = group.findViewById(checkedId);
            if (selectedRadioButton != null) {
                String selectedAnswer = selectedRadioButton.getText().toString();
                ((QuizActivity) getActivity()).recordAnswer(question, selectedAnswer);
            }
        });

        return view;
    }
}

