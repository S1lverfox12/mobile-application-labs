
package com.example.myapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("О себе") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Информация о себе",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(32.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Профиль",
                modifier = Modifier.size(100.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                InfoRow(label = "ФИО", value = "Торгайкин Илья Владиславович")
                InfoRow(label = "Учебное заведение", value = "УлГУ")
                InfoRow(label = "Группа", value = "МОАИС-О-22/1")
                InfoRow(label = "Тема приложения", value = "Магазин видеокарт")

            }
        }


    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
