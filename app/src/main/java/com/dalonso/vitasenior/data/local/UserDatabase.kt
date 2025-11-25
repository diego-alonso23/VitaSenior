package com.dalonso.vitasenior.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow


@Entity(tableName = "user_table")
data class User(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val age: String,
    val emergencyNumber: String
)


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

    @Query("SELECT * FROM user_table WHERE id = 1")
    fun getUser(): Flow<User?>
}

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class VitaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: VitaDatabase? = null

        fun getDatabase(context: Context): VitaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VitaDatabase::class.java,
                    "vita_senior_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}