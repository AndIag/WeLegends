package com.andiag.welegends.models.utils.converters

import com.raizlabs.android.dbflow.structure.BaseModel
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper

/**
 * Created by andyq on 11/02/2017.
 */
open class OrmBaseModel : BaseModel() {

    fun saveOrUpdate() : Boolean {
        try {
            return update()
        } catch (e:Exception){
            return save()
        }
    }

    fun saveOrUpdate(databaseWrapper: DatabaseWrapper){
        try {
            update(databaseWrapper)
        } catch (e:Exception){
            save(databaseWrapper)
        }
    }
}