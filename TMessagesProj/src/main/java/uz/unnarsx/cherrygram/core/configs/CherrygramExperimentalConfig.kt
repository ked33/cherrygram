/**
 * This is the source code of Cherrygram for Android.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Please, be respectful and credit the original author if you use this code.
 *
 * Copyright github.com/arsLan4k1390, 2022-2026.
 */

package uz.unnarsx.cherrygram.core.configs

import android.app.Activity
import android.content.SharedPreferences
import org.telegram.messenger.ApplicationLoader
import uz.unnarsx.cherrygram.preferences.boolean
import uz.unnarsx.cherrygram.preferences.int

object CherrygramExperimentalConfig {

    const val HLS_QUALITY_AUTO = 0
    const val HLS_QUALITY_ORIGINAL = 1
    const val HLS_QUALITY_1440 = 2
    const val HLS_QUALITY_1080 = 3
    const val HLS_QUALITY_720 = 4
    const val HLS_QUALITY_LOW = 5
    const val VIDEO_DECODER_HARDWARE = 0
    const val VIDEO_DECODER_PREFER_HARDWARE = 1
    const val VIDEO_DECODER_PREFER_SOFTWARE = 2

    private val sharedPreferences: SharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE)

    var sendMp4DocumentAsVideo by sharedPreferences.boolean("EP_SendMp4DocumentAsVideo", true)
    var defaultHlsVideoQuality by sharedPreferences.int("EP_DefaultHlsVideoQuality", HLS_QUALITY_AUTO)
    var videoPlayerDecoder by sharedPreferences.int("EP_VideoPlayerDecoder", VIDEO_DECODER_PREFER_SOFTWARE)
    var enhancedVideoBitrate by sharedPreferences.boolean("EP_EnhancedVideoBitrate", true)
    var showSmallGif by sharedPreferences.boolean("EP_ShowSmallGif", false)
    var showCopyVideoFrame by sharedPreferences.boolean("EP_ShowCopyVideoFrame", true)

}
