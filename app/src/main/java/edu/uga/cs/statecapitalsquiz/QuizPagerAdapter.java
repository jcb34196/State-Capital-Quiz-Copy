package edu.uga.cs.statecapitalsquiz;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class QuizPagerAdapter extends FragmentStateAdapter {

    private Quiz quiz;

    public QuizPagerAdapter(@NonNull FragmentActivity fragmentActivity, Quiz quiz) {
        super(fragmentActivity);
        this.quiz = quiz;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return QuizQuestionFragment.newInstance(quiz.getQuestion(position));
    }

    @Override
    public int getItemCount() {
        return quiz.getTotalQuestions();
    }
}

