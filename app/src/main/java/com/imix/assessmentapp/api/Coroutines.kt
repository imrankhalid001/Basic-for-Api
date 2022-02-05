package com.imix.assessmentapp.api

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Coroutines {

    private const val TAG = "Coroutines"
    fun main(work: suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            work()
        }


    fun io(work: suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                work()
            } catch (e: Exception) {
                Log.wtf(TAG, "Exception: ${e.toString()}")
                e.printStackTrace()
            }
        }

}