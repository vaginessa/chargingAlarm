package com.chargingwatts.chargingalarm.ui.intro

import AppConstants
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chargingwatts.chargingalarm.BaseFragment
import com.chargingwatts.chargingalarm.HomeActivity
import com.chargingwatts.chargingalarm.R
import com.chargingwatts.chargingalarm.util.preference.PreferenceHelper
import javax.inject.Inject


class IntroFragment : BaseFragment() {
    @Inject
    lateinit var preferenceHelper: PreferenceHelper


    private var mPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!getArguments()!!.containsKey(PAGE))
            throw RuntimeException("Fragment must contain a \"$PAGE\" argument!")
        mPage = getArguments()!!.getInt(PAGE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Select a layout based on the current page
        val layoutResId: Int
        when (mPage) {
            0 -> layoutResId = R.layout.fragment_intro_1
            1 -> layoutResId = R.layout.fragment_intro_2
            2 -> layoutResId = R.layout.fragment_intro_3
            else -> layoutResId = R.layout.fragment_intro_3
        }

        // Inflate the layout resource file
        val view = getActivity()!!.getLayoutInflater().inflate(layoutResId, container, false)

        // Set the current page index as the View's tag (useful in the PageTransformer)
        view.setTag(mPage)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lTvDone: TextView? = view.findViewById(R.id.tv_done)
        lTvDone?.setOnClickListener {
            preferenceHelper.putBoolean(AppConstants.IS_FIRST_LAUNCH, false)
            val homeIntent = Intent(context, HomeActivity::class.java)
            homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(homeIntent)
            if(activity != null){
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        }

    }

    companion object {

        private val PAGE = "page"

        fun newInstance(page: Int): IntroFragment {
            val frag = IntroFragment()
            val b = Bundle()
            b.putInt(PAGE, page)
            frag.setArguments(b)
            return frag
        }
    }

}