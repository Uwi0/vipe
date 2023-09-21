package com.kakapo.vipe

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kakapo.vipe.utils.windowViewFactory
import kotlinx.coroutines.delay

class CharacterWindow(context: Context) {

    private var view: View
    private lateinit var layoutParams: WindowManager.LayoutParams
    private var windowManager: WindowManager
    private var directionChar: Direction = Direction.Right

    init {
        initializeLayoutParams()
        view = contentView(context)
        layoutParams.gravity = Gravity.BOTTOM
        windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
    }

    private fun initializeLayoutParams() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams = WindowManager.LayoutParams(
                CHARACTER_SIZE_PX,
                CHARACTER_SIZE_PX,
                0,
                0,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }
    }

    private fun contentView(context: Context) = windowViewFactory(context) {
        Box(modifier = Modifier.size(CHARACTER_SIZE_DP.dp)) {
            Image(
                modifier = Modifier
                    .size(64.dp, 96.dp),
                painter = painterResource(id = R.drawable.img_mumei_char),
                contentDescription = ""
            )
        }
    }


    fun open() {
        try {
            if (view.windowToken == null) {
                if (view.parent == null) {
                    windowManager.addView(view, layoutParams)
                }
            }
        } catch (e: Exception) {
            Log.d("Window", "error create window${e.message}")
        }
    }

    suspend fun updatePosition() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        while (true){
            if (layoutParams.x <= (screenWidth/2) && directionChar == Direction.Right){
                layoutParams.x += 1
                if (layoutParams.x == (screenWidth/2)){
                    directionChar = Direction.Left
                }
            }
            if (layoutParams.x > -(screenWidth/2) && directionChar == Direction.Left){
                layoutParams.x -= 1
                if (layoutParams.x == -(screenWidth/2)){
                    directionChar = Direction.Right
                }
            }
            windowManager.updateViewLayout(view, layoutParams)
            delay(1_0)
        }
    }
}

enum class Direction{
    Left,
    Right
}