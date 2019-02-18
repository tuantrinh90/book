package com.dz.libraries.utilities

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import com.dz.libraries.loggers.Logger
import java.util.*


class TypefaceUtility private constructor(ctx: Context) {
    companion object {
        private const val TAG = "TypefaceUtility"

        private val caches = Hashtable<String, Typeface>()

        var FONT_DEFAULT = "fonts/sf_pro_display_regular.ttf"

        fun with(context: Context) = TypefaceUtility(context)

        /**
         * Using reflection to override default typeface
         * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
         * <style name="MyAppTheme" parent="@android:style/Theme.Holo.Light">
         *     <!-- you should set typeface which you want to override with TypefaceUtil -->
         *     <item name="android:typeface">serif</item>
         * </style>
         * public class MyApp extends Application {
         * @Override
         * public void onCreate() {
         *          TypefaceUtility.overrideFont(getApplicationContext(), "SERIF", "fonts/Roboto-Regular.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
         *      }
         * }
         * @param context to work with assets
         * @param defaultFontNameToOverride for example "monospace"
         * @param customFontFileNameInAssets file name of the font from assets
         */
        fun overrideFont(context: Context, defaultFontNameToOverride: String, customFontFileNameInAssets: String) {
            try {
                val customFontTypeface = Typeface.createFromAsset(context.assets, customFontFileNameInAssets)
                val defaultFontTypefaceField = Typeface::class.java.getDeclaredField(defaultFontNameToOverride)
                defaultFontTypefaceField.isAccessible = true
                defaultFontTypefaceField.set(null, customFontTypeface)
            } catch (e: Exception) {
                Log.e(TAG, "Can not set custom font $customFontFileNameInAssets instead of $defaultFontNameToOverride")
            }
        }
    }

    val context: Context = ctx

    fun get(assetPath: String): Typeface? {
        synchronized(caches) {
            if (!caches.containsKey(assetPath)) {
                try {
                    val tf = Typeface.createFromAsset(context.applicationContext.assets, assetPath)
                    caches[assetPath] = tf
                } catch (e: Exception) {
                    Logger.e(TAG, "Could not get typeface '" + assetPath + "' because " + e.message)
                }
            }

            return caches[assetPath]
        }
    }
}