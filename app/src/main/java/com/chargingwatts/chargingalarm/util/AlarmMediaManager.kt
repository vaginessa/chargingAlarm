package com.chargingwatts.chargingalarm.util

import android.content.Context
import android.content.ContextWrapper
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AlarmMediaManager @Inject constructor(context: Context) : ContextWrapper(context.applicationContext) {
    lateinit var audioManager:AudioManager
    init {
        audioManager  = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private var mMediaPlayer: MediaPlayer? = null



    fun playAlarmSound(uri: Uri, playVolume: Int) {
        if (uri === Uri.EMPTY) {
            Log.w(AlarmMediaManager::class.java.name, "playRingtone: Uri is null or empty.")
            return
        }
        if (mMediaPlayer == null) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, playVolume, 0)
            when (audioManager.getRingerMode()) {
                AudioManager.RINGER_MODE_SILENT -> Log.i("MyApp", "Silent mode")
                AudioManager.RINGER_MODE_VIBRATE -> Log.i("MyApp", "Vibrate mode")
                AudioManager.RINGER_MODE_NORMAL -> Log.i("MyApp", "Normal mode")
            }
            mMediaPlayer = MediaPlayer()
        }

        mMediaPlayer?.apply {
            try {
//                setOnCompletionListener {
//                        it.reset()
//                    setDataSource(this@AlarmMediaManager,uri)
//                    prepare()
//                    start()
//                }
                if (isPlaying) {
                    return
//                    stop()
                }

                reset()
                setDataSource(this@AlarmMediaManager, uri)
                prepare()
                start()
                isLooping = true

            } catch (exception: IOException) {
                Log.e(AlarmMediaManager::class.java.canonicalName, " Exception ", exception)

            } catch (exception: IllegalArgumentException) {
                Log.e(AlarmMediaManager::class.java.canonicalName, " Exception ", exception)

            } catch (exception: SecurityException) {
                Log.e(AlarmMediaManager::class.java.canonicalName, " Exception ", exception)

            } catch (exception: IllegalStateException) {
                Log.e(AlarmMediaManager::class.java.canonicalName, " Exception ", exception)

            }

        }

    }

    fun stopAlarmSound() {
        mMediaPlayer?.apply {
            if (isPlaying)
                stop()
            release()
            mMediaPlayer = null
        }
    }


    fun getRingerMode():Int = audioManager.ringerMode

    fun getMaxVoulume():Int = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)



}