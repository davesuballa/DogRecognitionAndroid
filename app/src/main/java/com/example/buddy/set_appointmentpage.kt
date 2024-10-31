package com.example.buddy


import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import android.widget.Button
class set_appointmentpage : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_appointmentpage)

        //Back to dog's profile page
        val backbtn : Button = findViewById(R.id.backbtn)

        backbtn.setOnClickListener{
            val intent = Intent (this, dog_profilepageold::class.java )
            startActivity(intent)
        }

        //saves the appointment and shows the record in the dog's profile
        val save_button : Button = findViewById(R.id.button_save)

        save_button.setOnClickListener{
            val intent = Intent (this, dog_profilepageold::class.java )
            startActivity(intent)
        }


    }
}