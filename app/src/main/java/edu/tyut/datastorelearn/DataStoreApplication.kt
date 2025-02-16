package edu.tyut.datastorelearn

import android.app.Application
import android.util.Log
import com.tencent.mmkv.MMKV

private const val TAG: String = "DataStoreApplication"

class DataStoreApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate...")
        initMMKV()
    }
    private fun initMMKV(){
        val rootDir: String = MMKV.initialize(this)
        Log.i(TAG, "initMMKV -> rootDir: $rootDir")
    }
}