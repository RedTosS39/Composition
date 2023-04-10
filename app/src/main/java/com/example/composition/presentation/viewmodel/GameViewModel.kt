package com.example.composition.presentation.viewmodel

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composition.R
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entities.GameResult
import com.example.composition.domain.entities.GameSettings
import com.example.composition.domain.entities.Level
import com.example.composition.domain.entities.Questions
import com.example.composition.domain.repository.GameRepository
import com.example.composition.domain.usecases.GenerateQuestionsUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application
    private val gameRepository: GameRepository = GameRepositoryImpl
    private val generateQuestionsUseCase = GenerateQuestionsUseCase(gameRepository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(gameRepository)
    private lateinit var settings: GameSettings
    private lateinit var level: Level

    private var timer: CountDownTimer? = null

    private val _minPercent = MutableLiveData<Int>()
    private val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private val _liveDataSettings = MutableLiveData<GameSettings>()
    val liveDataSettings: LiveData<GameSettings>
        get() = _liveDataSettings

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent


    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _percentageOfRightAnswers = MutableLiveData<Int>()
    val percentageOfRightAnswers: LiveData<Int>
        get() = _percentageOfRightAnswers

    private val _question = MutableLiveData<Questions>()
    val question: LiveData<Questions>
        get() = _question

    private var _countOfRightAnswers: Int = 0
    private var _countOfQuestions: Int = 0

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = calculatePercentageOfRightAnswers()
        _percentageOfRightAnswers.value = percent
        _progressAnswers.value = String.format(
            context.resources.getString(R.string.progress_answers),
            _countOfRightAnswers,
            settings.minCountOfRightAnswers
        )

        _enoughCount.value = _countOfRightAnswers >= settings.minCountOfRightAnswers
        _enoughPercent.value = percent >= settings.minPercentOfRightAnswers
    }

    private fun calculatePercentageOfRightAnswers(): Int {

        return (_countOfRightAnswers / _countOfQuestions.toDouble() * 100).toInt()
    }

    private fun choseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()

    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            _countOfRightAnswers++
        }
        _countOfQuestions++
    }

    private fun getGameSettings(level: Level) {

        this.level = level
        this.settings = getGameSettingsUseCase.invoke(level)
        _minPercent.value = settings.minPercentOfRightAnswers
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            settings.gameTimeIntSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.postValue(formatTime(millisUntilFinished))
            }

            override fun onFinish() {
                finishGame()
            }
        }

        timer?.start()
    }

    private fun generateQuestion() {
        _question.value = generateQuestionsUseCase(settings.maxSumValue)
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()

    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String().format("%02d:%02d", minutes, leftSeconds)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughCount.value == true && enoughPercent.value == true,
            _countOfRightAnswers,
            _countOfQuestions,
            settings
        )
    }


    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}