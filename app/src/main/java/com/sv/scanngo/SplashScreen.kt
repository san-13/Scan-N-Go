package com.sv.scanngo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Layout
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class SplashScreen : AppCompatActivity() {
    private val SPLASH_TIME:Long=2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_splash_screen)
            setContent {
                setUI()
            }
        Handler().postDelayed({
                startActivity(Intent(this,MainActivity::class.java))
            finish()
        },SPLASH_TIME)
    }
    @Preview(showBackground = true,
    showSystemUi = true)
    @Composable
    fun setUI(){
        Box(modifier = Modifier
            .background(
                color = colorResource(id = R.color.black),
                shape = RectangleShape
            )
            .fillMaxSize()
            ) {
            Column(modifier=Modifier.fillMaxHeight().wrapContentHeight(Alignment.CenterVertically).fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)) {
                Image(
                    painter = painterResource(id = R.drawable.image_removebg_preview),
                    contentDescription = "",
                    modifier = Modifier.size(width= 100.dp, height = 100.dp).fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }

    }
}