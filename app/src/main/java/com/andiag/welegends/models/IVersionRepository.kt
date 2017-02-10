package com.andiag.welegends.models

import com.andiag.commons.interfaces.presenters.AIInterfaceErrorHandlerPresenter
import com.andiag.commons.interfaces.presenters.AIInterfaceLoaderHandlerPresenter

/**
 * Created by Canalejas on 10/02/2017.
 */
interface IVersionRepository {
    /**
     * Get loaded version or set a callback.
     * Use null as callback if we just want to check the version
     * @param [caller] callback required if version are not loaded
     */
    fun getVersion(caller: AIInterfaceLoaderHandlerPresenter<String>?): String?

    /**
     * Ask if app is loading static data
     * @return [Boolean] true if loading false otherwise
     */
    fun isLoading(): Boolean

    /**
     * Check if our local version correspond to server version. If not, call all static data loaders
     * @return [AIInterfaceLoaderPresenter.onLoadSuccess] or [AIInterfaceLoaderPresenter.onLoadError]
     */
    fun loadVersion(caller: AIInterfaceErrorHandlerPresenter)
}