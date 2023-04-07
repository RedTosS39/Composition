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

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, callback)

        with(binding) {
            tvRequiredAnswers.text = gameResult.countOfQuestions.toString()
            tvScoresAnswers.text = gameResult.countOfRightAnswers.toString()
            if (gameResult.winner) {
                emojiResult.setImageResource(R.drawable.smile_bad)
            }

            buttonRetry.setOnClickListener {
                retryGame()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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