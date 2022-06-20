package com.example.firstkotlinapp

import com.example.firstkotlinapp.model.Crypto
import java.io.Serializable

data class Cryptocurrency(val name: String, val imageId : Int, var crypto : Crypto?) : Serializable
