package com.dz.utilities

class ServiceConfig {
    companion object {
        // base url
        const val URL = "http://13.229.60.66:8888"
        const val BASE_URL = "$URL/"

        // retry policy
        const val RETRY_POLICY = true

        // timeout
        const val REQUEST_FILE_TIMEOUT = 100
        const val REQUEST_TIMEOUT_LONG = 60
        const val REQUEST_TIMEOUT = 30

        // client id
        const val GRANT_TYPE = "password"
        const val CLIENT_ID = 1

        const val SIGN_IN = "api/v1/auth/login"
        const val CONTESTS = "api/v1/contests"
        const val HASHTAGS = "api/v1/hashtag"
        const val FRIENDS = "api/v1/members/get-friend"
        const val UPLOAD_FILE = "api/v1/media/upload/{type}"
        const val SUBMISSTION_FILE = "api/v1/submissions"
    }
}