package com.wit.venues.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.wit.venues.R
import com.wit.venues.views.login.LoginView

class SplashActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    makeFullScreen()
    setContentView(R.layout.activity_splash)

    // Using a handler to delay loading the MainActivity
    Handler().postDelayed({
      // Start activity
      startActivity(Intent(this, LoginView::class.java))
      // Animate the loading of new activity
      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
      // Close this activity
      finish()
    }, 1500)
  }


  private fun makeFullScreen() {
    // Remove Title
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    // Make Fullscreen
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN)
    // Hide the toolbar
    supportActionBar?.hide()
  }
}
