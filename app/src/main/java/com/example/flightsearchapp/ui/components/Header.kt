package com.example.flightsearchapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flightsearchapp.R

@Composable
fun Header()
{
    Text(
        text = stringResource(id = R.string.app_name),
        modifier = Modifier.padding(bottom = 16.dp).padding(top = 16.dp),
        fontSize = 32.sp,
        style = TextStyle(
            color = Color.White,
            shadow = Shadow(
                color = Color.Black.copy(alpha = 1f),
                blurRadius = 8f
            )
        )
    )
}