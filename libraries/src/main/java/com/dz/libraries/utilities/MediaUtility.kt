package com.dz.libraries.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.dz.libraries.applications.ExtApplication
import com.dz.libraries.loggers.Logger
import java.io.File
import java.util.*


class MediaUtility {
    companion object {
        private const val TAG = "MediaUtility"

        // CAMERA
        const val CAMERA_IMAGE_REQUEST = 100
        const val REQUEST_PICK_IMAGE = 101

        // CAMERA VIDEO
        val CAMERA_VIDEO_REQUEST = 102
        val REQUEST_PICK_VIDEO = 103

        // FORMAT
        const val IMAGE_PNG = ".png"
        const val VIDEO_MP4 = ".mp4"

        fun isImage(path: String?): Boolean {
            if (StringUtility.isNullOrEmpty(path)) return false

            return path!!.contains(".png", true) ||
                    path.contains(".jpg", true) ||
                    path.contains(".jpeg", true) ||
                    path.contains(".gif", true) ||
                    path.contains(".tiff", true) ||
                    path.contains(".bmp", true)
        }

        fun isImage(path: File?): Boolean = isImage(path?.absolutePath)

        fun getImageExtension(path: String?): String {
            if (StringUtility.isNullOrEmpty(path)) return ""

            return when {
                path!!.contains(".png", true) -> "png"
                path.contains(".jpg", true) -> "jpg"
                path.contains(".jpeg", true) -> "jpeg"
                path.contains(".gif", true) -> "gif"
                path.contains(".tiff", true) -> "tiff"
                path.contains(".bmp", true) -> "bmp"
                else -> ""
            }
        }

        fun getImageExtension(path: File?): String  = getImageExtension(path?.absolutePath)

        fun isVideo(path: String?): Boolean {
            if (StringUtility.isNullOrEmpty(path)) return false

            return path!!.contains(".mp4", true) ||
                    path.contains(".3gp", true) ||
                    path.contains(".avi", true) ||
                    path.contains(".m4a", true) ||
                    path.contains(".mov", true) ||
                    path.contains(".flac", true)
        }

        fun isVideo(path: File?): Boolean  = isVideo(path?.absolutePath)

        fun getVideoExtension(path: String?): String {
            if (StringUtility.isNullOrEmpty(path)) return ""

            return when {
                path!!.contains(".mp4", true) -> "mp4"
                path.contains(".3gp", true) -> "3gp"
                path.contains(".avi", true) -> "avi"
                path.contains(".m4a", true) -> "m4a"
                path.contains(".mov", true) -> "mov"
                path.contains(".flac", true) -> "flac"
                else -> ""
            }
        }

        fun getVideoExtension(path: File?): String  = getVideoExtension(path?.absolutePath)

        fun getThumbnailFromVideo(videoPath: String?): Bitmap? {
            var bitmap: Bitmap? = null

            var mediaMetadataRetriever: MediaMetadataRetriever? = null
            try {
                if (!StringUtility.isNullOrEmpty(videoPath)) {
                    mediaMetadataRetriever = MediaMetadataRetriever()
                    mediaMetadataRetriever.setDataSource(videoPath, HashMap())
                    bitmap = mediaMetadataRetriever.frameAtTime
                }
            } catch (e: Exception) {
                Log.e("ThumbnailFromVideo", "ThumbnailFromVideo:: ${e.message}")
            } finally {
                mediaMetadataRetriever?.release()
            }

            return bitmap
        }

        /**
         * get random image name
         *
         * @return
         */
        fun getImageNamePng(): String {
            return "${UUID.randomUUID()}$IMAGE_PNG"
        }

        /**
         * @return a image url from sd card
         */
        fun getImageUrlPng(): File {
            return File(ExtApplication.getPathProject(), getImageNamePng())
        }

        /**
         * get random video name
         *
         * @return
         */
        fun getVideoNameMp4(): String {
            return "${UUID.randomUUID()}$VIDEO_MP4"
        }

        /**
         * @return a video url from sd card
         */
        fun getVideoUrlMp4(): File {
            return File(ExtApplication.getPathProject(), getVideoNameMp4())
        }

        /**
         * @param context
         * @param file
         * @return a uri from device from android 7
         */
        fun getUriFromFileProvider(context: Context, file: File): Uri {
            return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        }

        /**
         * @param filePath
         * @return a uri image using to display image through Image Loader
         */
        fun getUriImageDisplayFromString(filePath: String?): String? {
            val file = FileUtility.getFileFromPath(filePath)
            return if (!FileUtility.isFileExists(file)) null else Uri.fromFile(file).toString()
        }

        /**
         * @param filePath
         * @return a uri image using to display image through Image Loader
         */
        fun getUriImageDisplayFromFile(filePath: File?): String? {
            return if (!FileUtility.isFileExists(filePath)) null else Uri.fromFile(filePath).toString()
        }

        /**
         * capture image
         *
         * @param activity
         * @param fileUri  is path use to save file
         */
        fun captureImage(activity: Activity, fileUri: File) {
            try {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriFromFileProvider(activity, fileUri))
                activity.startActivityForResult(intent, CAMERA_IMAGE_REQUEST)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }

        /**
         * capture image
         *
         * @param fragment
         * @param fileUri  is path use to save file
         */
        fun captureImage(fragment: Fragment, fileUri: File) {
            try {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriFromFileProvider(fragment.activity!!, fileUri))
                fragment.startActivityForResult(intent, CAMERA_IMAGE_REQUEST)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }

        /**
         * choose image from gallery
         *
         * @param activity
         * @param titleChooseImage
         */
        fun chooseImageFromGallery(activity: Activity, titleChooseImage: String) {
            try {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activity.startActivityForResult(Intent.createChooser(intent, titleChooseImage), REQUEST_PICK_IMAGE)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }

        /**
         * choose image from gallery
         *
         * @param fragment
         * @param titleChooseImage
         */
        fun chooseImageFromGallery(fragment: Fragment, titleChooseImage: String) {
            try {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                fragment.startActivityForResult(Intent.createChooser(intent, titleChooseImage), REQUEST_PICK_IMAGE)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }
        }


        /**
         * record video
         *
         * @param fragment
         * @param videoUri
         * @duration seconds
         */
        fun captureVideo(fragment: Fragment, videoUri: File, duration: Int) {
            try {
                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                // set limit duration
                if (duration > 0) {
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, duration)
                }

                // quality video 1: high quality, 0: low quality
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriFromFileProvider(fragment.activity!!, videoUri))
                fragment.startActivityForResult(intent, CAMERA_VIDEO_REQUEST)
            } catch (ex: Exception) {
                Logger.e(TAG, ex)
            }
        }

        /**
         * record video
         *
         * @param activity
         * @param videoUri
         * @duration seconds
         */
        fun captureVideo(activity: Activity, videoUri: File, duration: Int = 0) {
            try {
                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)

                // set limit duration
                if (duration > 0) {
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, duration)
                }

                // quality video 1: high quality, 0: low quality
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriFromFileProvider(activity, videoUri))
                activity.startActivityForResult(intent, CAMERA_VIDEO_REQUEST)
            } catch (ex: Exception) {
                Logger.e(TAG, ex)
            }
        }

        /**
         * choose video from gallery
         *
         * @param fragment
         * @param titleChooseVideo
         */
        fun chooseVideoFromGallery(fragment: Fragment, titleChooseVideo: String) {
            try {
                val intent = Intent(Intent.ACTION_PICK)
                intent.action = Intent.ACTION_GET_CONTENT
                intent.type = "video/*"
                fragment.startActivityForResult(Intent.createChooser(intent, titleChooseVideo), REQUEST_PICK_VIDEO)
            } catch (ex: Exception) {
                Logger.e(TAG, ex)
            }
        }

        /**
         * choose video from gallery
         *
         * @param activity
         * @param titleChooseVideo
         */
        fun chooseVideoFromGallery(activity: Activity, titleChooseVideo: String) {
            try {
                val intent = Intent(Intent.ACTION_PICK)
                intent.action = Intent.ACTION_GET_CONTENT
                intent.type = "video/*"
                activity.startActivityForResult(Intent.createChooser(intent, titleChooseVideo), REQUEST_PICK_VIDEO)
            } catch (ex: Exception) {
                Logger.e(TAG, ex)
            }
        }

        /**
         * Method for return file path of Gallery image
         *
         * @param context
         * @param uri
         * @return path of the selected image file from gallery
         */
        @SuppressLint("NewApi")
        fun getPath(context: Context, uri: Uri?): String? {
            if (uri == null) return null

            try {// check here to KITKAT or new version
                // DocumentProvider
                if (DocumentsContract.isDocumentUri(context, uri)) {
                    // ExternalStorageProvider
                    if (isExternalStorageDocument(uri)) {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val type = split[0]

                        if ("primary".equals(type, ignoreCase = true)) {
                            return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                        }
                    } else if (isDownloadsDocument(uri)) {
                        val id = DocumentsContract.getDocumentId(uri)
                        val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                        return getDataColumn(context, contentUri, null, null)
                    } else if (isMediaDocument(uri)) {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val type = split[0]
                        var contentUri: Uri? = null

                        when (type) {
                            "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }

                        val selection = "_id=?"
                        val selectionArgs = arrayOf(split[1])
                        return getDataColumn(context, contentUri, selection, selectionArgs)
                    }
                } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
                    // Return the remote address
                    return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
                } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
                    return uri.path
                }
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return null
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context       The context.
         * @param uri           The Uri to query.
         * @param selection     (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        private fun getDataColumn(context: Context, uri: Uri?,
                                  selection: String?, selectionArgs: Array<String>?): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)

            try {
                cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            } catch (e: Exception) {
                Logger.e(TAG, e)
            } finally {
                cursor?.close()
            }

            return null
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        private fun isExternalStorageDocument(uri: Uri): Boolean {
            try {
                return "com.android.externalstorage.documents" == uri.authority
            } catch (ex: Exception) {
                Logger.e(TAG, ex)
            }

            return false
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        private fun isDownloadsDocument(uri: Uri): Boolean {
            try {
                return "com.android.providers.downloads.documents" == uri.authority
            } catch (ex: Exception) {
                Logger.e(TAG, ex)
            }

            return false
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        private fun isMediaDocument(uri: Uri): Boolean {
            try {
                return "com.android.providers.media.documents" == uri.authority
            } catch (ex: Exception) {
                Logger.e(TAG, ex)
            }

            return false
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        private fun isGooglePhotosUri(uri: Uri): Boolean {
            try {
                return "com.google.android.apps.photos.content" == uri.authority
            } catch (ex: Exception) {
                Logger.e(TAG, ex)
            }

            return false
        }
    }
}