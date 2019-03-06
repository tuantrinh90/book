package com.dz.utilities

import java.util.*

class Constant {
    companion object {
        // format date
        const val FORMAT_DATE_SERVER_RETURN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val FORMAT_YEAR_MONTH_DAY = "yyyy/MM/dd"

        // paging
        const val NUMBER_PER_PAGE = 20

        // language
        const val LANGUAGE_ENGLISH = "en"
        const val LANGUAGE_VIETNAMESE = "vi"
        const val DEFAULT_LANGUAGE = "DEFAULT_LANGUAGE"

        // time next screen
        const val TIME_DELAY_NEXT_SCREEN = 2L // seconds
        const val TIME_DELAY_INIT_SCREEN = 20L // miliseconds

        // toast
        const val TOAST_TIME = 2500L

        // video duration
        const val VIDEO_DURATION = 0 // seconds

        //key video
        const val VIDEO_PATH = "VIDEO_PATH"

        const val VIDEO_TOTAL_DURATION = "VIDEO_TOTAL_DURATION"

        //key contest
        const val CONTEST_ID = "CONTEST_ID"

        // user info
        const val SIGN_IN_RESPONSE = "sign_in_response"

        // status contest
        const val OPEN = 1
        const val CLOSED = 2

        const val APP_LINK_SHARE = "https://dieuphapam.net/dpa/nhan-gian-huu-tinh.4057/"

        // file type
        const val TYPE_IMAGE = 0
        const val TYPE_VIDEO = 1

        // sort
        const val SORT_NEWEST = "DESC"
        const val SORT_OLDEST = "ASC"

        const val FILTER_JOINED = "true"
        const val FILTER_NOT_JOINED_YET = "false"
        const val FILTER_ALL = ""

        // border image
        const val BORDER_IMAGE = 1

        // upload file type
        // root, default, contests_files, contests_videos, contests_video_60, submissions_files, submissions_videos, submissions_video_60
        const val UPLOAD_TYPE_FILES = "contests_files"

        const val UPLOAD_TYPE_VIDEOS = "contests_videos"
        const val UPLOAD_TYPE_VIDEOS_60 = "contests_video_60"
        const val UPLOAD_TYPE_SUBMISSIONS = "submissions_files"
        const val UPLOAD_TYPE_SUBMISSIONS_60 = "submissions_video_60"
        const val UPLOAD_NUMBER_IMAGE = 4
        const val UPLOAD_NUMBER_FRIEND = 5

        //youtube api
        const val API_KEY = "AIzaSyBCqBQmEq2SvrU1t4dvaA9QiaMdu26YYNg"

        const val KEY_INTENT_DETAIL = "INTENT_DETAIL"
    }
}
