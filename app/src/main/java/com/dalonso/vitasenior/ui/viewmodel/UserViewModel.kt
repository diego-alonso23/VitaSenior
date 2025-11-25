package com.dalonso.vitasenior.ui.viewmodel

import android.app.Application
import android.util.Log // <--- IMPORTANTE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dalonso.vitasenior.data.local.User
import com.dalonso.vitasenior.data.local.VitaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val db = VitaDatabase.getDatabase(application)
    private val dao = db.userDao()

    val currentUser: StateFlow<User?> = dao.getUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun registerUser(name: String, age: String, number: String, onSuccess: () -> Unit) {

        Log.d("DEBUG_DB", "Intentando guardar usuario: $name")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = User(id = 1, name = name, age = age, emergencyNumber = number)
                dao.saveUser(user)


                Log.d("DEBUG_DB", "Usuario guardado exitosamente en BD")

                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {

                Log.e("DEBUG_DB", "Error al guardar: ${e.message}")
            }
        }
    }
}

