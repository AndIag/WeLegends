package com.andiag.welegends.models.utils

import android.util.Log
import java.util.concurrent.Callable
import java.util.concurrent.Semaphore

/**
 * Created by Canalejas on 09/12/2016.
 */

class CallbackSemaphore(val permits: Int, private val callable: Callable<*>?) : Semaphore(permits) {
    private val TAG: String = CallbackSemaphore::class.java.simpleName

    override fun release() {
        super.release()
        if (availablePermits() == this.permits && callable != null) {
            try {
                callable.call()
            } catch (e: Exception) {
                Log.e(TAG, e.message)
                e.printStackTrace()
            }
        }
    }

    override fun release(permits: Int) {
        super.release(permits)
        if (availablePermits() == this.permits && callable != null) {
            try {
                callable.call()
            } catch (e: Exception) {
                Log.e(TAG, e.message)
                e.printStackTrace()
            }
        }
    }
}
