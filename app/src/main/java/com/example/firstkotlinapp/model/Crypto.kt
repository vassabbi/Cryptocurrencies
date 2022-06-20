package com.example.firstkotlinapp.model

import java.io.Serializable

data class Crypto(
    val `data`: Data,
    val timestamp: Long
) : Serializable