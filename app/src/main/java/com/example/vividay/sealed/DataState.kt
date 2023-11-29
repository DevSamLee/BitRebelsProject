package com.example.vividay.sealed

import com.example.vividay.models.Day

sealed class DataState {
    class Success(val data:MutableList<Day>): DataState()
    class Failure(val message:String): DataState()
    object Loading : DataState()
    object Empty : DataState()
}