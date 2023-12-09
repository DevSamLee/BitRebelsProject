package com.example.vividay.presentation.data_screen

import android.media.Image
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vividay.models.Day
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.vividay.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.ui.res.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    onSaved: () -> Unit,
    initialDateTime: String = "",
    initialDayColor: String = "",
    initialDayNote: String = ""
)  {
    val database = Firebase.database
    var dateTime by remember {
        mutableStateOf(initialDateTime)
    }
    var dayColor by remember {
        mutableStateOf(initialDayColor)
    }
    var dayNote by remember {
        mutableStateOf(initialDayNote)
    }

    var selectedDayColor by rememberSaveable {
        mutableStateOf<DropDownItem?>(null)
    }

    val dayColorItems = listOf(
        DropDownItem("Great", "#42eff5"),
        DropDownItem("Good","#2267f2"),
        DropDownItem("SoSo","#12c45c"),
        DropDownItem("Bad","#f59e42"),
        DropDownItem("Terrible", "#f54e42")
    )

    val faceIcons = listOf(
        GreatFaceIcon(),
        GoodFaceIcon(),
        SoSoFaceIcon(),
        BadFaceIcon(),
        TerribleFaceIcon()
    )

    val greatIcon = painterResource(id = R.drawable.smiling)
    val goodIcon = painterResource(id = R.drawable.happy)
    val soSoIcon = painterResource(id = R.drawable.neutral)
    val badIcon = painterResource(id = R.drawable.sad)
    val terribleIcon = painterResource(id = R.drawable.crying)

    Box(
    modifier = Modifier
        .fillMaxSize()
        .padding(26.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .width(330.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(20.dp),
                text = "How's your day today?",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            TextField(
                modifier = Modifier
                    .width(330.dp),
                value = dateTime,
                onValueChange = { newText -> dateTime = newText },
                label = { Text(text = "Date") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                dayColorItems.forEach { item ->
                    Button(
                        onClick = {
                            selectedDayColor = item
                            // Handle the selected day color as needed
                        },
                        modifier = Modifier
                            .weight(1f) // Use weight to fill the available space
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(android.graphics.Color.parseColor(item.color)))
                    ) {
                        // Use the specific face icon corresponding to the current dayColorItem
                        Box(
                        ) {
                            Image(
                                painter = when (item.text) {
                                    "Great" -> greatIcon
                                    "Good" -> goodIcon
                                    "SoSo" -> soSoIcon
                                    "Bad" -> badIcon
                                    "Terrible" -> terribleIcon
                                    else -> throw IllegalArgumentException("Invalid dayColorItem text: ${item.text}")
                                },
                                contentDescription = null,
                                modifier = Modifier.size(50.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }
                    }
                }
            }
            TextField(
                value = dayNote,
                onValueChange = { newText -> dayNote = newText },
                label = { Text(text = "Note") },
                modifier = Modifier
                    .height(300.dp)
                    .width(330.dp)
            )
            Spacer(modifier = Modifier.weight(1f)) // Spacer to push content above bottom navigation bar

            val context = LocalContext.current
            Button(
                onClick = {
                    val daysRef = database.reference.child("Day")
                    val dayRef = daysRef.child(dateTime)
                    val dayInfo = Day(dateTime, selectedDayColor?.color ?: "", dayNote)
                    dayRef.setValue(dayInfo)
                    Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show()
                    dateTime = ""
                    dayColor = ""
                    dayNote = ""
                },
                modifier = Modifier.padding(9.dp)
            ) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
fun GreatFaceIcon(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.smiling),
        contentDescription = "Great Day",
        modifier = modifier.size(50.dp),
        colorFilter = ColorFilter.tint(Color.White)
    )
}

@Composable
fun GoodFaceIcon(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.happy),
        contentDescription = "Good Day",
        modifier = modifier.size(50.dp),
        colorFilter = ColorFilter.tint(Color.White)
    )
}
@Composable
fun SoSoFaceIcon(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.neutral),
        contentDescription = "SoSo Day",
        modifier = modifier.size(50.dp),
        colorFilter = ColorFilter.tint(Color.White)
    )
}
@Composable
fun BadFaceIcon(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.sad),
        contentDescription = "Bad Day",
        modifier = modifier.size(50.dp),
        colorFilter = ColorFilter.tint(Color.White)
    )
}
@Composable
fun TerribleFaceIcon(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.crying),
        contentDescription = "Terrible Day",
        modifier = modifier.size(50.dp),
        colorFilter = ColorFilter.tint(Color.White)
    )
}



