package com.irvanmaulana0013.asessment1.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.irvanmaulana0013.asessment1.R
import com.irvanmaulana0013.asessment1.navigation.Screen
import com.irvanmaulana0013.asessment1.ui.theme.Asessment1Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ScreenContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    var diameter by rememberSaveable { mutableStateOf("") }
    var diameterError by rememberSaveable { mutableStateOf(false) }

    var jariJari by rememberSaveable { mutableStateOf("") }
    var jariJariError by rememberSaveable { mutableStateOf(false) }

    var tinggi by rememberSaveable { mutableStateOf("") }
    var tinggiError by rememberSaveable { mutableStateOf(false) }

    val radioOptions = listOf(
        stringResource(id = R.string.diameter),
        stringResource(id = R.string.jarijari)
    )

    var option by rememberSaveable { mutableStateOf(radioOptions[0]) }
    var result by rememberSaveable { mutableDoubleStateOf(0.0) }

    Column(
        modifier = modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = stringResource(id = R.string.intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            radioOptions.forEach { text ->
                Option(
                    label = text,
                    isSelected = option == text,
                    modifier = Modifier
                        .selectable(
                            selected = option == text,
                            onClick = { option = text },
                            role = Role.RadioButton
                        )
                        .weight(1f)
                        .padding(16.dp)
                )
            }
        }
        if (option == radioOptions[0]) {
            OutlinedTextField(
                value = diameter,
                onValueChange = { diameter = it },
                label = { Text(text = stringResource(id = R.string.diameter)) },
                trailingIcon = { IconPicker(diameterError, "m") },
                supportingText = { ErrorHint(diameterError) },
                isError = diameterError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 20.dp)
            )
        } else {
            OutlinedTextField(
                value = jariJari,
                onValueChange = { jariJari = it},
                label = { Text(text = stringResource(id = R.string.jarijari)) },
                trailingIcon = { IconPicker(jariJariError, "m") },
                supportingText = { ErrorHint(jariJariError) },
                isError = jariJariError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 20.dp)
            )
        }
        OutlinedTextField(
            value = tinggi,
            onValueChange = { tinggi = it},
            label = { Text(text = stringResource(id = R.string.tinggi)) },
            trailingIcon = { IconPicker(tinggiError, "m") },
            supportingText = { ErrorHint(tinggiError) },
            isError = tinggiError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    tinggiError = (tinggi == "" || tinggi == "0")

                    if (option == radioOptions[0]) {
                        diameterError = (diameter == "" || diameter == "0")
                        if (diameterError || tinggiError) return@Button
                        result = hitungDiameter(diameter.toDouble(), tinggi.toDouble())
                    } else {
                        jariJariError = (jariJari == "" || jariJari == "0")
                        if (jariJariError || tinggiError) return@Button
                        result = hitungJarijari(jariJari.toDouble(), tinggi.toDouble())
                    }
                },
                modifier = Modifier.padding(top = 12.dp, end = 16.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.hitung))
            }
            Button(
                onClick = {
                    diameter = ""
                    jariJari = ""
                    tinggi = ""
                    diameterError = false
                    jariJariError = false
                    tinggiError = false
                    result = 0.0
                },
                modifier = Modifier.padding(top = 12.dp, end = 16.dp),
                contentPadding = PaddingValues(horizontal = 34.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.reset))
            }
        }

        if (result != 0.0) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp
            )
            Text(
                text = stringResource(R.string.teks),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.hasil, result),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}

@Composable
fun Option(label: String, isSelected: Boolean, modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = null)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(R.string.invalid))
    }
}

private fun hitungDiameter (diameter: Double, tinggi: Double): Double {
    return Math.PI * diameter * diameter * tinggi / 4
}

private fun hitungJarijari (jariJari: Double, tinggi: Double): Double {
    return Math.PI * jariJari * jariJari * tinggi
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Asessment1Theme {
        MainScreen(rememberNavController())
    }
}