package com.chargingwatts.chargingalarm

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.navigation.NavigationView
import javax.inject.Inject

class HomeActivity : BaseActivity() {
    @Inject
    lateinit var appExecutors: AppExecutors


    var navController: NavController? = null
    private var drawerLayout: DrawerLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)


        navController = Navigation.findNavController(this, R.id.nav_host);
//
        navController?.let {
            setupActionBar(it)
            setupNavigationMenu(it)
        }
//        navController?.navigate(R.id.battery_detail_fragment)
    }


    private fun setupNavigationMenu(navController: NavController) {
//        // In split screen mode, you can drag this view out from the left
//        // This does NOT modify the actionbar
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        NavigationUI.setupWithNavController(sideNavView, navController)

    }


    private fun setupActionBar(navController: NavController) {
//        // This allows NavigationUI to decide what label to show in the action bar
//        // And, since we are passing in drawerLayout, it will also determine whether to
//        // show the up arrow or drawer menu icon


        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }


    override fun onSupportNavigateUp(): Boolean {
        navController?.let {
            return NavigationUI.navigateUp(drawerLayout, it)
                    || super.onSupportNavigateUp()
        }
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        navController?.let {
            return item.onNavDestinationSelected(it) || super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }
}
