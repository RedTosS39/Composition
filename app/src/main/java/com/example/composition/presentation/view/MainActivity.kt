package com.example.composition.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.composition.R
import com.example.composition.presentation.viewmodel.GameViewModel

class MainActivity : AppCompatActivity() {
    private  var viewModel = ViewModelProvider(this@MainActivity)[GameViewModel::class.java]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}