package com.kc.newsreader.util

import android.content.Context
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Created by changk on 1/19/18.
 */
object TestUtil {

    fun getStringFromFile(context: Context, path: String): String {
        val inputStream = context.assets.open(path)
        val json = convertStreamToString(inputStream)
        inputStream.close()
        return json
    }

    fun getStringFromFile(path: String): String {
        return convertStreamToString(this.javaClass.classLoader.getResourceAsStream(path))
    }

    fun convertStreamToString(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
        var line: String? = null
        while ({line = reader.readLine(); line}() != null) {
            sb.append(line).append("\n")
        }
        reader.close()
        return sb.toString()
    }
}