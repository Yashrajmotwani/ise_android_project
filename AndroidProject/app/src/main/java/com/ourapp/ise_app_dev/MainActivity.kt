package com.ourapp.ise_app_dev

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        bottomNavigationView.setOnItemSelectedListener { menuItem: MenuItem ->
            // Handle item selection
            when (menuItem.itemId) {
                R.id.nav_positions -> {
                    // Replace the content with the Home Fragment
                    replaceFragment(SearchFragment())
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


        fab.setOnClickListener { view ->
            // Create the BottomSheetDialog
            val bottomSheetDialog = BottomSheetDialog(this)
            val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
            bottomSheetDialog.setContentView(bottomSheetView)

            // Set onClickListeners for the options
            bottomSheetView.findViewById<View>(R.id.view_map_option).setOnClickListener {
                // Navigate to MapActivity
                val mapIntent = Intent(this, MapActivity::class.java)
                startActivity(mapIntent)
                bottomSheetDialog.dismiss() // Close the BottomSheet
            }

            bottomSheetView.findViewById<View>(R.id.saved_project_option).setOnClickListener {
                // Navigate to SavedProjectsActivity
                val savedIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(savedIntent)
                bottomSheetDialog.dismiss() // Close the BottomSheet
            }

            bottomSheetView.findViewById<View>(R.id.view_college_list_option).setOnClickListener {
                // Navigate to CollegeListActivity
                val collegeIntent = Intent(this, CollegeListActivity::class.java)
                startActivity(collegeIntent)
                bottomSheetDialog.dismiss() // Close the BottomSheet
            }

            bottomSheetView.findViewById<View>(R.id.signout).setOnClickListener {
                // Navigate to CollegeListActivity
                val signOutIntent = Intent(this, SignOut::class.java)
                signOutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(signOutIntent)
                bottomSheetDialog.dismiss()

            }

            bottomSheetDialog.show() // Show the BottomSheet
        }

    }

    // Override onBackPressed to handle back button logic
    override fun onBackPressed() {
        // Get the current fragment from the fragment container
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment is SearchFragment) {
            if (backPressedOnce) {
                // If the user presses back again within the time window, exit the app
                super.onBackPressed()
            } else {
                // First back press - show a toast message and set the flag
                backPressedOnce = true
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

                // Reset the flag after 2 seconds
                Handler(Looper.getMainLooper()).postDelayed({
                    backPressedOnce = false
                }, 2000)  // 2000ms = 2 seconds
            }
        } else {
            // If the current fragment is not SearchFragment, replace it with SearchFragment
            val searchFragment = SearchFragment()
            replaceFragment(searchFragment) // This will replace the current fragment with SearchFragment
            // Update the bottom navigation view to highlight the "Projects" item
            bottomNavigationView.selectedItemId = R.id.nav_positions
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        // Add to back stack if it's not the SearchFragment
        if (fragment !is SearchFragment) {
            transaction.addToBackStack(null)
        }
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
