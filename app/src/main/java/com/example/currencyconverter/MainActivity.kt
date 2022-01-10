package com.example.currencyconverter

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.currencyconverter.databinding.ActivityMainBinding
import com.example.currencyconverter.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConvert.setOnClickListener {
            viewModel.convert(
                binding.etFrom.text.toString(),
                binding.spFromCurrency.selectedItem.toString(),
                binding.spToCurrency.selectedItem.toString(),
            )
        }

        collectLatestLifecycleFlow(viewModel.conversion) { event ->
            when (event) {
                is MainViewModel.CurrencyEvent.Success -> {
                    binding.progressBar.isVisible = false
                    binding.tvResult.setTextColor(Color.BLACK)
                    binding.tvResult.text = event.resultText
                }
                is MainViewModel.CurrencyEvent.Failure -> {
                    binding.progressBar.isVisible = false
                    binding.tvResult.setTextColor(Color.RED)
                    binding.tvResult.text = event.errorText
                }
                is MainViewModel.CurrencyEvent.Loading -> {
                    binding.progressBar.isVisible = true
                }
                else -> Unit
            }
        }
    }
}

fun <T> AppCompatActivity.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}