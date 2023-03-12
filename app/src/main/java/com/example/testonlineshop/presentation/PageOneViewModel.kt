package com.example.testonlineshop.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testonlineshop.model.FlashSale
import com.example.testonlineshop.model.Latest
import com.example.testonlineshop.network.GetApiService
import kotlinx.coroutines.launch
import retrofit2.Response

class PageOneViewModel : ViewModel() {

    private var _latestList = MutableLiveData<Response<Latest>>()
    val latestList: LiveData<Response<Latest>> = _latestList

    private var _flashSaleList = MutableLiveData<Response<FlashSale>>()
    val flashSaleList: LiveData<Response<FlashSale>> = _flashSaleList

    init {
        getDataFromServer()
    }

    private fun getDataFromServer() {
        viewModelScope.launch {
            try {
                val latestResult = GetApiService.retrofitService.getLatest()
                _latestList.value = latestResult

                val flashSaleResult = GetApiService.retrofitService.getFlashSale()
                _flashSaleList.value = flashSaleResult
            } catch (e: Exception) {
                Log.d(
                    "PageOneViewModel",
                    "Hello from background thread! Something wrong, try Later!"
                )
            }
        }
    }
}