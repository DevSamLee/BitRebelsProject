package com.example.vividay.presentation.data_screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vividay.models.Day
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DataViewModel: ViewModel(){
    val state = mutableStateOf(Day())

    private fun getData(){
        viewModelScope.launch {
            state.value = getDataFromFireStore()
        }
    }
}

suspend fun getDataFromFireStore(): Day {
    val db = FirebaseFirestore.getInstance()
    var day = Day()

    try {
        db.collection("Day").get().await().map {
        val result = it.toObject(Day::class.java)
            day = result
        }
    } catch (e: FirebaseFirestoreException) {
        Log.d("error", "getDataFromFireStore: $e")
    }

    return day
}