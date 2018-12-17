package com.nodecoyote.happybelly.utility

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.customtabs.CustomTabsIntent
import android.widget.Toast
import com.nodecoyote.happybelly.R
import java.io.Serializable

class WebViewHelper: Serializable {
    fun open(url: String, context: Context) {
        val webAddress = Uri.parse(url)

        // Chrome Tabs
        val tab = CustomTabsIntent.Builder()
        val tabIntent = tab.build()
        if (Build.VERSION.SDK_INT >= 23) {
            tab.setToolbarColor(context.getColor(R.color.colorAccent))
        }

        // Any browser
        val browserIntent = Intent(Intent.ACTION_VIEW, webAddress)
        try {
            if (!url.isEmpty()) {
                tabIntent.launchUrl(context, webAddress)
            }
        } catch (e: ActivityNotFoundException) {
            if (browserIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(browserIntent)
            } else {
                Toast.makeText(context, "Browser required to access this website", Toast.LENGTH_LONG).show()
            }
        }
    }
}