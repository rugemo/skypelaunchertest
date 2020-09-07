package com.rugemo.skypelaunchertest

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pm_skype_button.setOnClickListener {
            error_view_title.visibility = View.GONE
            error_view_body.visibility = View.GONE
            val intent = packageManager.getLaunchIntentForPackage("com.skype.raider") ?: Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse("skype:" + "live:echo123" + "?chat")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        tut_skype_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("skype:" + "live:echo123" + "?chat"))
            intent.component = ComponentName("com.skype.raider", "com.skype.raider.Main")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            try {
                startActivity(intent)
            } catch(exception: ActivityNotFoundException) {
                error_view_title.visibility = View.VISIBLE
                error_view_body.visibility = View.VISIBLE
                error_view_body.text = exception.message
            }

        }

        install_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.skype.raider"))
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        if(checkForSkype()) {
            install_button.visibility = View.GONE
            install_text.visibility = View.GONE
            pm_skype_button.visibility = View.VISIBLE
            pm_text.visibility = View.VISIBLE
            tut_text.visibility = View.VISIBLE
            tut_skype_button.visibility = View.VISIBLE
        } else {
            pm_skype_button.visibility = View.GONE
            pm_text.visibility = View.GONE
            tut_text.visibility = View.GONE
            tut_skype_button.visibility = View.GONE
            install_button.visibility = View.VISIBLE
            install_text.visibility = View.VISIBLE
        }
    }

    private fun checkForSkype(): Boolean {
        try {
            packageManager.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
        return true
    }
}