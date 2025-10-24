package com.example.nycjobs.data

import android.content.Context
import android.util.Log
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Upsert
import com.example.nycjobs.model.JobPost
import com.example.nycjobs.util.TAG
import kotlinx.coroutines.flow.Flow

/**
 * Job Post Data Access Object (DAO)
 *
 * This interface defines the data access object for the JobPost entity.
 */
@Dao
interface JobPostDao {
    @Query("SELECT * FROM JobPost ORDER BY postingLastUpdated DESC")
    fun getAll(): Flow<List<JobPost>>

    @Query("SELECT * FROM JobPost WHERE jobId = :id")
    fun get(id: Int): Flow<JobPost>

    @Upsert(entity = JobPost::class)
    suspend fun upsert(jobPostings: List<JobPost>)
}

/**
 * Local Database
 *
 * This class defines the local database for the app.
 */
@Database(entities = [JobPost::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun jobPostDao(): JobPostDao

    companion object {
        private const val DATABASE = "local_database"

        @Volatile
        private var Instance: LocalDatabase? = null

        /**
         * Get The Database.
         *
         * @param context the context of the app
         * @return the database
         */
        fun getDatabase(context: Context): LocalDatabase {
            Log.i(TAG, "getting database")
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, LocalDatabase::class.java, DATABASE)
                    .fallbackToDestructiveMigration(false).build().also { Instance = it }
                }
            }
        }
    }