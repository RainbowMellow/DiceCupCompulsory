package com.example.dicecupcompulsory.GUI.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.dicecupcompulsory.GUI.History.HistoryActivity
import com.example.dicecupcompulsory.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import com.example.dicecupcompulsory.Model.BEDiceRoll
import java.io.Serializable

class MainActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            val totalDicesNew = savedInstanceState.getInt("totalDices")
            val historyList = savedInstanceState.getSerializable("history") as ArrayList<BEDiceRoll>
            val currentDicesList = savedInstanceState.getIntegerArrayList("currentDices") as ArrayList<Int>

            history = historyList
            totalDices = totalDicesNew
            currentDices = currentDicesList

            println(history.size)

            if (history.isNotEmpty()) {
                createDices(totalDices, history[history.size-1].diceNumber)
            }
            else {
                createDices(totalDices, currentDices)
            }

            updateDiceCount()

        }
        else {
            for (i in 1..totalDices)
            {
                currentDices.add(1)
            }
            createDices(totalDices, firstList)

            updateDiceCount()
        }
    }

    private fun createDices(dicesToBeAdded: Int, dicePicture: ArrayList<Int>) {

        for (i in 1..dicesToBeAdded) {
            val imgView = ImageView(this)
            val lp = LinearLayout.LayoutParams(300, 300)
            lp.setMargins(5,5,5,5)

            imgView.layoutParams = lp

            imgView.setBackgroundResource(dicePictures[dicePicture[i-1]])
            //imgView.scaleType = ImageView.ScaleType.CENTER
            glDices.addView(imgView)
        }
    }

    fun updateDiceCount() {
        tvDiceCount.text = totalDices.toString()
    }

    fun onClickMoreDices(view: View) {
        if(totalDices != 6)
        {
            totalDices++
            createDices(1, firstList)
            currentDices.add(1)
        }

        updateDiceCount()
    }

    fun onClickLessDices(view: View) {
        if(totalDices != 1)
        {
            totalDices--
            glDices.removeViewAt(totalDices)

            currentDices.removeAt(currentDices.size-1)
        }

        updateDiceCount()
    }

    fun onClickRoll(view: View) {
        var listOfDiceRolls = ArrayList<Int>()
        currentDices.clear()

        val diceNumbersInGrid = totalDices-1

        for (i in 0..diceNumbersInGrid)
        {
            val randomNumber = mGenerator.nextInt(6)+1
            println(randomNumber)

            val currentImgView = glDices.getChildAt(i)

            with (currentImgView as ImageView)
            {
                currentImgView.setBackgroundResource(dicePictures[randomNumber])
            }
            listOfDiceRolls.add(randomNumber)
            currentDices.add(randomNumber)
        }

        val diceRoll = BEDiceRoll(listOfDiceRolls, Calendar.getInstance().time)
        history.add(diceRoll)

        history.forEach{ d ->
            println(d.date)
            println(d.diceNumber)
        }
    }

    fun onClickHistory(view: View) {
        val intent = Intent(this, HistoryActivity::class.java)
        intent.putExtra("history", history as Serializable)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1)
        {
            if(resultCode == 2)
            {
                history.clear()
                glDices.removeAllViews()
                createDices(totalDices, currentDices)

                println("History cleared")
            }
        }
    }
}