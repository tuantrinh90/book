package com.dz.libraries.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@Suppress("DEPRECATION")
class FacebookUtility {
    companion object {
        fun getHashKey(context: Context): String? {
            var result = ""

            try {
                @SuppressLint("PackageManagerGetSignatures")
                val info = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)

                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    result = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                    println("HashKey = $result")
                    return result
                }
            } catch (e: PackageManager.NameNotFoundException) {
                println("NameNotFoundException:: " + e.message)
            } catch (e: NoSuchAlgorithmException) {
                println("NoSuchAlgorithmException:: " + e.message)
            }

            return result
        }
    }
}