package com.lexlock.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lexlock.domain.model.Word
import java.io.InputStreamReader

object AssetLoader {
    fun loadWordsFromJson(context: Context, fileName: String): List<Word> {
        return try {
            val inputStream = context.assets.open(fileName)
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<List<Word>>() {}.type
            Gson().fromJson(reader, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
