package com.example.routee_commerce

import android.app.Application
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: MultiDexApplication() {
}