package com.example.buddy
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val splashLogoImageView = findViewById<ImageView>(R.id.splash_logo)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Load animations
        val blurInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val blurOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        // Start blur in animation
        splashLogoImageView.startAnimation(blurInAnimation)

        // Delay for the duration of the blur in animation
        Handler(Looper.getMainLooper()).postDelayed({
            // Check current user authentication state
            val currentUser = auth.currentUser

            if (currentUser != null && currentUser.isEmailVerified) {
                // User is logged in and email is verified
                startHomepageActivity()
            } else {
                // User is not logged in or email is not verified
                startMainActivity()
            }
        }, SPLASH_DELAY_AFTER_BLUR_IN)
    }

    private fun startHomepageActivity() {
        val intent = Intent(this, homepageold::class.java)
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.dissolve_in, // Animation for the new activity
            R.anim.dissolve_out // No animation for the current activity
        )

        // Start the activity with the specified animation options
        startActivity(intent, options.toBundle())
        finish()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.dissolve_in, // Animation for the new activity
            R.anim.dissolve_out // No animation for the current activity
        )

        // Start the activity with the specified animation options
        startActivity(intent, options.toBundle())
        finish()
    }

    companion object {
        private const val SPLASH_DELAY_AFTER_BLUR_IN = 1000L // Delay after blur in animation (0.5 seconds)
    }
}
