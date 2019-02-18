package com.dz.libraries.utilities

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonUtility {
    fun getGsonBuilder(): Gson = GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setPrettyPrinting()
            .create()
}