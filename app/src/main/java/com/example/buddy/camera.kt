package com.example.buddy

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class camera : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
    }

    override fun onResume() {
        super.onResume()
        openCamera()
    }

    private fun openCamera() {
        supportFragmentManager.beginTransaction().replace(
            R.id.frame,
            CameraPreviewFragment()
        ).commit()
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame)
        if (fragment !is CameraPreviewFragment) {
            openCamera()
        } else {

            val intent = Intent(this, homepageold::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.dissolve_in, // Slide in from left
                R.anim.dissolve_out // Slide out to right
            )
            startActivity(intent, options.toBundle())
            finish()
        }
    }
}