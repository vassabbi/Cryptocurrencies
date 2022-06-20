package com.example.firstkotlinapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstkotlinapp.databinding.ActivityMainBinding
import com.example.firstkotlinapp.interfaces.RetrofitServices
import com.example.firstkotlinapp.model.Crypto
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    val BASE_URL = "https://api.coincap.io/v2/"
    private lateinit var binding: ActivityMainBinding
    private  val adapter = CryptocurrencyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        generateCrypto()
    }

    private fun init(){
        binding.rcView.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rcView.adapter = adapter
        adapter.setOnItemClickListener(object : CryptocurrencyAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@MainActivity, InfoActivity::class.java)
                intent.putExtra(InfoActivity.CRYPTO_NAME, adapter.getItem(position))
                startActivity(intent)
                //Toast.makeText(this@MainActivity, "You Clicked on item no. $position", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun generateCrypto(){
        val cryptoList : MutableList<Cryptocurrency> = mutableListOf()
        cryptoList.add(Cryptocurrency("bitcoin",  R.drawable.bitcoin, null))
        cryptoList.add(Cryptocurrency("ethereum", R.drawable.ethereum, null))
        cryptoList.add( Cryptocurrency("tether",  R.drawable.tether, null))
        cryptoList.add( Cryptocurrency("eos",  R.drawable.eos, null))
        cryptoList.add( Cryptocurrency("stellar",  R.drawable.stellar, null))
        cryptoList.add( Cryptocurrency("litecoin",  R.drawable.litecoin, null))
        cryptoList.add( Cryptocurrency("cardano",  R.drawable.cardano, null))
        cryptoList.add( Cryptocurrency("bitcoin-cash",  R.drawable.bitcoin_cash, null))

        val retrofitBuider = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(RetrofitServices::class.java)

        for (crypto in cryptoList) {

            val data = retrofitBuider.getCrypto(crypto.name)

            data.enqueue(object : retrofit2.Callback<Crypto?> {
                override fun onResponse(call: Call<Crypto?>, response: Response<Crypto?>) {
                    val responseBody = response.body()!!
                    crypto.crypto = responseBody
                    //adapter.addCrypto(crypto)
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<Crypto?>, t: Throwable) {
                }
            })
        }
        adapter.addAll(cryptoList)
    }
}