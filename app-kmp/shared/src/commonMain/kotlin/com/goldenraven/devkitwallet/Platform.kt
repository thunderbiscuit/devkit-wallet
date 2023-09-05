package com.goldenraven.devkitwallet

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform