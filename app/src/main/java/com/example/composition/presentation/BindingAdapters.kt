package com.example.composition.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entities.GameResult


@BindingAdapter("countOfQuestions")
fun bindingCountOfQuestions(textView: TextView, countOfQuestions: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_answers), countOfQuestions
    )
}

@BindingAdapter("scoresAnswers")
fun bindingScoresAnswers(textView: TextView, countOfRightAnswers: Int) {
    textView.text =
        String.format(textView.context.getString(R.string.scores_answers), countOfRightAnswers)
}

@BindingAdapter("requiredPercentage")
fun bindingRequiredPercentage(textView: TextView, requiredPercentage: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_percentage), requiredPercentage
    )
}

@BindingAdapter("scoresPercentage")
fun bindingScoresPercentage(
    textView: TextView,
    gameResult: GameResult,
) {
    textView.text = String.format(textView.context.getString(R.string.scores_percentage),
        getPercentageOfRightAnswer(gameResult))
}

private fun getPercentageOfRightAnswer(gameResult: GameResult) = with(gameResult) {
    if (countOfQuestions == 0) {
        0
    } else {
        ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }
}

@BindingAdapter("setEmoji")
fun bindEmoji(imageView: ImageView, winner: Boolean) {
    imageView.setImageResource(getImageResId(winner))
}

private fun getImageResId(winner: Boolean): Int {
    return if (winner) {
        R.drawable.smile_good
    } else {
        R.drawable.smile_bad
    }
}