package com.example.androidstorage

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BasicsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basics)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun showSingleChoiceDialog(){
        val options = arrayOf("First Item","Second Item","Third Item")
        val single = AlertDialog
            .Builder(this)
            .setTitle("Show Data")
            .setSingleChoiceItems(options,0){dalogInterface, i ->

            }.setPositiveButton("Accept"){_,_->

            }
            .setNegativeButton("Close"){_,_->

            }.create()
        single.show()
    }

    private fun showMultipleChoiceDialog(){
        val options = arrayOf("First Item","Second Item","Third Item")
        val single = AlertDialog
            .Builder(this)
            .setTitle("Show Data")
            .setMultiChoiceItems(options, booleanArrayOf(false,false,false)){ _, i,isChecked ->

            }.setPositiveButton("Accept"){_,_->

            }
            .setNegativeButton("Close"){_,_->

            }.create()
        single.show()
    }
}