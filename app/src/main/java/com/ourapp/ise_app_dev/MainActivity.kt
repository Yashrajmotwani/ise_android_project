package com.ourapp.ise_app_dev

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ourapp.ise_app_dev.ui.theme.Ise_app_devTheme
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem: MenuItem ->
            // Handle item selection
            when (menuItem.itemId) {
                R.id.nav_positions -> {
                    // Replace the content with the Home Fragment
                    replaceFragment(PositionFragment())
                    highlightMenuItem(menuItem)
                    true
                }
                R.id.nav_teachers -> {
                    // Replace the content with the Search Fragment
                    replaceFragment(TeacherFragment())
                    highlightMenuItem(menuItem)
                    true
                }

                else -> false
            }
        }

        // Set the default selected item
        bottomNavigationView.selectedItemId = R.id.nav_positions


    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    private fun highlightMenuItem(item: MenuItem) {
        val menuView: View = bottomNavigationView.findViewById(item.itemId)
        val scaleX = ObjectAnimator.ofFloat(menuView, "scaleX", 1.2f)
        val scaleY = ObjectAnimator.ofFloat(menuView, "scaleY", 1.2f)

        scaleX.duration = 150
        scaleY.duration = 150

        scaleX.start()
        scaleY.start()

        // Optionally, reset the scale after a short delay
        Handler(Looper.getMainLooper()).postDelayed({
            ObjectAnimator.ofFloat(menuView, "scaleX", 1f).start()
            ObjectAnimator.ofFloat(menuView, "scaleY", 1f).start()
        }, 200)
    }


}
