package es.coru.andiag.welegends.models.utils

import com.google.gson.Gson
import com.raizlabs.android.dbflow.converter.TypeConverter

/**
 * Created by Canalejas on 13/12/2016.
 */

@com.raizlabs.android.dbflow.annotation.TypeConverter
class ConverterStringList : TypeConverter<String, Array<String>>() {

    override fun getDBValue(model: Array<String>?): String? {
        return if (model == null) null else gson!!.toJson(model)
    }

    override fun getModelValue(data: String): Array<String> {
        return gson!!.fromJson(data, Array<String>::class.java)
    }

    companion object {

        private var gson: Gson? = null

        init {
            gson = Gson()
        }
    }
}
