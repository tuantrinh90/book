package com.dz.libraries.utilities

import android.content.Context
import android.os.Environment
import android.util.Base64
import android.util.Base64OutputStream
import com.dz.libraries.loggers.Logger
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class FileUtility private constructor(f: File? = null) {
    companion object {
        private const val TAG = "FileUtility"
        private val LINE_SEP = System.getProperty("line.separator")

        private fun isSdCardPresent(): Boolean {
            return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        }

        fun with(file: File?) = FileUtility(file)

        fun getFileFromPath(filePath: String?): File? = if (StringUtility.isNullOrEmpty(filePath)) null else File(filePath)

        fun isFileExists(filePath: File?): Boolean = filePath?.exists() ?: false

        fun getRootPath(context: Context, folderName: String): File? {
            try {
                return if (isSdCardPresent()) {
                    val file = File(Environment.getExternalStorageDirectory().absolutePath, folderName)
                    if (!isFileExists(file)) file.mkdir()
                    file
                } else {
                    val file = File(context.cacheDir, folderName)
                    if (!isFileExists(file)) file.mkdir()
                    file
                }
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return null
        }

        fun createFolder(path: String, folderName: String): File? {
            try {
                val file = File(path, folderName)
                if (!isFileExists(file)) file.mkdir()
                return file
            } catch (e: Exception) {
                println("$TAG Create folder failed:: ${e.message}")
            }

            return null
        }

        fun convertFileToBase64(file: File?): String? {
            try {
                if (!isFileExists(file)) return null

                val inputStream = FileInputStream(file!!.absolutePath)
                if (OptionalUtility.isNullOrEmpty(inputStream)) return null

                val buffer = ByteArray(8192)
                var bytesRead: Int

                val output = ByteArrayOutputStream()
                val output64 = Base64OutputStream(output, Base64.DEFAULT)

                do {
                    bytesRead = inputStream.read(buffer)
                    if (bytesRead == -1) break
                    output64.write(buffer, 0, bytesRead)
                } while (true)

                // close
                output64.close()

                return output.toString()
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return null
        }

        fun getByteFromPath(path: String): ByteArray? {
            try {
                val file = getFileFromPath(path)
                if (!isFileExists(file)) return null

                val buf = ByteArray(file!!.length().toInt())
                val bos = ByteArrayOutputStream()
                val fis = FileInputStream(file)

                var readNum: Int
                do {
                    readNum = fis.read(buf)
                    if (readNum == -1) break
                    bos.write(buf, 0, readNum)
                    println("$TAG:: Read num bytes: $readNum")
                } while (true)

                return bos.toByteArray()
            } catch (e: IOException) {
                Logger.e(TAG, e)
            }

            return null
        }

        fun deleteFile(path: File?): Boolean {
            try {
                if (!isFileExists(path)) return false

                val files = path!!.listFiles()
                if (!CollectionUtility.isNullOrEmpty(files)) {
                    for (file in files) {
                        if (file.isFile) file.delete()
                        if (file.isDirectory) deleteFile(file)
                    }
                }

                return path.delete()
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return false
        }
    }

    var file: File? = f

    fun doIfEmpty(consumer: () -> Unit) {
        if (!isFileExists(file)) {
            consumer()
        }
    }

    fun doIfPresenter(consumer: (f: File) -> Unit) {
        if (isFileExists(file)) {
            consumer(file!!)
        }
    }
}