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
import com.google.android.material.slider.Slider

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var mFullName: String? = null
    private var mETFirstName: EditText? = null
    private var mETMiddleName: EditText? = null
    private var mETLastName: EditText? = null
    private var mSubmitButton: Button? = null

    private var mCameraButton: Button? = null
    //create the variable for the imageview that holds the profile pic
    private var mIvPic: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get the buttons
        mSubmitButton = findViewById(R.id.button_profile)
        mCameraButton = findViewById(R.id.button_camera)

        mETFirstName = findViewById(R.id.et_firstName)
        mETMiddleName = findViewById(R.id.et_middleName)
        mETLastName = findViewById(R.id.et_lastName)
        //Say that this class itself contains the listener.
        mSubmitButton!!.setOnClickListener(this)
        mCameraButton!!.setOnClickListener(this)

        //If there is saved data stored in database/local storage, get it.
        loadStoredData()
        //When elements are changed, save them to local storage
        setupListeners()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_camera -> {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try{
                    cameraActivity.launch(cameraIntent)
                }catch(ex: ActivityNotFoundException){
                    //do error handling here
                }
            }
            R.id.button_profile -> {
                var ready = true
                // handle input first
                // get name and do error checking
                mFullName = mETFirstName!!.text.toString() + " " + mETLastName!!.text.toString() + " is logged in!"
                if (mFullName!!.isNullOrBlank()) {
                    Toast.makeText(this@MainActivity, "Enter your first and last name!", Toast.LENGTH_SHORT).show()
                    ready = false
                }
                // remove leading spaces or tabs
                mFullName = mFullName!!.replace("^\\s+".toRegex(), "")

                // only start new intent if all fields are filled out properly
                if (ready) {
                    //Reward them for submitting their names
                    Toast.makeText(this@MainActivity, "Good job!", Toast.LENGTH_SHORT).show()
                    //Start an activity and pass the EditText string to it.
                    val messageIntent = Intent(this, ProfileActivity::class.java)
                    messageIntent.putExtra("mFullNameReceived", mFullName) //For when we need to send info to a new activity
                    this.startActivity(messageIntent)
                }
            }
        }
    }

    private fun setupListeners() {
        mETFirstName!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                saveData("firstName_data", s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        mETMiddleName!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                saveData("middleName_data", s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        mETLastName!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                saveData("lastName_data", s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })


    }

    private fun loadStoredData(){
        val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        if(sharedPreferences.contains("firstName_data")){
            mETFirstName!!.setText(loadData("firstName_data", "").toString())
        }
        if(sharedPreferences.contains("middleName_data")){
            mETMiddleName!!.setText(loadData("middleName_data", "").toString())
        }
        if(sharedPreferences.contains("lastName_data")){
            mETLastName!!.setText(loadData("lastName_data", "").toString())
        }
    }
    private fun saveData(key: String, value: Any) {
        val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is String -> editor.putString(key, value)
            else -> throw UnsupportedOperationException("Not yet implemented")

        }
        editor.apply()
    }

    private fun loadData(key: String, Value: Any): Any? {

        val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        return when (Value) {
            is Boolean -> sharedPreferences.getBoolean(key, true)
            is Float -> sharedPreferences.getFloat(key,0.0f)
            is Int -> sharedPreferences.getInt(key, 0)
            is Long -> sharedPreferences.getLong(key, 0)
            is String -> sharedPreferences.getString(key, "")
            else -> throw UnsupportedOperationException("Not yet implemented")
        }

    }

    private val cameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK){
            mIvPic = findViewById(R.id.iv_pic)

            val thumbnailImage = result.data!!.getParcelableExtra<Bitmap>("data")
            mIvPic!!.setImageBitmap(thumbnailImage)
        }
    }
}