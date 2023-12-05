package com.example.vividay.presentation.data_screen


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.vividay.models.Day
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.example.vividay.sealed.DataState

class DataViewModel: ViewModel(){
    val response: MutableState<DataState> = mutableStateOf(DataState.Empty)

    init {
        fetchDataFromFirebase()
    }

    fun fetchDataFromFirebase() {
        val tempList = mutableListOf<Day>()
        response.value = DataState.Loading
        FirebaseDatabase.getInstance().getReference("Day")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(DataSnap in snapshot.children) {
                        val dayItem = DataSnap.getValue(Day::class.java)
                        if (dayItem != null)
                            tempList.add(dayItem)
                    }
                    response.value = DataState.Success(tempList)
                }

                override fun onCancelled(error: DatabaseError) {
                    response.value = DataState.Failure(error.message)
                }
            })
    }
}