package com.example.lab_week_10

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.database.TotalObject
import com.example.lab_week_10.viewmodels.TotalViewModel
import kotlin.getValue

class MainActivity : AppCompatActivity() {
    private val db by lazy { prepareDatabase() }
    private val viewModel by lazy { ViewModelProvider(this)[TotalViewModel::class.java] }
    private var isFirstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeValueFromDatabase()
        prepareViewModel()
    }

    override fun onPause() {
        super.onPause()
        val totalValue = viewModel.total.value?.value ?: 0
        val date = viewModel.total.value?.date ?: ""
        db.totalDao().update(Total(1, TotalObject(totalValue, date))) // Save total and date
    }

    override fun onStart() {
        super.onStart()
        // Amati LiveData untuk mendapatkan nilai terbaru
        viewModel.total.observe(this, { totalObject ->
            // Tampilkan Toast hanya setelah LiveData berubah
            val date = totalObject.date
            if (date.isNotEmpty() && isFirstTime){
                Toast.makeText(this, "Last updated: $date", Toast.LENGTH_LONG).show()
                isFirstTime = false
            }

        })
    }

    private fun prepareViewModel() {
        viewModel.total.observe(this, {
            updateText(it.value)
        })

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java, "total-database"
        ).allowMainThreadQueries().build()
    }

    private fun initializeValueFromDatabase() {
        val total = db.totalDao().getTotal(ID)
        if (total.isEmpty()) {
            db.totalDao().insert(Total(id = 1, total = TotalObject(0, "")))
        } else {
            val totalObject = total.first().total
            viewModel.setTotal(totalObject)
        }
    }

    companion object {
        const val ID: Long = 1
    }
}
