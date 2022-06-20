package com.example.firstkotlinapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firstkotlinapp.databinding.CryptoItemBinding

class CryptocurrencyAdapter : RecyclerView.Adapter<CryptocurrencyAdapter.CryptocurrencyHolder>() {
    val cryptoList = ArrayList<Cryptocurrency>()
    private lateinit var mListener: onItemClickListener

    class CryptocurrencyHolder(item: View, listener: onItemClickListener) : RecyclerView.ViewHolder(item){
        val binding = CryptoItemBinding.bind(item)
        fun bind(crypto: Cryptocurrency){
            binding.imV.setImageResource(crypto.imageId)
            binding.twName.text = crypto.name
            if (crypto.crypto?.data?.priceUsd !== null){
                val price : Double = crypto.crypto?.data?.priceUsd.toString().toDouble()
                binding.twCost.text = "Cost = " + String.format("%.2f", price) + " $"
            }
            if (crypto.crypto?.data?.changePercent24Hr !== null){
                val changing : Double = crypto.crypto?.data?.changePercent24Hr.toString().toDouble()
                binding.twChanging.text = "Changing = " + String.format("%.2f", changing)  + "%"
                if (changing < 0) {
                    binding.twChanging.setTextColor(Color.RED)
                } else{
                    binding.twChanging.setTextColor(Color.GREEN)
                }
            }
        }
        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    interface onItemClickListener{
        fun onItemClick(position : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptocurrencyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.crypto_item, parent, false)
        return CryptocurrencyHolder(view, mListener);
    }

    override fun onBindViewHolder(holder: CryptocurrencyHolder, position: Int) {
        holder.bind(cryptoList[position])
    }

    override fun getItemCount(): Int {
        return cryptoList.size
    }

    fun addCrypto(crypto: Cryptocurrency){
        cryptoList.add(crypto)
        notifyDataSetChanged()
    }

    fun addAll(list : List<Cryptocurrency>){
        cryptoList.clear()
        cryptoList.addAll(list)
        notifyDataSetChanged()
    }

    fun getItem(position: Int) : Cryptocurrency{
        return cryptoList[position]
    }
}