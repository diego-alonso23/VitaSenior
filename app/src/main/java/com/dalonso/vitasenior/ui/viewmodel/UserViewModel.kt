package com.dalonso.vitasenior.ui.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dalonso.vitasenior.AlarmReceiver
import com.dalonso.vitasenior.data.local.ExerciseLog
import com.dalonso.vitasenior.data.local.Reminder
import com.dalonso.vitasenior.data.local.User
import com.dalonso.vitasenior.data.local.VitaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val db = VitaDatabase.getDatabase(application)
    private val dao = db.userDao()
    private val context = application.applicationContext


    val currentUser: StateFlow<User?> = dao.getUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    val completedCount: StateFlow<Int> = dao.getCompletedExercisesCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val recentLogs: StateFlow<List<ExerciseLog>> = dao.getRecentExercises()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    val reminders: StateFlow<List<Reminder>> = dao.getAllReminders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {

        viewModelScope.launch {
            dao.getUser().collect {
                _isLoading.value = false
            }
        }
    }

    fun registerUser(name: String, age: String, number: String, photoUri: String? = null, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val current = currentUser.value
            val finalPhotoUri = photoUri ?: current?.profilePictureUri

            val user = User(
                id = 1,
                name = name,
                age = age,
                emergencyNumber = number,
                profilePictureUri = finalPhotoUri
            )
            dao.saveUser(user)

            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }


    fun updateUserProfile(name: String, number: String, photoUri: String?, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val current = currentUser.value
            val age = current?.age ?: ""
            val finalPhotoUri = photoUri ?: current?.profilePictureUri

            val user = User(
                id = 1,
                name = name,
                age = age,
                emergencyNumber = number,
                profilePictureUri = finalPhotoUri
            )
            dao.saveUser(user)

            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }


    fun markExerciseAsCompleted(exerciseId: Int, title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val log = ExerciseLog(
                exerciseId = exerciseId,
                exerciseTitle = title,
                timestamp = System.currentTimeMillis()
            )
            dao.insertExerciseLog(log)
        }
    }


    fun addReminder(title: String, timeString: String, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val reminder = Reminder(title = title, time = timeString, type = type)
            val id = dao.insertReminder(reminder).toInt()
            scheduleAlarm(id, title, timeString)
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteReminder(reminder)
            cancelAlarm(reminder.id)
        }
    }



    private fun scheduleAlarm(id: Int, title: String, timeString: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("TITLE", "Recordatorio VitaSenior")
            putExtra("MESSAGE", "$title - Es hora: $timeString")
            putExtra("ID", id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val timeInMillis = parseTimeToMillis(timeString)

        if (timeInMillis > System.currentTimeMillis()) {
            try {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
                Log.d("ALARM", "Alarma programada para: $timeString (ID: $id)")
            } catch (e: SecurityException) {
                Log.e("ALARM", "Falta permiso para alarmas exactas")
            }
        }
    }

    private fun cancelAlarm(id: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun parseTimeToMillis(timeString: String): Long {
        return try {
            val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val date = formatter.parse(timeString) ?: return 0L

            val calendar = Calendar.getInstance()
            val now = Calendar.getInstance()
            val tempCal = Calendar.getInstance()
            tempCal.time = date

            calendar.set(Calendar.HOUR_OF_DAY, tempCal.get(Calendar.HOUR_OF_DAY))
            calendar.set(Calendar.MINUTE, tempCal.get(Calendar.MINUTE))
            calendar.set(Calendar.SECOND, 0)

            if (calendar.before(now)) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }

            calendar.timeInMillis
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }
}