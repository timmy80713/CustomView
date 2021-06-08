package com.timmy.codelab.customview.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.timmy.codelab.customview.databinding.ActivityCircleLoadingViewBinding

class CircleLoadingViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCircleLoadingViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCircleLoadingViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}