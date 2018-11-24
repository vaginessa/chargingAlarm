package com.chargingwatts.chargingalarm

import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class HowToUseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_how_to_use)
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        this.setFinishOnTouchOutside(true);
        val ivCancel : ImageView = findViewById(R.id.iv_cancel)
        ivCancel.setOnClickListener {
            finish()
        }
    }
}
