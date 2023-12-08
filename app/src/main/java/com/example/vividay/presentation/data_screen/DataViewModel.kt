package com.example.vividay.presentation.data_screen


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.vividay.models.Day
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.vividay.sealed.DataState

class DataViewModel: ViewModel(){
    val response: MutableState<DataState> = mutableStateOf(DataState.Empty)
    var days: List<Day> by mutableStateOf(emptyList())

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

                    // Update the days property with the fetched data
                    days = tempList

                    // Print the contents of the days list after fetching data
                    println("Days list after fetching data: $days")
                }

                override fun onCancelled(error: DatabaseError) {
                    response.value = DataState.Failure(error.message)
                }
            })
    }

    fun getSpecificDay(dateTime: String?): Day {
        val formattedDateTime = dateTime?.removeSurrounding("\"") ?: ""
        // Print the contents of the days list
        println("Days list: $days") // it returns empty
        return days.find { it.dateTime.toString() == formattedDateTime }
            ?: throw NoSuchElementException("Day with dateTime $dateTime not found")
    }
}