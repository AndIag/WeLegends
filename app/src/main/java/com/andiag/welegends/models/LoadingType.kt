package com.andiag.welegends.models

/**
 * Created by Canalejas on 11/02/2017.
 */
enum class LoadingType {

    /**
     * Use in memory value if exists
     */
    NO_LOAD,

    /**
     * Search a value throw cache or database
     */
    LOCAL_LOAD,

    /**
     * Use network to find a value
     */
    REMOTE_LOAD

}