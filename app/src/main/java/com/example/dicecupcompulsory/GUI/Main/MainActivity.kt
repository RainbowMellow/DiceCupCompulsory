package com.example.dicecupcompulsory.GUI.Main

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.dicecupcompulsory.GUI.History.HistoryActivity
import com.example.dicecupcompulsory.R
import java.util.*
import kotlin.collections.ArrayList
import com.example.dicecupcompulsory.Model.BEDiceRoll
import com.example.dicecupcompulsory.Model.BEPlayer
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    val HISTORY_WAS_CLEARED = 2
    val GET_HISTORY = 1

    var history = ArrayList<BEDiceRoll>()
    val dicePictures = arrayOf(0, R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
            R.drawable.dice4, R.drawable.dice5, R.drawable.dice6)
    val mGenerator = Random()

    var totalDices: Int = 2
    val firstList = arrayListOf(1,1,1,1,1,1,1)
    var currentDices = ArrayList<Int>()
    var hasPlayers : Boolean = false
    var playerOne = BEPlayer("Player1", "color")
    var playerTwo = BEPlayer("Player2", "color")
    var currentPlayer = playerOne
    var isInUse : Boolean = false


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("history", history)
        outState.putInt("totalDices", totalDices)
        outState.putIntegerArrayList("currentDices", currentDices)
        outState.putSerializable("currentPlayer", currentPlayer)

        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            val totalDicesNew = savedInstanceState.getInt("totalDices")
            val historyList = savedInstanceState.getSerializable("history") as ArrayList<BEDiceRoll>
            val currentDicesList = savedInstanceState.getIntegerArrayList("currentDices") as ArrayList<Int>
            val current = savedInstanceState.getSerializable("currentPlayer") as BEPlayer

            isInUse = true

            history = historyList
            totalDices = totalDicesNew
            currentDices = currentDicesList
            currentPlayer = current


            //Makes the dices show the dices that were last rolled.
            createDices(totalDices, currentDices)

            val playerText = "Your turn " + currentPlayer.name + "!"
            tvPlayerText.text = playerText

            updateDiceCount()
        }
        else
        {
            isInUse = false

            //Sets the first dices
            for (i in 1..totalDices)
            {
                currentDices.add(1)
            }

            createDices(totalDices, firstList)

            updateDiceCount()
        }

        if (intent.extras != null) {
            val extras: Bundle = intent.extras!!
            val players = extras.getBoolean("hasPlayers")

            hasPlayers = players

            if(hasPlayers)
            {
                val firstPlayer = extras.getSerializable("playerOne") as BEPlayer
                val secondPlayer = extras.getSerializable("playerTwo") as BEPlayer
                val playerOneStarts = extras.getBoolean("playerOneStarts")

                playerOne = firstPlayer
                playerTwo = secondPlayer

                if(!isInUse)
                {
                    if(playerOneStarts)
                    {
                        currentPlayer = playerOne
                    }
                    else if(!playerOneStarts)
                    {
                        currentPlayer = playerTwo
                    }
                }
                else if(!isInUse)
                {

                }

                val playerText = "Your turn " + currentPlayer.name + "!"
                tvPlayerText.text = playerText

                if(hasPlayers)
                {
                    tvPlayerText.setTextColor(Color.parseColor(currentPlayer.color))
                }
            }
            else
            {
                tvPlayerText.visibility = View.INVISIBLE
            }
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

        if(!hasPlayers)
        {
            //Adds the roll to the history
            val diceRoll = BEDiceRoll(null, listOfDiceRolls, Calendar.getInstance().time)
            history.add(diceRoll)
        }
        else if(hasPlayers)
        {
            //Adds the roll to the history
            val diceRoll = BEDiceRoll(currentPlayer, listOfDiceRolls, Calendar.getInstance().time)
            history.add(diceRoll)
        }


        if(currentPlayer.name == playerOne.name)
        {
            currentPlayer = playerTwo
        }
        else if(currentPlayer.name == playerTwo.name)
        {
            currentPlayer = playerOne
        }

        val playerText = "Your turn " + currentPlayer.name + "!"
        tvPlayerText.text = playerText

        if(hasPlayers)
        {
            tvPlayerText.setTextColor(Color.parseColor(currentPlayer.color))
        }
    }

    //Sends the user and history list to the HistoryActivity
    fun onClickHistory(view: View) {
        val intent = Intent(this, HistoryActivity::class.java)
        intent.putExtra("history", history as Serializable)
        intent.putExtra("hasPlayers", hasPlayers)
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

    fun onClickBack(view: View) {
        finish()
    }
}