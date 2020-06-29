/*
 * Copyright (c) 2020 System Level Solutions (India) Pvt. Ltd. All rights reserved.
 * Created by  Jainesh.Desai.550 on 14/4/20 2:32 PM
 */

package com.example.nasaphotooftheday.defs

import androidx.annotation.StringDef

class MediaTypeDef{

    companion object {
        @StringDef(
            IMAGE,
            VIDEO
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class MessageType

        const val IMAGE: String = "image"
        const val VIDEO: String = "video"

    }

    @MessageType
    private var sensorType: String = ""

    fun setMediaType(@MessageType message: String) {
        this.sensorType = message
    }
}