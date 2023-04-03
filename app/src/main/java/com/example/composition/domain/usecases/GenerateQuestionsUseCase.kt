package com.example.composition.domain.usecases

import com.example.composition.domain.entities.Questions
import com.example.composition.domain.repository.GameRepository

class GenerateQuestionsUseCase(private val gameRepository: GameRepository) {

    operator fun invoke(
        maxSumValue: Int
    ): Questions {
        return gameRepository.generateQuestions(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object {

        private const val COUNT_OF_OPTIONS = 6
    }
}