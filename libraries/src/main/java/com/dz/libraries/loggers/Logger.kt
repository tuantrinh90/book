package com.dz.libraries.loggers

import android.annotation.SuppressLint
import android.util.Log
import com.dz.libraries.utilities.CollectionUtility
import com.dz.libraries.utilities.FileUtility
import com.dz.libraries.utilities.OptionalUtility
import com.dz.libraries.utilities.StringUtility
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Logger {
    companion object {
        private const val TAG = "Logger"
        private const val tagDefault = "Logger"

        private var isEnableLog = true
        private var path = ""

        @SuppressLint("SdCardPath")
        private var dirPath = "/mnt/sdcard/logger"
        private var name = "logger"
        private var suffix = "log"

        /**
         * @param tag
         * @param msg
         * @return a error message from exception
         */
        private fun getMessageError(tag: String?, msg: String?): String? {
            // The log will be shown in log cat.
            with(StringBuilder()) {
                try {// log details
                    val throwable = Throwable().fillInStackTrace()
                    if (CollectionUtility.with(throwable?.stackTrace).size() > 0) {
                        // bufferLog.append(Arrays.toString(throwable?.stackTrace))
                        append(throwable?.stackTrace!![3].toString()) // with index is 3, StackTraceElement store information caused error
                    }
                } catch (e: Exception) {
                    println("$TAG::getMessageError::${e.message}")
                }

                // append tag
                append("$tag::$msg")

                // return
                return toString()
            }
        }

        /**
         * Building Message
         *
         * @param msg The message you would like logged.
         * @return Message String
         */
        private fun buildMessage(typeLog: TypeLog, tag: String?, msg: String?): String? {
            val bufferLog = getMessageError(tag, msg)

            // The log will be written in the log file.
            if (isEnableLog) {
                LogFile.logToFile(path, "${typeLog.name}    $bufferLog")
            }

            return bufferLog
        }

        /**
         * @param e
         * @return a message from exception
         */
        private fun getMessageException(e: Exception?): String? {
            return e?.message
        }

        /**
         * Send a DEBUG log message.
         */
        fun d(msg: String?) {
            if (isEnableLog) {
                Log.d(tagDefault, buildMessage(TypeLog.DEBUG, tagDefault, msg))
            }
        }

        /**
         * Send a DEBUG log message.
         *
         * @param ex
         */
        fun d(ex: Exception?) {
            if (isEnableLog) {
                Log.d(tagDefault, buildMessage(TypeLog.DEBUG, tagDefault, getMessageException(ex)))
            }
        }

        /**
         * Send a DEBUG log message.
         *
         * @param msg The message you would like logged.
         */
        fun d(tag: String?, msg: String?) {
            if (isEnableLog) {
                StringUtility.with(tag)
                        .doIfEmpty { d(msg) }
                        .doIfPresent { Log.d(tag, buildMessage(TypeLog.DEBUG, tag, msg)) }
            }
        }

        /**
         * Send a DEBUG log message.
         *
         * @param tag
         * @param ex
         */
        fun d(tag: String?, ex: Exception?) {
            if (isEnableLog) {
                StringUtility.with(tag)
                        .doIfEmpty { d(ex) }
                        .doIfPresent { Log.d(tag, buildMessage(TypeLog.DEBUG, tag, getMessageException(ex))) }
            }
        }

        /**
         * Send an ERROR log message.
         *
         * @param msg The message you would like logged.
         */
        fun e(msg: String?) {
            if (isEnableLog) {
                Log.e(tagDefault, buildMessage(TypeLog.ERROR, tagDefault, msg))
            }
        }

        /**
         * Send an ERROR log message.
         *
         * @param ex
         */
        fun e(ex: Exception?) {
            if (isEnableLog) {
                Log.e(tagDefault, buildMessage(TypeLog.ERROR, tagDefault, getMessageException(ex)))
            }
        }

        /**
         * Send a ERROR log message.
         *
         * @param msg The message you would like logged.
         */
        fun e(tag: String?, msg: String?) {
            if (isEnableLog) {
                StringUtility.with(tag)
                        .doIfEmpty { e(msg) }
                        .doIfPresent { Log.e(tag, buildMessage(TypeLog.ERROR, tag, msg)) }
            }
        }

        /**
         * Send a ERROR log message.
         *
         * @param tag
         * @param ex
         */
        fun e(tag: String?, ex: Exception?) {
            if (isEnableLog) {
                StringUtility.with(tag)
                        .doIfEmpty { e(ex) }
                        .doIfPresent { Log.e(tag, buildMessage(TypeLog.ERROR, tag, getMessageException(ex))) }
            }
        }

        /**
         * Send a INFO log message.
         *
         * @param msg The message you would like logged.
         */
        fun i(tag: String?, msg: String?) {
            if (isEnableLog) {
                StringUtility.with(tag)
                        .doIfEmpty { i(msg) }
                        .doIfPresent { Log.i(tag, buildMessage(TypeLog.INFO, tag, msg)) }
            }
        }

        /**
         * Send a INFO log message.
         *
         * @param tag
         * @param ex
         */
        fun i(tag: String?, ex: Exception?) {
            if (isEnableLog) {
                StringUtility.with(tag)
                        .doIfEmpty { i(ex) }
                        .doIfPresent { Log.i(tag, buildMessage(TypeLog.INFO, tag, getMessageException(ex))) }
            }
        }

        /**
         * Send an INFO log message.
         *
         * @param msg The message you would like logged.
         */
        fun i(msg: String?) {
            if (isEnableLog) {
                Log.i(tagDefault, buildMessage(TypeLog.INFO, tagDefault, msg))
            }
        }

        /**
         * Send an INFO log message.
         *
         * @param ex
         */
        fun i(ex: Exception?) {
            if (isEnableLog) {
                Log.i(tagDefault, buildMessage(TypeLog.INFO, tagDefault, getMessageException(ex)))
            }
        }

        /**
         * Send a VERBOSE log message.
         *
         * @param msg The message you would like logged.
         */
        fun v(tag: String?, msg: String?) {
            if (isEnableLog) {
                StringUtility.with(tag)
                        .doIfEmpty { v(msg) }
                        .doIfPresent { Log.v(tag, buildMessage(TypeLog.VERBOSE, tag, msg)) }
            }
        }

        /**
         * Send a VERBOSE log message.
         *
         * @param tag
         * @param ex
         */
        fun v(tag: String?, ex: Exception?) {
            if (isEnableLog) {
                StringUtility.with(tag)
                        .doIfEmpty { v(ex) }
                        .doIfPresent { Log.v(tag, buildMessage(TypeLog.VERBOSE, tag, getMessageException(ex))) }
            }
        }

        /**
         * Send a VERBOSE log message.
         *
         * @param msg The message you would like logged.
         */
        fun v(msg: String?) {
            if (isEnableLog) {
                Log.v(tagDefault, buildMessage(TypeLog.VERBOSE, tagDefault, msg))
            }
        }

        /**
         * Send a VERBOSE log message.
         *
         * @param ex
         */
        fun v(ex: Exception?) {
            if (isEnableLog) {
                Log.v(tagDefault, buildMessage(TypeLog.VERBOSE, tagDefault, getMessageException(ex)))
            }
        }

        /**
         * Send a WARN log message.
         *
         * @param msg The message you would like logged.
         */
        fun w(tag: String?, msg: String?) {
            if (isEnableLog) {
                StringUtility.with(tag)
                        .doIfEmpty { w(msg) }
                        .doIfPresent { Log.w(tag, buildMessage(TypeLog.WARN, tag, msg)) }
            }
        }

        /**
         * Send a WARN log message.
         *
         * @param tag
         * @param ex
         */
        fun w(tag: String?, ex: Exception?) {
            if (isEnableLog) {
                StringUtility.with(tag)
                        .doIfEmpty { w(ex) }
                        .doIfPresent { Log.w(tag, buildMessage(TypeLog.WARN, tag, getMessageException(ex))) }
            }
        }

        /**
         * Send a WARN log message
         *
         * @param msg The message you would like logged.
         */
        fun w(msg: String?) {
            if (isEnableLog) {
                Log.w(tagDefault, buildMessage(TypeLog.WARN, tagDefault, msg))
            }
        }

        /**
         * Send a WARN log message
         *
         * @param ex
         */
        fun w(ex: Exception?) {
            if (isEnableLog) {
                Log.w(tagDefault, buildMessage(TypeLog.WARN, tagDefault, getMessageException(ex)))
            }
        }

        /**
         * enable or disable the log
         *
         * @param isEnableLog whether to enable the log
         */
        fun setEnableLog(isEnableLog: Boolean) {
            Logger.isEnableLog = isEnableLog
        }

        /**
         * @param path
         */
        fun setPathSaveLog(path: String) {
            Logger.path = path

            // create folder to storage log file
            createLogDir(path)
        }

        /**
         * set the log file path The log file path will be: dirPath +
         *
         *
         * name + Formatted time +suffix
         *
         * @param dirPath the log file dir path,such as "/mnt/sdcard/dzlogger"
         * @param name    the log file base file name, such as "log"
         * @param suffix  the log file suffix, such as "log"
         */
        @SuppressLint("SimpleDateFormat")
        fun setPathSaveLog(dirPath: String, name: String,
                           suffix: String) {
            Logger.dirPath = dirPath
            Logger.name = name
            Logger.suffix = suffix

            // day error
            val fdf = SimpleDateFormat("yyyy-MM-dd")
            val myDateString = fdf.format(Date())

            // file name
            val buffer = "$name-$myDateString.$suffix"
            val file = File(dirPath, buffer)
            setPathSaveLog(file.absolutePath)
        }

        /**
         * create the Directory from the path
         *
         * @param path
         */
        private fun createLogDir(path: String) {
            try {
                if (isEnableLog) {
                    val file = FileUtility.getFileFromPath(path)
                    if (OptionalUtility.isNullOrEmpty(file)) return

                    if (!file!!.parentFile.exists()) {
                        if (!file.parentFile.mkdirs()) {
                            println("$TAG:: The Log Dir can not be created!")
                        } else {
                            println("$TAG:: The Log Dir was successfully created! - ${file.parent}")
                        }
                    }
                } else {
                    println("$tagDefault:: The Log Dir was not allow be created!")
                }
            } catch (e: Exception) {
                println("$TAG::createLogDir::${e.message}")
            }
        }

        /**
         * type of log
         */
        private enum class TypeLog {
            INFO, DEBUG, VERBOSE, WARN, ERROR
        }
    }
}