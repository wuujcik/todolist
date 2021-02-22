package com.wuujcik.todolist.model

import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import java.io.InputStream
import java.net.URL

class ImageProvider {

    private val handlerThread = HandlerThread("IMAGE_PROVIDER_HANDLER_THREAD")

    init {
        handlerThread.start()
    }

    fun loadImageFromWebOperations(
        url: String?,
        completion: ((drawable: Drawable?, exception: String?) -> Unit)
    ) {


        val origLooper = Looper.myLooper()
            ?: Looper.getMainLooper()
        Handler(handlerThread.looper).post {
            try {
                val inputStream = URL(url).content as InputStream
                val drawable: Drawable = Drawable.createFromStream(inputStream, "src")
                origLooper.run {
                    completion(drawable, null)
                }
            } catch (e: java.lang.Exception) {
                Log.e(TAG, "Exception: ${e.message}")
                origLooper.run {
                    completion(null, e.message)
                }
            }
        }
    }


    companion object {
        const val TAG = "ImageProvider"
    }
}