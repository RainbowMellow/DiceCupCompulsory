package com.example.dicecupcompulsory.GUI.Start

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.dicecupcompulsory.GUI.History.HistoryActivity
import com.example.dicecupcompulsory.GUI.Main.MainActivity
import com.example.dicecupcompulsory.Model.BEPlayer
import com.example.dicecupcompulsory.R
import kotlinx.android.synthetic.main.start_layout.*
import java.io.Serializable

class StartActivity : AppCompatActivity(){

    var hasPlayers : Boolean = false
    var playerOneStarts : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_layout)

        rbNoPlayers.isChecked = true
        hasPlayers = false
        rbPlayerOne.isChecked = true
        playerOneStarts = true
    }

    fun onClickPlay(view: View) {

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("hasPlayers", hasPlayers)

        if(hasPlayers)
        {
            if(rbPlayerOne.isChecked)
            {
                playerOneStarts = true
            }
            else if(!rbPlayerOne.isChecked)
            {
                playerOneStarts = false
            }


            var nameOne = ""
            var nameTwo = ""

            if(!playerOneName.text.isEmpty())
            {
                nameOne = playerOneName.text.toString()
            }
            else
            {
                nameOne = "Player 1"
            }
            val playerOne = BEPlayer(nameOne, "#6F87FF")

            if(!playerTwoName.text.isEmpty())
            {
                nameTwo = playerTwoName.text.toString()
            }
            else
            {
                nameTwo = "Player 2"
            }
            val playerTwo = BEPlayer(nameTwo, "#8000FF")

            intent.putExtra("playerOne", playerOne as Serializable)
            intent.putExtra("playerTwo", playerTwo as Serializable)
            intent.putExtra("playerOneStarts", playerOneStarts)
        }

        startActivity(intent)
    }

    fun onClickWithPlayers(view: View) {
        llPlayerInfo.visibility = View.VISIBLE
        hasPlayers = true

        val layout = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        layout.addRule(RelativeLayout.BELOW, llPlayersOrNot.id)
        layout.addRule(RelativeLayout.CENTER_HORIZONTAL)
        layout.topMargin = 50
        layout.bottomMargin = 20
        llPlayerInfo.layoutParams = layout

        val btnLayout = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        btnLayout.addRule(RelativeLayout.CENTER_HORIZONTAL)
        btnLayout.addRule(RelativeLayout.BELOW, llPlayerInfo.id)
        btnPlay.layoutParams = btnLayout
    }

    fun onClickWithoutPlayers(view: View) {
        llPlayerInfo.visibility = View.INVISIBLE
        hasPlayers = false

        val layout = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        layout.addRule(RelativeLayout.BELOW, btnPlay.id)
        layout.addRule(RelativeLayout.CENTER_HORIZONTAL)
        llPlayerInfo.layoutParams = layout

        val btnLayout = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        btnLayout.addRule(RelativeLayout.BELOW, llPlayersOrNot.id)
        btnLayout.addRule(RelativeLayout.CENTER_HORIZONTAL)
        layout.topMargin = 20
        btnPlay.layoutParams = btnLayout
    }

}