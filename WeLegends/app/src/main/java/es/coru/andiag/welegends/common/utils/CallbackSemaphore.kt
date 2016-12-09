package es.coru.andiag.welegends.common.utils

import java.util.concurrent.Callable
import java.util.concurrent.Semaphore

/**
 * Created by Canalejas on 09/12/2016.
 */

class CallbackSemaphore(permits: Int, private val callable: Callable<*>?) : Semaphore(permits) {

    override fun release() {
        super.release()
        if (queueLength == 0 && callable != null) {
            try {
                callable.call()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
