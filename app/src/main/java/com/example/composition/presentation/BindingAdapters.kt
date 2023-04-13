package com.example.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entities.GameResult

interface OnOptionCLick {
    fun onOptionCLick(option: Int)
}

@BindingAdapter("countOfQuestions")
fun bindCountOfQuestions(textView: TextView, countOfQuestions: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_answers), countOfQuestions
    )
}

@BindingAdapter("scoresAnswers")
fun bindScoresAnswers(textView: TextView, countOfRightAnswers: Int) {
    textView.text =
        String.format(textView.context.getString(R.string.scores_answers), countOfRightAnswers)
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, requiredPercentage: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_percentage), requiredPercentage
    )
}

@BindingAdapter("scoresPercentage")
fun bindScoresPercentage(
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

@BindingAdapter("answerProgress")
fun bindAnswerProgress(textView: TextView, progress: String) {
    textView.text = progress
}

@BindingAdapter("setColor")
fun bindAnswerProgressColor(textView: TextView, state: Boolean) {
    textView.setTextColor(getColorByState(textView.context, state))
}

@BindingAdapter("setProgressBar")
fun bindProgressBar(progressBar: ProgressBar, percent: Int) {
    progressBar.setProgress(percent, true)
}

@BindingAdapter("setSecondaryProgress")
fun bindSecondaryProgress(progressBar: ProgressBar, minPercent: Int) {
    progressBar.secondaryProgress = minPercent
}

@BindingAdapter("setProgressBarColor")
fun bindProgressBarColor(progressBar: ProgressBar, state: Boolean) {
    val color = getColorByState(progressBar.context, state)
    progressBar.progressTintList = ColorStateList.valueOf(color)
}

private fun getColorByState(context: Context, state: Boolean): Int {
    val colorResId =  if (state) {
        android.R.color.holo_green_dark
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(context, colorResId)
}

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, number: Int) {

    textView.text = number.toString()
}


@BindingAdapter("onOptionCLick")
fun bindOnOptionCLick(textView: TextView, clickListener: OnOptionCLick)  {

    textView.setOnClickListener {
        clickListener.onOptionCLick(textView.text.toString().toInt())
    }
}