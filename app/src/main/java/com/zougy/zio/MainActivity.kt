package com.zougy.zio

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.zougy.views.onClickOnShake
import com.zougy.views.onClickOnSinge
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        acMainTvMsg.onClickOnSinge { onClick }
    }

    private val onClick: (view: View) -> Unit = {
        Log.d("MainActivity", "ZLog onCreate ${R.id.acMainTvMsg}")
        Log.d("MainActivity", "ZLog onCreate ${it.id}")
    }
}
