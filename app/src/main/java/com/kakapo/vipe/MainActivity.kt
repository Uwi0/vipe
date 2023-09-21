package com.kakapo.vipe

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kakapo.designsystem.theme.VipeTheme
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp
            var xOffSet by remember { mutableStateOf(1.dp)}
            LaunchedEffect(key1 = Unit){
                while (true){
                    xOffSet += 1.dp
                    delay(100)
                }
            }
            VipeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            modifier = Modifier.height(48.dp),
                            onClick = {
                                checkOverlayPermission()
                                startService()
                            }
                        ) {
                            Text(text = "Start Vipe")
                        }
                        Spacer(modifier = Modifier.size(24.dp))
                        Button(
                            modifier = Modifier.height(48.dp),
                            onClick = { stopCharacterService() }
                        ) {
                            Text(text = "Stop Vipe")
                        }
                        Image(
                            modifier = Modifier.size(64.dp, 96.dp).offset(x = xOffSet),
                            painter = painterResource(id = R.drawable.img_mumei_char),
                            contentDescription = ""
                        )
                    }

                }
            }
        }
    }

    private fun startService() {
        if (Settings.canDrawOverlays(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(this, VipeService::class.java))
            } else {
                startService(Intent(this, VipeService::class.java))
            }
        }
    }

    private fun stopCharacterService(){
        stopService(Intent(this, VipeService::class.java))
    }

    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            startActivity(myIntent)
        }
    }

}
