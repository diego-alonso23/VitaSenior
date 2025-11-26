package com.dalonso.vitasenior.data.local

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Entity(tableName = "user_table")
data class User(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val age: String,
    val emergencyNumber: String,
    val profilePictureUri: String? = null
)


@Entity(tableName = "exercise_logs")
data class ExerciseLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val exerciseId: Int,
    val exerciseTitle: String,
    val timestamp: Long
)

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val time: String,
    val type: String
)


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

    @Query("SELECT * FROM user_table WHERE id = 1")
    fun getUser(): Flow<User?>

    @Insert
    suspend fun insertExerciseLog(log: ExerciseLog)

    @Query("SELECT COUNT(*) FROM exercise_logs")
    fun getCompletedExercisesCount(): Flow<Int>

    @Query("SELECT * FROM exercise_logs ORDER BY timestamp DESC LIMIT 5")
    fun getRecentExercises(): Flow<List<ExerciseLog>>

    @Insert
    suspend fun insertReminder(reminder: Reminder): Long

    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    @Query("SELECT * FROM reminders ORDER BY time ASC")
    fun getAllReminders(): Flow<List<Reminder>>


}


@Database(entities = [User::class, ExerciseLog::class, Reminder::class], version = 4, exportSchema = false)
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
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}