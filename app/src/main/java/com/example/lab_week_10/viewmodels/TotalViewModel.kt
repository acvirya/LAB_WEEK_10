package com.example.lab_week_10.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab_week_10.database.TotalObject

class TotalViewModel : ViewModel() {
    private val _total = MutableLiveData<TotalObject>()
    val total: LiveData<TotalObject> = _total

    init {
        // Set initial value for total
        _total.postValue(TotalObject(0, ""))
    }

    fun incrementTotal() {
        // Ambil nilai total saat ini
        val currentTotal = _total.value?.value ?: 0
        val newTotal = currentTotal + 1

        // Ambil waktu saat ini sebagai string
        val currentDate = java.util.Date().toString()

        // Debug log untuk memastikan bahwa tanggal terupdate
        Log.d("TotalViewModel", "Updated total: $newTotal, Updated date: $currentDate")

        // Perbarui LiveData dengan total dan date yang baru
        _total.postValue(TotalObject(newTotal, currentDate))
    }

    fun setTotal(newTotal: TotalObject) {
        _total.postValue(newTotal)
    }
}
