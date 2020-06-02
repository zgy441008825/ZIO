package com.zougy.zio

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.zougy.views.onClickOnSinge
import kotlinx.android.synthetic.main.layout_keyboard.view.*

class KeyboardLayout(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        val view = inflate(context, R.layout.layout_keyboard, this)
        setBackgroundColor(Color.parseColor("#D8DDE0"))
        layoutKeyboardACT0.onClickOnSinge(block = onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACT1).onClickOnShake(onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACT2).onClickOnShake(onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACT3).onClickOnShake(onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACT4).onClickOnShake(onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACT5).onClickOnShake(onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACT6).onClickOnShake(onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACT7).onClickOnShake(onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACT8).onClickOnShake(onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACT9).onClickOnShake(onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACTA).onClickOnShake(onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACTB).onClickOnShake(onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACTC).onClickOnShake(onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACTD).onClickOnShake(onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACTE).onClickOnShake(onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACTF).onClickOnShake(onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACTClear).onClickOnShake(onKeyClick)
//        view.findViewById<Button>(R.id.layoutKeyboardACTDel).onClickOnShake(onKeyClick)
    }

    private val onKeyClick: (view: View) -> Unit = {
        Log.d("MainActivity", "ZLog :$it")
    }

}