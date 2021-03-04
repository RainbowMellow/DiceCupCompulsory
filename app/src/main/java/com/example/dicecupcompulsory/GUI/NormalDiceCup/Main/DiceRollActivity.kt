package com.example.dicecupcompulsory.GUI.NormalDiceCup.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.dicecupcompulsory.GUI.NormalDiceCup.History.HistoryActivity
import com.example.dicecupcompulsory.R
import java.util.*
import kotlin.collections.ArrayList
import com.example.dicecupcompulsory.Model.BEDiceRoll
import kotlinx.android.synthetic.main.diceroll_layout.*
import java.io.Serializable

class DiceRollActivity : AppCompatActivity() {

    val HISTORY_WAS_CLEARED = 2
    val GET_HISTORY = 1

    var history = ArrayList<BEDiceRoll>()
    val dicePictures = arrayOf(0, R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
            R.drawable.dice4, R.drawable.dice5, R.drawable.dice6)
    val mGenerator = Random()

    var totalDices: Int = 2
    val firstList = arrayListOf(1,1,1,1,1,1,1)
    var currentDices = ArrayList<Int>()


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("history", history)
        outState.putInt("totalDices", totalDices)
        outState.putIntegerArrayList("currentDices", currentDices)

        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diceroll_layout)

        if (savedInstanceState != null) {
            val totalDicesNew = savedInstanceState.getInt("totalDices")
            val historyList = savedInstanceState.getSerializable("history") as ArrayList<BEDiceRoll>
            val currentDicesList = savedInstanceState.getIntegerArrayList("currentDices") as ArrayList<Int>

            history = historyList
            totalDices = totalDicesNew
            currentDices = currentDicesList

            //Makes the dices show the dices that were last rolled.
            createDices(totalDices, currentDices)

            updateDiceCount()
        }
        else {
            //Sets the first dices
            for (i in 1..totalDices)
            {
                currentDices.add(1)
            }

            createDices(totalDices, firstList)

            updateDiceCount()
        }
    }

    //Takes an amount and a list of indexes for the picture list and adds them to the main gridlayout
    private fun createDices(dicesToBeAdded: Int, dicePicture: ArrayList<Int>) {

        for (i in 1..dicesToBeAdded) {
            val imgView = ImageView(this)

            val lp = LinearLayout.LayoutParams(300, 300)
            lp.setMargins(5,5,5,5)
            imgView.layoutParams = lp

            imgView.setBackgroundResource(dicePictures[dicePicture[i-1]])

            glDices.addView(imgView)
        }
    }

    //Updates the counter on screen with the new amount of dices
    fun updateDiceCount() {
        tvDiceCount.text = totalDices.toString()
    }

    //Adds a dice to the gridLayout if the limit has not been reached
    fun onClickMoreDices(view: View) {
        if(totalDices != 6)
        {
            totalDices++
            createDices(1, firstList)
            currentDices.add(1)
        }

        updateDiceCount()
    }

    //Removes a dice from the gridLayout if the limit has not been reached
    fun onClickLessDices(view: View) {
        if(totalDices != 1)
        {
            totalDices--
            glDices.removeViewAt(totalDices)

            currentDices.removeAt(currentDices.size-1)
        }

        updateDiceCount()
    }

    //Gets a random number for each dice, and changes the pictures of the dices to reflect the numbers
    fun onClickRoll(view: View) {
        currentDices.clear()

        val listOfDiceRolls = ArrayList<Int>()
        val diceNumbersInGrid = totalDices-1

        for (i in 0..diceNumbersInGrid)
        {
            val randomNumber = mGenerator.nextInt(6)+1

            val currentImgView = glDices.getChildAt(i)

            with (currentImgView as ImageView)
            {
                currentImgView.setBackgroundResource(dicePictures[randomNumber])
            }

            listOfDiceRolls.add(randomNumber)
            currentDices.add(randomNumber)
        }

        //Adds the roll to the history
        val diceRoll = BEDiceRoll(listOfDiceRolls, Calendar.getInstance().time)
        history.add(diceRoll)
    }

    //Sends the user and history list to the HistoryActivity
    fun onClickHistory(view: View) {
        val intent = Intent(this, HistoryActivity::class.java)
        intent.putExtra("history", history as Serializable)
        startActivityForResult(intent, GET_HISTORY)
    }

    //Checks if the history list has been cleared in the HistoryActivity
    //If it is cleared, make sure the dices look like the last rolled dices
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GET_HISTORY)
        {
            if(resultCode == HISTORY_WAS_CLEARED)
            {
                history.clear()
                glDices.removeAllViews()
                createDices(totalDices, currentDices)

                println("History cleared")
            }
        }
    }

    //Gets the user back to the main view
    fun onClickBack(view: View) {
        finish()
    }

}