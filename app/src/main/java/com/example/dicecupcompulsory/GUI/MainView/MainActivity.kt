package com.example.dicecupcompulsory.GUI.MainView

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.dicecupcompulsory.GUI.NormalDiceCup.Main.DiceRollActivity
import com.example.dicecupcompulsory.GUI.YatzyDiceCup.Yatzy.YahtzeeActivity
import com.example.dicecupcompulsory.R
import kotlinx.android.synthetic.main.main_layout.*

class MainActivity : AppCompatActivity() {

    var isFiveChecked = true
    var hasClickedYahtzee = false

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("isFiveChecked", isFiveChecked)
        outState.putBoolean("hasClickedYahtzee", hasClickedYahtzee)

        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        if (savedInstanceState != null) {
            val isFirstChecked = savedInstanceState.getBoolean("isFiveChecked")
            val hasClicked = savedInstanceState.getBoolean("hasClickedYahtzee")

            if(hasClicked)
            {
                llYahtzeeOptions.visibility = View.VISIBLE
                hasClickedYahtzee = true
            }

            if(isFirstChecked)
            {
                rbFiveDices.isChecked = true
                isFiveChecked = true
            }
            else
            {
                rbSixDices.isChecked = true
                isFiveChecked = false
            }
        }
        else
        {
            rbFiveDices.isChecked = true
        }
    }

    //Sends the user to the Normal Dice Cup
    fun onClickNormalDiceCup(view: View) {
        val intent = Intent(this, DiceRollActivity::class.java)
        startActivity(intent)
    }

    //Shows the Yahtzee options
    fun onClickYahtzeeDiceCup(view: View) {
        llYahtzeeOptions.visibility = View.VISIBLE
        hasClickedYahtzee = true
    }

    //Takes the options and sends them and the user to the YahtzeeActivity
    fun onClickPlay(view: View) {
        val intent = Intent(this, YahtzeeActivity::class.java)

        if(rbFiveDices.isChecked)
        {
            intent.putExtra("diceAmount", 5)
            isFiveChecked = true
        }
        if(rbSixDices.isChecked)
        {
            intent.putExtra("diceAmount", 6)
            isFiveChecked = false
        }

        startActivity(intent)

        llYahtzeeOptions.visibility = View.INVISIBLE
    }

}