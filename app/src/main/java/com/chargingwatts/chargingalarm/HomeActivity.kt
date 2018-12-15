package com.chargingwatts.chargingalarm

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.work.PeriodicWorkRequest
import com.chargingwatts.chargingalarm.util.battery.BATTERY_WORKER_REQUEST_TAG
import com.chargingwatts.chargingalarm.util.battery.BatteryChangeReciever
import com.chargingwatts.chargingalarm.util.battery.PeriodicBatteryUpdater
import com.chargingwatts.chargingalarm.util.preference.PreferenceHelper
import com.google.android.material.navigation.NavigationView
import javax.inject.Inject


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {


    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var periodicBatteryUpdater: PeriodicBatteryUpdater
    @Inject
    lateinit var batteryChangeReciever: BatteryChangeReciever
    @Inject
    lateinit var preferenceHelper: PreferenceHelper


    var navController: NavController? = null
    private var navigationView: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        if(preferenceHelper.getBoolean(AppConstants.IS_FIRST_LAUNCH,true)){
            val introIntent = Intent(this, IntroActivity::class.java)
            introIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(introIntent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish()
            return
        }

        setContentView(R.layout.activity_home)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        navigationView?.setNavigationItemSelectedListener(this)
        navController = Navigation.findNavController(this, R.id.nav_host);
//
        navController?.let {
            setupActionBar(it)
            setupNavigationMenu(it)

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.drawer_item_share -> {
                drawerLayout?.closeDrawers()
                launchApplicationShareIntent()
                true
            }
            R.id.drawer_item_contact_us -> {
                drawerLayout?.closeDrawers()
                launchEmailIntent()
                true
            }

            else -> {
                drawerLayout?.closeDrawers()
                item.onNavDestinationSelected(navController!!)
                true
            }
        }
    }

    private fun launchEmailIntent() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "message/rfc822"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("er.abhishek.luthrat@gmail.com"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject of email")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "body of email")
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchApplicationShareIntent() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "ChargingWatts Full Battery Alarm")
        var extraText = "Protect your Mobile phone battery from the damage of overcharging.\nDownload this app and get notified when battery gets 100% charged.\n"
        extraText = extraText + getString(R.string.playstore_base_url)+ packageName +"\n"
        shareIntent.putExtra(Intent.EXTRA_TEXT, extraText)
        try {
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            Toast.makeText(this, "There are no sharing clients installed.", Toast.LENGTH_SHORT).show()

        }

    }

    override fun onResume() {
        super.onResume()
        periodicBatteryUpdater.startPeriodicBatteryUpdate(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, BATTERY_WORKER_REQUEST_TAG)
        try {
            batteryChangeReciever.registerReciever(this)
        } catch (exception: Exception) {

        }
    }

    override fun onPause() {
        super.onPause()
        try {
            batteryChangeReciever.unregisterReciever(this)

        } catch (exception: Exception) {

        }
    }


    private fun setupNavigationMenu(navController: NavController) {
//        // In split screen mode, you can drag this view out from the left
//        // This does NOT modify the actionbar
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
//        NavigationUI.setupWithNavController(navigationView!!, navController)
        navigationView.setNavigationItemSelectedListener(this)

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
