package edu.tyut.datastorelearn.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

private const val TAG: String = "DataSoreService"

class DataSoreService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand...")
        return super.onStartCommand(intent, flags, startId)
    }
}