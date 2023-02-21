package com.example.standaloneapp1

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.example.standaloneapp1.databinding.ActivityMainBinding
import com.google.android.material.slider.Slider
import kotlin.math.pow

class ProfileActivity : AppCompatActivity(){
    private var mFullName: String? = null
    private var mTvName: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //Get the intent that created this activity.
        //val receivedIntent: Intent? = intent
        mFullName = intent.getStringExtra("mFullNameReceived")

        mTvName = findViewById(R.id.tv_name)

        mTvName!!.text = mFullName
    }

}
