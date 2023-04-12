package com.example.composition.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding

class GameFinishedFragment : Fragment() {

    private val args by navArgs<GameFinishedFragmentArgs>()
    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding ==  null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupClickListeners()
    }

    private fun initViews() {

        with(binding) {
            tvRequiredAnswers.text = String.format(
                getString(
                    (R.string.required_answers),
                    args.gameResult.countOfQuestions.toString()

                )
            )

            tvScoresAnswers.text = String.format(
                getString(
                    (R.string.scores_answers),
                    args.gameResult.countOfRightAnswers.toString()
                )
            )

            emojiResult.setImageResource(getImageResId())

            tvScoresPercentage.text = String.format(
                getString(
                    (R.string.scores_percentage),
                    getPercentageOfRightAnswer().toString()
                )
            )

            tvRequiredPercentage.text = String.format(
                getString(
                    (R.string.required_percentage),
                    args.gameResult.gameSettings.minPercentOfRightAnswers.toString()
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun getPercentageOfRightAnswer() = with(args.gameResult) {
        if (countOfQuestions == 0) {
            0
        } else {
            ((countOfRightAnswers.toDouble() *  100)  / countOfQuestions).toInt()
        }
    }

    private fun getImageResId(): Int {
        return if (!args.gameResult.winner) {
            R.drawable.smile_bad
        } else {
            R.drawable.smile_good
        }
    }

    private fun setupClickListeners() {

        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }
}