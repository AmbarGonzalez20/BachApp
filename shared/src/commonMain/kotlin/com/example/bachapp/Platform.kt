package com.example.bachapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform