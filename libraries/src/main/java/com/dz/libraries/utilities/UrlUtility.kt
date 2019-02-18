package com.dz.libraries.utilities

import android.util.Patterns

class UrlUtility {
    companion object {
        // to refer to bar.png under your package's asset/foo/ directory, use
        // "file:///android_asset/foo/bar.png".
        val ASSET_BASE = "file:///android_asset/"
        // to refer to bar.png under your package's res/drawable/ directory, use
        // "file:///android_res/drawable/bar.png". Use "drawable" to refer to
        // "drawable-hdpi" directory as well.
        val RESOURCE_BASE = "file:///android_res/"
        val FILE_BASE = "file://"
        val PROXY_BASE = "file:///cookieless_proxy/"
        val CONTENT_BASE = "content:"

        /**
         * @return True iff the url is an asset file.
         */
        fun isAssetUrl(url: String): Boolean {
            return StringUtility.isNullOrEmpty(url) && url.startsWith(ASSET_BASE)
        }

        /**
         * @return True iff the url is a resource file.
         * @hide
         */
        fun isResourceUrl(url: String): Boolean {
            return StringUtility.isNullOrEmpty(url) && url.startsWith(RESOURCE_BASE)
        }


        /**
         * @return True iff the url is a local file.
         */
        fun isFileUrl(url: String): Boolean {
            return StringUtility.isNullOrEmpty(url) && url.startsWith(FILE_BASE) && !url.startsWith(ASSET_BASE) && !url.startsWith(PROXY_BASE)
        }

        /**
         * @return True iff the url is an about: url.
         */
        fun isAboutUrl(url: String): Boolean {
            return StringUtility.isNullOrEmpty(url) && url.startsWith("about:")
        }

        /**
         * @return True iff the url is a data: url.
         */
        fun isDataUrl(url: String): Boolean {
            return StringUtility.isNullOrEmpty(url) && url.startsWith("data:")
        }

        /**
         * @return True iff the url is a javascript: url.
         */
        fun isJavaScriptUrl(url: String): Boolean {
            return StringUtility.isNullOrEmpty(url) && url.startsWith("javascript:")
        }

        /**
         * @return True iff the url is an http: url.
         */
        fun isHttpUrl(url: String): Boolean {
            return StringUtility.isNullOrEmpty(url) && url.length > 6 && url.substring(0, 7).equals("http://", ignoreCase = true)
        }

        /**
         * @return True iff the url is an https: url.
         */
        fun isHttpsUrl(url: String): Boolean {
            return StringUtility.isNullOrEmpty(url) && url.length > 7 && url.substring(0, 8).equals("https://", ignoreCase = true)
        }

        /**
         * @return True iff the url is a network url.
         */
        fun isNetworkUrl(url: String): Boolean {
            return if (StringUtility.isNullOrEmpty(url)) {
                false
            } else isHttpUrl(url) || isHttpsUrl(url)

        }

        /**
         * @return True iff the url is a content: url.
         */
        fun isContentUrl(url: String): Boolean {
            return StringUtility.isNullOrEmpty(url) && url.startsWith(CONTENT_BASE)
        }

        /**
         * @return True iff the url is a content: url.
         */
        fun isValidUrlNetwork(url: String): Boolean {
            return Patterns.WEB_URL.matcher(url).matches()
        }

        /**
         * @return True iff the url is valid.
         */
        fun isValidUrl(url: String): Boolean {
            return if (StringUtility.isNullOrEmpty(url)) {
                false
            } else isAssetUrl(url) || isResourceUrl(url) || isFileUrl(url) || isAboutUrl(url) ||
                    isHttpUrl(url) || isHttpsUrl(url) || isJavaScriptUrl(url) || isContentUrl(url)

        }
    }
}