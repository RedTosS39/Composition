package com.example.composition.presentation.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entities.GameResult

class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult
    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding ==  null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

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
                    gameResult.countOfQuestions.toString()
                )
            )

            tvScoresAnswers.text = String.format(
                getString(
                    (R.string.scores_answers),
                    gameResult.countOfRightAnswers.toString()
                )
            )

            emojiResult.setImageResource(getImageResId())

            tvScoresPercentage.text = String.format(
                getString(
                    (R.string.scores_percentage),
                    getPercentageOfRightAnswer()
                )
            )

            tvRequiredPercentage.text = String.format(
                getString(
                    (R.string.required_percentage),
                    gameResult.gameSettings.minPercentOfRightAnswers.toString()
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun getPercentageOfRightAnswer() = with(gameResult) {
        if (countOfQuestions == 0) {
            0
        } else {
            ((countOfRightAnswers.toDouble() *  100)  / countOfQuestions).toInt()
        }
    }

    private fun getImageResId(): Int {
        return if (!gameResult.winner) {
            R.drawable.smile_bad
        } else {
            R.drawable.smile_good
        }
    }

    private fun setupClickListeners() {

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )

    }

    private fun parseArgs() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(GAME_RESULT, GameResult::class.java)?.let {
                gameResult = it
            }
        }
    }


    companion object {
        const val GAME_RESULT = "GAME_RESULT"
        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(GAME_RESULT, gameResult)
                }
            }
        }
    }
}