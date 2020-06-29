/*
 * Created by  Jainesh.Desai.550 on 14/4/20 2:32 PM
 */
package com.example.nasaphotooftheday.utils

import android.os.Environment
import android.util.Log
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class Logging private constructor() {
    private val APP_FOLDER_NAME = "NPOHD"
    private val sdf =
        SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    @Synchronized
    fun logToFile(isSendData: Boolean, data: String) {
        if (storeData) {
            try {
                val saveDir = File(
                    Environment.getExternalStorageDirectory()
                        .toString() + "/" + APP_FOLDER_NAME + "/log"
                )
                if (!saveDir.exists()) if (!saveDir.mkdirs()) {
                    Log.i(
                        TAG,
                        "logToFile: Log not stored to file as unable to create directory"
                    )
                }
                val saveFile = File(
                    saveDir,
                    String.format(
                        "log_%s.txt",
                        timeStamp
                    )
                )
                //Log.e("tag", "filePath = " + saveDir.getAbsolutePath() + "/" + "log.txt");
                val writer =
                    BufferedOutputStream(FileOutputStream(saveFile, true))
                val currentDateAndTime = sdf.format(Date())
                writer.write("\n\n==================\n".toByteArray())
                writer.write(if (isSendData) "Sent data: ".toByteArray() else "Received data: ".toByteArray())
                val currentFrameTime = "$currentDateAndTime : \n"
                writer.write(currentFrameTime.toByteArray())
                writer.write(data.toByteArray())
                writer.flush()
                writer.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @Synchronized
    fun firmwareUpdateLogToFile(data: String) {
        logToFile("Firmware update flow", data)
    }

    @Synchronized
    fun logToFile(label: String, data: String) {
        if (storeData) {
            try {
                val saveDir = File(
                    Environment.getExternalStorageDirectory()
                        .toString() + "/" + APP_FOLDER_NAME + "/log"
                )
                Log.e(
                    TAG,
                    "logToFile: " + saveDir.absolutePath
                )
                if (!saveDir.exists()) if (!saveDir.mkdirs()) {
                    Log.i(
                        TAG,
                        "logToFile: Log not stored to file as unable to create directory"
                    )
                }
                val saveFile = File(
                    saveDir,
                    String.format(
                        "log_%s.txt",
                        timeStamp
                    )
                )
                //Log.e("tag", "filePath = " + saveDir.getAbsolutePath() + "/" + "log.txt");
                val writer =
                    BufferedOutputStream(FileOutputStream(saveFile, true))
                val currentDateAndTime = sdf.format(Date())
                writer.write("\n\n==================\n".toByteArray())
                writer.write(("$label : ").toByteArray())
                val currentFrameTime = "$currentDateAndTime : \n"
                writer.write(currentFrameTime.toByteArray())
                writer.write(data.toByteArray())
                writer.flush()
                writer.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun v(TAG: String?, msg: String) {
        try {
            if (LOG_ENABLE) {
                Log.v(TAG, buildMsg(msg))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun d(TAG: String?, msg: String) {
        try {
            if (LOG_ENABLE) {
                Log.d(TAG, buildMsg(msg))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun i(TAG: String?, msg: String) {
        try {
            if (LOG_ENABLE && Log.isLoggable(
                    TAG,
                    Log.INFO
                )
            ) {
                Log.i(TAG, buildMsg(msg))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun w(TAG: String?, msg: String) {
        try {
            if (LOG_ENABLE && Log.isLoggable(
                    TAG,
                    Log.WARN
                )
            ) {
                Log.w(TAG, buildMsg(msg))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun w(TAG: String?, msg: String, e: Exception?) {
        try {
            if (LOG_ENABLE && Log.isLoggable(
                    TAG,
                    Log.WARN
                )
            ) {
                Log.w(TAG, buildMsg(msg), e)
            }
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
    }

    fun e(TAG: String?, msg: String) {
        try {
            if (LOG_ENABLE && Log.isLoggable(
                    TAG,
                    Log.ERROR
                )
            ) {
                Log.e(TAG, buildMsg(msg))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun e(TAG: String?, msg: String, e: Exception?) {
        if (LOG_ENABLE && Log.isLoggable(
                TAG,
                Log.ERROR
            )
        ) {
            Log.e(TAG, buildMsg(msg), e)
        }
    }

    private fun buildMsg(msg: String): String {
        val buffer = StringBuilder()
        if (DETAIL_ENABLE) {
            val stackTraceElement =
                Thread.currentThread().stackTrace[4]
            buffer.append("[ ")
            buffer.append(Thread.currentThread().name)
            buffer.append(": ")
            buffer.append(stackTraceElement.fileName)
            buffer.append(": ")
            buffer.append(stackTraceElement.lineNumber)
            buffer.append(": ")
            buffer.append(stackTraceElement.methodName)
        }
        buffer.append("() ] _____ ")
        buffer.append(msg)
        return buffer.toString()
    }

    companion object {
        private const val TAG = "Logging"
        private const val LOG_ENABLE = true
        private const val DETAIL_ENABLE = true
        private const val storeData = true
        private var timeStamp: String? = null
        private var logging: Logging? = null

        @get:Synchronized
        val instance: Logging?
            get() = if (logging == null) {
                timeStamp =
                    SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Date())
                Logging()
                    .also { logging = it }
            } else logging
    }
}