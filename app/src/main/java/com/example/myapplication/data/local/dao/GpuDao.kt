package com.example.myapplication
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GPUDao {
    @Query("SELECT * FROM gpus")
    fun getAllGPUs(): Flow<List<GPU>>

    @Query("SELECT * FROM gpus WHERE id = :gpuId")
    fun getGPUById(gpuId: Int): Flow<GPU>

    @Insert
    suspend fun insertGPU(gpu: GPU)

    @Query("DELETE FROM gpus")
    suspend fun clearAllGPUs()
}