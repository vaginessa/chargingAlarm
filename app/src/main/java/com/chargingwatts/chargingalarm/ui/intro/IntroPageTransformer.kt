package com.chargingwatts.chargingalarm.ui.intro

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.chargingwatts.chargingalarm.R



class IntroPageTransformer : ViewPager.PageTransformer {

    override fun transformPage(page: View, position: Float) {

        // Get the page index from the tag. This makes
        // it possible to know which page index you're
        // currently transforming - and that can be used
        // to make some important performance improvements.
        val pagePosition = page.getTag() as Int

        // Here you can do all kinds of stuff, like get the
        // width of the page and perform calculations based
        // on how far the user has swiped the page.
        val pageWidth = page.getWidth()
        val pageWidthTimesPosition = pageWidth * position
        val absPosition = Math.abs(position)

        // Now it's time for the effects
        if (position <= -1.0f || position >= 1.0f) {

            // The page is not visible. This is a good place to stop
            // any potential work / animations you may have running.

        } else if (position == 0.0f) {

            // The page is selected. This is a good time to reset Views
            // after animations as you can't always count on the PageTransformer
            // callbacks to match up perfectly.

        } else {

            // The page is currently being scrolled / swiped. This is
            // a good place to show animations that react to the user's
            // swiping as it provides a good user experience.

            // Let's start by animating the title.
            // We want it to fade as it scrolls out
            val tvTitle = page.findViewById(R.id.tv_title) as TextView
            tvTitle.setAlpha(1.0f - absPosition)

            // Now the description. We also want this one to
            // fade, but the animation should also slowly move
            // down and out of the screen
            val tvDescription = page.findViewById(R.id.tv_description) as TextView
            tvDescription.setTranslationY(-pageWidthTimesPosition / 2f)
            tvDescription.setAlpha(1.0f - absPosition)

            // Now, we want the image to move to the right,
            // i.e. in the opposite direction of the rest of the
            // content while fading out
            val ivHead = page.findViewById(R.id.iv_head) as ImageView

            // We're attempting to create an effect for a View
            // specific to one of the pages in our ViewPager.
            // In other words, we need to check that we're on
            // the correct page and that the View in question
            // isn't null.
//            if (pagePosition == 0 && ivHead != null) {
                ivHead!!.setAlpha(1.0f - absPosition)
                ivHead!!.setTranslationX(-pageWidthTimesPosition * 1.5f)
//            }

            // Finally, it can be useful to know the direction
            // of the user's swipe - if we're entering or exiting.
            // This is quite simple:
            if (position < 0) {
                // Create your out animation here
            } else {
                // Create your in animation here
            }
        }
    }

}