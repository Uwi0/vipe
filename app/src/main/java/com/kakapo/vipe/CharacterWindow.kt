package com.kakapo.vipe

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.kakapo.vipe.utils.MyLifecycleOwner
import kotlinx.coroutines.delay

class CharacterWindow(context: Context) {

    private var view: View
    private lateinit var layoutParams: WindowManager.LayoutParams
    private var windowManager: WindowManager

    init {
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

        view = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            val lifecycleOwner = MyLifecycleOwner()
            val viewModelStore = ViewModelStore()
            lifecycleOwner.performRestore(null)
            lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            setViewTreeLifecycleOwner(lifecycleOwner)
            setViewTreeSavedStateRegistryOwner(lifecycleOwner)
            val viewModelStoreOwner = object : ViewModelStoreOwner {
                override val viewModelStore: ViewModelStore
                    get() = viewModelStore
            }
            setViewTreeViewModelStoreOwner(viewModelStoreOwner)
            setContent {
                Box(modifier = Modifier.size(CHARACTER_SIZE_DP.dp)) {
                    Image(
                        modifier = Modifier
                            .size(64.dp, 96.dp),
                        painter = painterResource(id = R.drawable.img_mumei_char),
                        contentDescription = ""
                    )
                }
            }
        }
        layoutParams.gravity = Gravity.BOTTOM
        windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
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

    suspend fun updatePosition(){
        while (true){
            layoutParams.x += 1
            windowManager.updateViewLayout(view, layoutParams)
            delay(1_00)
        }
    }
}