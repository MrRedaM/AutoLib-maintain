package com.clovertech.autolib.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.clovertech.autolib.model.Tache

@Dao
interface TacheDAO {
    @Query ("SELECT * from taches")
    fun getAllTaches(): LiveData<List<Tache>>
    @Insert
    fun insertAllTaches(tacheList : List<Tache>)
    @Insert
    suspend fun insertTache(tache : Tache)
    


}