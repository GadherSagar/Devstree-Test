package com.test.devstree_test.database

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.devstree_test.database.Constant.DATABASE_NAME
import com.test.devstree_test.database.Constant.DATABASE_VERSION
import com.test.devstree_test.database.entity.LocationEntity

@Database(
    entities = [LocationEntity::class], version = DATABASE_VERSION, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract val locationDao: LocationDao
}