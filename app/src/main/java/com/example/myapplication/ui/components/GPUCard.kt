
package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

@Composable
fun GPUCard(
    gpu: GPU,
    isInCart: Boolean,
    onAddToCart: () -> Unit,
    onRemoveFromCart: () -> Unit,
    onItemClick: () -> Unit
) {
    val context = LocalContext.current
    val imageName = if (gpu.imageUrl.isNullOrBlank()) "no_image" else gpu.imageUrl

    val imageResId = context.resources.getIdentifier(
        imageName,
        "drawable",
        context.packageName
    )
    Card(
        colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween

        ) {

            Image(
                painter = painterResource(id = imageResId),
                contentDescription = gpu.name,
                modifier = Modifier
                    .size(200.dp)
            )



            Spacer(modifier = Modifier.width(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {

                Column {
                    Text(
                        text = gpu.name,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "${gpu.manufacturer} • ${gpu.memorySize}GB • ${gpu.memoryType}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "%.2f ₽".format(gpu.price),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = if (isInCart) onRemoveFromCart else onAddToCart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isInCart) Color.Red else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(if (isInCart) "Удалить из корзины" else "Добавить в корзину")
            }
        }
    }
}