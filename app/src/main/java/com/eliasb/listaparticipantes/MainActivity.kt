package com.eliasb.listaparticipantes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class Participante(val nombre: String, val alias: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaTorneo(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PantallaTorneo(modifier: Modifier = Modifier) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var alias by rememberSaveable { mutableStateOf("") }
    val participantes = remember { mutableStateListOf<Participante>() }

    // el boton solo se activa si los dos campos tienen algo escrito
    val formularioValido = nombre.isNotBlank() && alias.isNotBlank()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Inscripción al torneo",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = alias,
            onValueChange = { alias = it },
            label = { Text("Alias") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (formularioValido) {
                    val nuevo = Participante(nombre.trim(), alias.trim())
                    participantes.add(nuevo)
                    nombre = ""
                    alias = ""
                }
            },
            enabled = formularioValido,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir participante")
        }

        Text(
            text = "Participantes inscritos: ${participantes.size}",
            style = MaterialTheme.typography.titleMedium
        )

        if (participantes.isEmpty()) {
            Text("Todavía no hay participantes")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(participantes) { participante ->
                    TarjetaParticipante(
                        participante = participante,
                        onEliminar = { participantes.remove(participante) }
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaParticipante(participante: Participante, onEliminar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8DEF8))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(participante.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Alias: ${participante.alias}")
            }
            // boton para eliminar al participante de la lista
            IconButton(onClick = onEliminar) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTorneo() {
    MaterialTheme {
        PantallaTorneo()
    }
}
