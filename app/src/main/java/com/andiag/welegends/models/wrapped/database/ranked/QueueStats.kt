package com.andiag.welegends.models.wrapped.database.ranked

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Canalejas on 30/12/2016.
 */

class QueueStats {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("tier")
    @Expose
    var tier: String? = null
    @SerializedName("queue")
    @Expose
    var queue: QueueType? = null
    @SerializedName("entries")
    @Expose
    var entries: List<Entry>? = null

}
