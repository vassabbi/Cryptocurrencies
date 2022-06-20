package com.example.firstkotlinapp

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.firstkotlinapp.databinding.ActivityInfoBinding
import com.example.firstkotlinapp.databinding.ActivityMainBinding
import com.example.firstkotlinapp.interfaces.RetrofitServices
import com.example.firstkotlinapp.model.Crypto
import com.example.firstkotlinapp.model.History
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class InfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityInfoBinding

    val BASE_URL = "https://api.coincap.io/v2/"
    companion object{
        val CRYPTO_NAME = "crypto"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    //private val LineChart lineChart

    fun init(){
        val crypto = intent.getSerializableExtra(CRYPTO_NAME) as Cryptocurrency

        binding.image.setImageResource(crypto.imageId)
        binding.twInfoName.text = crypto.name
        if (crypto.crypto != null) {
            binding.twInfoCost.text = "Cost = " + String.format(
                "%.2f",
                crypto.crypto?.data?.priceUsd.toString().toDouble()
            ) + " $"
            binding.twVolumeUsd.text =
                "Quantity of trading volume represented in USD over the last 24 hours: " +
                        String.format(
                            "%.2f",
                            crypto.crypto?.data?.volumeUsd24Hr.toString().toDouble()
                        ) + " $"
            binding.twSupply.text = "Available supply for trading: " +
                    String.format("%.0f", crypto.crypto?.data?.supply.toString().toDouble())

            makeRequest(crypto)
        }
    }

    fun makeRequest(crypto : Cryptocurrency){
        val retrofitBuider = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(RetrofitServices::class.java)

        val data = retrofitBuider.getCryptoHistory(crypto.name)

        data.enqueue(object : retrofit2.Callback<History?> {
            override fun onResponse(call: Call<History?>, response: Response<History?>) {
                val responseBody = response.body()!!
                setLineChartData(responseBody)
            }

            override fun onFailure(call: Call<History?>, t: Throwable) {
            }
        })
    }

    fun setLineChartData(history: History){
        val xvalue = ArrayList<String>()
        val lineEntry = arrayListOf<Entry>()

        var index = 0f

        for (course in history.data){
            val formatter = java.text.SimpleDateFormat("dd/MM/yyyy")
            val calendar = Calendar.getInstance()
            calendar.setTimeInMillis(course.time)
            lineEntry.add(Entry(index, course.priceUsd.toFloat()))
            xvalue.add(formatter.format(calendar.getTime()))
            index = index + 1
        }

        class FormatterXAxis : ValueFormatter(){
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return xvalue[value.toInt()]
            }
        }

        val formatter : ValueFormatter = FormatterXAxis()


        val lineDataSet = LineDataSet(lineEntry, "Rate")

        val dataSets = ArrayList<ILineDataSet?>();
        dataSets.add(lineDataSet)
        val data = LineData(dataSets)

        val xAxis = binding.cryptoChart.xAxis
        xAxis.granularity = 60f
        xAxis.valueFormatter = formatter
        xAxis.setLabelCount(5, true)

        binding.cryptoChart.data = data
        binding.cryptoChart.setBackgroundColor(resources.getColor(R.color.white))
        binding.cryptoChart.description.text = ""

    }

    fun onClickClose(view : View){
        finish()
    }
}