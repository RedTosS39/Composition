package com.example.composition.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entities.GameSettings
import com.example.composition.domain.entities.Level
import com.example.composition.domain.repository.GameRepository
import com.example.composition.domain.usecases.GenerateQuestionsUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(

) : ViewModel() {

    private val gameRepository: GameRepository = GameRepositoryImpl
    private val generateQuestionsUseCase =  GenerateQuestionsUseCase(gameRepository)
    private val getGameSettingsUseCase =  GetGameSettingsUseCase(gameRepository)

   private lateinit var  _liveDataSettings: MutableLiveData<GameSettings>
   val liveDataSettings: LiveData<GameSettings> = _liveDataSettings

    fun generateLevel(level: Level) {
        _liveDataSettings.postValue(getGameSettingsUseCase.invoke(level))
    }

    fun generateQuestions(maxSumValue: Int) {
        generateQuestionsUseCase.invoke(maxSumValue)
    }
}