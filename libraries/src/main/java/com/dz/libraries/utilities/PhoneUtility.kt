package com.dz.libraries.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.telephony.TelephonyManager
import android.util.TypedValue
import androidx.annotation.RequiresApi
import com.dz.libraries.loggers.Logger

@Suppress("DEPRECATION")
class PhoneUtility {
    companion object {
        private const val TAG = "PhoneUtility"

        fun isPhone(context: Context): Boolean? {
            try {
                val telephonyManager: TelephonyManager? = context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                return telephonyManager?.phoneType != TelephonyManager.PHONE_TYPE_NONE
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return false
        }

        @SuppressLint("MissingPermission")
        @RequiresApi(Build.VERSION_CODES.O)
        fun getIMEI(context: Context): String? {
            try {
                val telephonyManager: TelephonyManager? = context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                return telephonyManager?.imei
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return ""
        }

        @SuppressLint("MissingPermission", "HardwareIds")
        fun getIMSI(context: Context): String? {
            try {
                val telephonyManager: TelephonyManager? = context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                return telephonyManager?.subscriberId
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return ""
        }

        fun getPhoneType(context: Context): Int? {
            try {
                val telephonyManager: TelephonyManager? = context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                return telephonyManager?.phoneType
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return -1
        }

        fun isSimCardReady(context: Context): Boolean? {
            try {
                val telephonyManager: TelephonyManager? = context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                return telephonyManager?.simState == TelephonyManager.SIM_STATE_READY
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return false
        }

        fun getSimOperatorName(context: Context): String? {
            try {
                val telephonyManager: TelephonyManager? = context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                return telephonyManager?.simOperatorName
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return ""
        }

        fun getDeviceInfo(): String {
            return ("Model: " + Build.MODEL
                    + "\nId: " + Build.ID
                    + "\nDevice: " + Build.DEVICE
                    + "\nBrand: " + Build.BRAND
                    + "\nDisplay: " + Build.DISPLAY
                    + "\nHardware: " + Build.HARDWARE
                    + "\nBoard: " + Build.BOARD
                    + "\nHost: " + Build.HOST
                    + "\nManufacturer: " + Build.MANUFACTURER
                    + "\nProduct: " + Build.PRODUCT)
        }

        /**
         * @return return device os and version
         */
        fun getDeviceOs(): String {
            return "Android " + Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")"
        }

        /**
         * get device id
         *
         * @param act
         * @return device id
         */
        @SuppressLint("MissingPermission", "HardwareIds")
        fun getDeviceId(act: Context): String? {
            var deviceId = ""

            try {
                deviceId = android.provider.Settings.Secure.getString(act.contentResolver, android.provider.Settings.Secure.ANDROID_ID)
                StringUtility.with(deviceId).doIfEmpty {
                    val tm = act.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    deviceId = tm.deviceId
                    StringUtility.with(deviceId).doIfEmpty { deviceId = tm.simSerialNumber }
                }
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return deviceId
        }

        /**
         * Returns the consumer friendly device name
         */
        fun getDeviceName(): String? {
            try {
                val manufacturer = Build.MANUFACTURER
                val model = Build.MODEL

                return if (model.startsWith(manufacturer)) StringUtility.with(model).capitalize()
                else StringUtility.with(model).capitalize() + " " + model
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return ""
        }


        /**
         * check device support camera
         *
         * @param context
         * @return true if device has supported camera
         */
        fun isDeviceSupportCamera(context: Context): Boolean =
                context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)

        /**
         * convert dp to pixel
         *
         * @param context
         * @param dimenId
         * @return Pixel
         */
        fun convertDpMeasureToPixel(context: Context, dimenId: Int): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    context.resources.getDimension(dimenId), context.resources.displayMetrics).toInt()
        }

        /**
         * convert sp to pixel
         *
         * @param context
         * @param dimenId
         * @return
         */
        fun convertSpMeasureToPixel(context: Context, dimenId: Int): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    context.resources.getDimension(dimenId), context.resources.displayMetrics).toInt()
        }

        /**
         * the check device is tablet or phone
         *
         * @param context
         * @return
         */
        fun isTablet(context: Context): Boolean {
            return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
        }
    }
}