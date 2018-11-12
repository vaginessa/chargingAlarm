package com.chargingwatts.chargingalarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ImageView

class HowToUseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_how_to_use)
        this.setFinishOnTouchOutside(true);
        val ivCancel : ImageView = findViewById(R.id.iv_cancel)
        ivCancel.setOnClickListener {
            finish()
        }
    }
}
