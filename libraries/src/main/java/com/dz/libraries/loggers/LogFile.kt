package com.dz.libraries.loggers

import android.annotation.SuppressLint
import com.dz.libraries.utilities.FileUtility
import com.dz.libraries.utilities.OptionalUtility
import com.dz.libraries.utilities.StringUtility
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LogFile {
    companion object {
        private val TAG = LogFile::class.java.simpleName
        private var executor: ExecutorService? = null

        /**
         * log string to file on sdcard
         *
         * @param path
         * @param msg
         */
        @SuppressLint("SimpleDateFormat")
        fun logToFile(path: String, msg: String) {
            try {
                if (StringUtility.isNullOrEmpty(path)) return

                // create executor
                OptionalUtility.with(executor).doIfEmpty { executor = Executors.newSingleThreadExecutor() }

                // execute log file
                LogFile.executor!!.execute {
                    try {
                        val time = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(Date())
                        val file = getFileFromPath(path)
                        if (FileUtility.isFileExists(file)) {
                            val out = PrintWriter(BufferedWriter(FileWriter(file, true)))
                            out.println("$time \n $msg \n")
                            out.flush()
                            out.close()
                        }
                    } catch (e: Exception) {
                        println("$TAG::logToFile::${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("$TAG::logToFile::${e.message}")
            }
        }

        /**
         * Get File form the file path.<BR></BR>
         * if the file does not exist, create it and return it.
         *
         * @param path the file path
         * @return the file
         */
        private fun getFileFromPath(path: String): File? {
            try {
                val file = FileUtility.getFileFromPath(path)
                if (OptionalUtility.isNullOrEmpty(file)) return null

                if (FileUtility.isFileExists(file)) {
                    if (!file!!.canWrite()) {
                        println("$TAG:: The log file can not be written.")
                        return null
                    }
                }

                // create the log file
                try {
                    if (file!!.createNewFile()) {
                        println("$TAG:: The log file was successfully created! - ${file.absolutePath}")
                    } else {
                        println("$TAG:: The log file exist! - ${file.absolutePath}")
                    }

                    if (!file.canWrite()) {
                        println("$TAG:: The log file can not be written.")
                        return null
                    }
                } catch (e: IOException) {
                    println("$TAG:: Failed to create the log file:: ${e.message}")
                }

                return file
            } catch (e: Exception) {
                println("$TAG::getFileFromPath::${e.message}")
            }

            return null
        }
    }
}