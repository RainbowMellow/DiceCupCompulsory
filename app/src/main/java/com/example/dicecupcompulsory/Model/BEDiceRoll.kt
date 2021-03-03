package com.example.dicecupcompulsory.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class BEDiceRoll ( val diceNumber: ArrayList<Int>, val date: Date ) : Serializable{
}