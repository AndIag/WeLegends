package com.andiag.welegends.models.utils

/**
 * Created by andyq on 11/02/2017.
 */
interface CallbackData<T> {
    fun onSuccess(data:T)
    fun onError(t:Throwable?)
}