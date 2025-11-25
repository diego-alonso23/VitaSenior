package com.dalonso.vitasenior.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalonso.vitasenior.R
import com.dalonso.vitasenior.ui.theme.VitaSeniorTheme

@Composable
fun RegistroScreen(

    onRegisterClick: (String, String, String) -> Unit,
    onSpeakRequest: (String) -> Unit
) {
    val context = LocalContext.current


    var nombre by rememberSaveable { mutableStateOf("") }
    var edad by rememberSaveable { mutableStateOf("") }
    var numero by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Image(
            painter = painterResource(id = R.drawable.vitasenior_logo),
            contentDescription = null,
            modifier = Modifier.Companion.size(250.dp)
                .clickable { onSpeakRequest("Logotipo de Vita Senior") }
        )

        Text(
            text = "Crear Cuenta",
            fontSize = 32.sp,
            fontWeight = FontWeight.Companion.Bold,
            fontStyle = FontStyle.Companion.Italic,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.Companion.clickable { onSpeakRequest("Pantalla Crear Cuenta") }
        )

        Spacer(modifier = Modifier.Companion.height(32.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = {
                Text(
                    "Nombre Completo",
                    Modifier.Companion.clickable { onSpeakRequest("Campo para Nombre Completo") })
            },
            modifier = Modifier.Companion.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.Text)
        )

        Spacer(modifier = Modifier.Companion.height(16.dp))

        OutlinedTextField(
            value = edad,
            onValueChange = { if (it.all { char -> char.isDigit() }) edad = it },
            label = {
                Text("Edad", Modifier.Companion.clickable { onSpeakRequest("Campo para Edad") })
            },
            modifier = Modifier.Companion.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.Number)
        )

        Spacer(modifier = Modifier.Companion.height(24.dp))

        OutlinedTextField(
            value = numero,
            onValueChange = { if (it.all { char -> char.isDigit() }) numero = it },
            label = {
                Text(
                    "Numero De Emergencia",
                    Modifier.Companion.clickable { onSpeakRequest("Campo para Número de Emergencia") })
            },
            modifier = Modifier.Companion.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.Number)
        )

        Spacer(modifier = Modifier.Companion.height(24.dp))

        Button(
            onClick = {
                if (nombre.isEmpty() || edad.isEmpty() || numero.isEmpty()) {
                    val msg = "Por favor, completa todos los campos"
                    onSpeakRequest(msg)
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                } else {

                    onRegisterClick(nombre, edad, numero)
                }
            },
            modifier = Modifier.Companion
                .fillMaxWidth()
                .height(56.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Companion.Green,
                contentColor = Color.Companion.White
            )
        ) {
            Text(text = "Registrarme", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.Companion.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroScreenPreview() {
    VitaSeniorTheme {
        RegistroScreen(
            onRegisterClick = { _, _, _ -> },
            onSpeakRequest = {}
        )
    }
}