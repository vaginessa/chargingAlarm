package com.chargingwatts.chargingalarm

import AppConstants
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.chargingwatts.chargingalarm.ui.intro.IntroAdapter
import com.chargingwatts.chargingalarm.ui.intro.IntroPageTransformer
import com.chargingwatts.chargingalarm.util.preference.PreferenceHelper
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject


class IntroActivity : BaseActivity() {
    private var mViewPager: ViewPager? = null
    private var mTabLayout: TabLayout? = null

    @Inject
    lateinit var preferenceHelper: PreferenceHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

//        if(!preferenceHelper.getBoolean(AppConstants.IS_FIRST_LAUNCH,true)){
//            val homeIntent = Intent(this, HomeActivity::class.java)
//            homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(homeIntent)
//            return
//        }
        mViewPager = findViewById<View>(R.id.viewpager) as ViewPager

        // Set an Adapter on the ViewPager
        mViewPager?.adapter = IntroAdapter(supportFragmentManager)

        // Set a PageTransformer
        mViewPager?.setPageTransformer(false, IntroPageTransformer())


        val tabLayout = findViewById(R.id.tab_layout) as TabLayout
        tabLayout.setupWithViewPager(mViewPager, true)


    }

}
