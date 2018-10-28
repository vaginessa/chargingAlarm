package com.chargingwatts.chargingalarm.util.image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG


object ImageUtil {

    fun textAsBitmap(text: String, textSize: Float, textColor: Int): Bitmap {
        val paint = Paint(ANTI_ALIAS_FLAG)
        paint.setTextSize(textSize)
        paint.setColor(textColor)
        paint.setTextAlign(Paint.Align.LEFT)
        val baseline = -paint.ascent() // ascent() is negative
        val width = (paint.measureText(text) + 0.5f).toInt() // round
        val height = (baseline + paint.descent() + 0.5f).toInt()
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawText(text, 0f, baseline, paint)
        return image
    }
}