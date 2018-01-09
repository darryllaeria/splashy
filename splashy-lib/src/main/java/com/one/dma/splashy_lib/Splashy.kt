package com.one.dma.splashy_lib

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.view.View
import android.view.WindowManager
import android.util.TypedValue
import android.util.DisplayMetrics
import android.widget.ProgressBar


/**
 * Created by darryll on 1/4/2018.
 */

class Splashy(internal var mActivity: AppCompatActivity) {

    private var mView: View
    private var splashWrapperRelativeLayout: RelativeLayout
    private var targetActivity: Class<*>? = null
    private var bundle: Bundle? = null

    private var SPLASH_TIME_OUT: Int = 2000 // Default duration before launch target activity
    private var actionBarHeight: Float = 0.0f

    private var handler: Handler = Handler()
    private var killHandler: Boolean = false

    /**
     * Default constructor
     */
    init {
        val mInflater: LayoutInflater = LayoutInflater.from(mActivity)
        this.mView = mInflater.inflate(R.layout.splash, null)
        this.splashWrapperRelativeLayout = mView.findViewById<RelativeLayout>(R.id.splash_wrapper_rl)

        // Get action bar height to offset in Y axis, to make it centre
        val tv = TypedValue()
        if (mActivity.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = convertPixelsToDp(TypedValue.complexToDimensionPixelSize(tv.data, mActivity.resources.getDisplayMetrics()), mActivity)

            splashWrapperRelativeLayout.findViewById<ImageView>(R.id.logo).y = -actionBarHeight
            splashWrapperRelativeLayout.findViewById<TextView>(R.id.logo_header_tv).y = -actionBarHeight
            splashWrapperRelativeLayout.findViewById<TextView>(R.id.logo_footer_tv).y = -actionBarHeight
            splashWrapperRelativeLayout.findViewById<ProgressBar>(R.id.progress_bar).y = -actionBarHeight
        }

        // Hide support action bar
        mActivity.supportActionBar!!.hide()

        // Set visibility to invisible until manually set
        splashWrapperRelativeLayout.findViewById<TextView>(R.id.logo_header_tv).visibility = View.INVISIBLE
        splashWrapperRelativeLayout.findViewById<TextView>(R.id.logo_footer_tv).visibility = View.INVISIBLE
        splashWrapperRelativeLayout.findViewById<TextView>(R.id.version_tv).visibility = View.INVISIBLE
    }

    /**
     * Sets full screen splash screen
     */
    fun setFullScreen(): Splashy {

        mActivity.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        return this
    }

    /**
     * Sets target activity after splash screen
     */
    fun setTargetActivity(tActivity: Class<*>): Splashy {
        this.targetActivity = tActivity

        return this
    }

    /**
     * Set splash screen logo
     */
    fun setLogo(logo: Int): Splashy {

        if(logo != 0) {
            val logoImageView = splashWrapperRelativeLayout.findViewById<ImageView>(R.id.logo)
            logoImageView.setImageResource(logo)
        }

        return this
    }

    /**
     * Override default splash timeout duration
     */
    fun setTimeoutDuration(timeout: Int): Splashy {

        this.SPLASH_TIME_OUT = timeout

        return this
    }

    /**
     * Set background resource for splash screen; color, drawable, etc
     */
    fun setBackgroundResource(resource: Int): Splashy {

        if(resource != 0) {
            splashWrapperRelativeLayout.setBackgroundResource(resource)
        }

        return this
    }

    /**
     * Set header logo text
     */
    fun setLogoHeaderText(headerText: String?, textSize: Int?, colorResource: Int?, font: Typeface?, headerLogoSpacing: Int?): Splashy {

        val logoHeader: TextView = splashWrapperRelativeLayout.findViewById<TextView>(R.id.logo_header_tv)

        if(headerText != null) {
            logoHeader.visibility = View.VISIBLE
            logoHeader.text = headerText

            // Set text size
            alterTextSize(logoHeader, textSize)

            // Set text color
            alterTextColor(logoHeader, colorResource)

            // Set text type face
            alterTypeface(logoHeader, font)

            // Set header to logo padding space
            if(headerLogoSpacing != null && headerLogoSpacing > 0) {
                logoHeader.setPadding(0,0,0, headerLogoSpacing)
            }
        }

        return this
    }

    /**
     * Set header logo text
     */
    fun setLogoFooterText(footerText: String?, textSize: Int?, colorResource: Int?, font: Typeface?, footerLogoSpacing: Int?): Splashy {

        val logoFooter: TextView = splashWrapperRelativeLayout.findViewById<TextView>(R.id.logo_footer_tv)

        if(footerText != null) {
            logoFooter.visibility = View.VISIBLE
            logoFooter.text = footerText

            // Set text size
            alterTextSize(logoFooter, textSize)

            // Set text color
            alterTextColor(logoFooter, colorResource)

            // Set text type face
            alterTypeface(logoFooter, font)

            // Set header to logo padding space
            if(footerLogoSpacing != null && footerLogoSpacing > 0) {
                logoFooter.setPadding(0, footerLogoSpacing,0,0)
            }
        }

        return this
    }

    /**
     * Set version text
     */
    fun setVersionText(versionText: String?, textSize: Int?, colorResource: Int?, font: Typeface?): Splashy {

        val version: TextView = splashWrapperRelativeLayout.findViewById<TextView>(R.id.version_tv)

        if(versionText != null) {
            version.visibility = View.VISIBLE
            version.text = versionText

            // Set text size
            alterTextSize(version, textSize)

            // Set text color
            alterTextColor(version, colorResource)

            // Set text type face
            alterTypeface(version, font)
        }

        return this
    }

    /**
     * To be called to create splash screen
     */
    fun create(): View {
        if (targetActivity != null) {
            handler.postDelayed(
                    {
                        if(!killHandler) {
                            startTargetActivity()
                        }
                    },
                    SPLASH_TIME_OUT.toLong()
            )
        }

        return mView
    }

    /**
     * To be called to kill splash screen, after finish loading background, etc
     */
    fun kill() {
        killHandler = true

        startTargetActivity()
    }

    // Code chunk to start target activity
    private fun startTargetActivity() {
        val i = Intent(mActivity, targetActivity)

        if (bundle != null) {
            i.putExtras(bundle!!)
        }

        mActivity.run {
            startActivity(i)

            // Fade out splash screen
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            // Close splash
            finish()
        }
    }

    /****** Internal functions ******/

    // Change text size for Text View
    private fun alterTextSize(textView: TextView, textSize: Int?) {
        if(textSize != null) {
            textView.textSize = textSize.toFloat()
        }
    }

    // Change text color for Text View
    private fun alterTextColor(textView: TextView, colorResource: Int?) {
        if(colorResource != 0 && colorResource != null) {
            textView.setTextColor(ContextCompat.getColor(mActivity.applicationContext, colorResource))
        }
    }

    // Change text font/typeface for Text View
    private fun alterTypeface(textView: TextView, typeFace: Typeface?) {
        if(typeFace != null) {
            textView.typeface = typeFace
        }
    }

    private fun convertPixelsToDp(pixel: Int, activity: AppCompatActivity): Float {
        return pixel / (activity.resources.getDisplayMetrics().densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}