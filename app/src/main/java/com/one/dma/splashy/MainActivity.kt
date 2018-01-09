package com.one.dma.splashy

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

import com.one.dma.splashy_lib.Splashy

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val font: Typeface = Typeface.createFromAsset(getAssets(), "comic_sans.ttf")

        val splash = Splashy(this@MainActivity)
                //.setFullScreen()
                .setTimeoutDuration(5000)
                .setTargetActivity(TargetActivity::class.java)
                .setLogo(R.drawable.demo)
                .setBackgroundResource(R.color.colorPrimary)
                .setLogoHeaderText("Test the header bla bla", 20, R.color.colorAccent, font, null)
                //.setLogoFooterText("Ferret Card", null, null, null, null)
                .setLogoFooterText("Snapclaim", 20, R.color.colorAccent, font, null)
                .setVersionText("Version 1.1.2", null, R.color.colorPrimaryDark, null)
                //.setBackgroundResource(R.drawable.ic_launcher_background)
                //.create()

        setContentView(splash.create())
/*
        val handler = Handler()
        handler.postDelayed({
            splash.kill()
        }, 3000)
*/
    }
}
