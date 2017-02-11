package com.andiag.welegends.common.utils

/**
 * Created by andyq on 11/02/2017.
 */
interface CallbackData<in T> {
    fun onSuccess(data:T)
    fun onError(t:Throwable?)
}