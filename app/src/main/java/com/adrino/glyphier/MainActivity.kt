package com.adrino.glyphier

import android.content.ComponentName
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.adrino.glyphier.ui.theme.GlyphierTheme
import com.nothing.ketchum.Common
import com.nothing.ketchum.GlyphException
import com.nothing.ketchum.GlyphManager


class MainActivity : ComponentActivity() {
    private val TAG = "Glyphier"
    private lateinit var mGlyphManager: GlyphManager
    private val mGlyphCallback = object: GlyphManager.Callback {
        override fun onServiceConnected(component: ComponentName?) {
            if (Common.is20111()) mGlyphManager.register(Common.DEVICE_20111)
            if (Common.is22111()) mGlyphManager.register(Common.DEVICE_22111)
            if (Common.is23111()) mGlyphManager.register(Common.DEVICE_23111)
            if (Common.is23113()) mGlyphManager.register(Common.DEVICE_23113)
            try {
                mGlyphManager.openSession()
            } catch (e: GlyphException) {
                Log.e(TAG, e.message ?: "")
            }
        }

        override fun onServiceDisconnected(component: ComponentName?) {
            mGlyphManager.closeSession();
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            initGlyphComponents()
            GlyphierTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Button(
                        modifier = Modifier.wrapContentSize(),
                        onClick = {
                            val frame = mGlyphManager.glyphFrameBuilder
                                .buildChannelB()
                                .buildInterval(10)
                                .buildCycles(3)
                                .buildPeriod(3000)
                                .build()
                            mGlyphManager.animate(frame)
                        }
                    ) {
                        Text(text = "Start")
                    }
                }
            }
        }
    }

    private fun initGlyphComponents() {
        mGlyphManager = GlyphManager.getInstance(applicationContext);
        mGlyphManager.init(mGlyphCallback);
    }

    override fun onDestroy() {
        try {
            mGlyphManager.closeSession()
        } catch (error: GlyphException) {
            Log.e(TAG, error.message ?: "")
        }
        mGlyphManager.unInit()
        super.onDestroy()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GlyphierTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Button(modifier = Modifier.wrapContentSize(), onClick = {}) {
                Text(text = "Start")
            }
        }
    }
}