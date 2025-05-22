package com.agile.endofdarknes

import android.content.Context
import org.json.JSONObject

class StoryRepository(private val context: Context) {
    fun loadStory(): JSONObject? {
        return try {
            val inputStream = context.assets.open("story.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            JSONObject(String(buffer, Charsets.UTF_8))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}