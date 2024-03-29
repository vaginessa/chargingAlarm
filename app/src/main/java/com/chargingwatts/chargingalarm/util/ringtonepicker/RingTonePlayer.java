/*
 * Copyright 2017 Keval Patel
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance wit
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 *  the specific language governing permissions and limitations under the License.
 */

package com.chargingwatts.chargingalarm.util.ringtonepicker;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Keval on 29-Mar-17.
 * This class plays the ringtone sample whenever user selects any ringtone from the list.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */
public final class RingTonePlayer implements Closeable {

    @NonNull
    private final Context mContext;

    /**
     * Media player for the ringtone.
     */
    @NonNull
    private final MediaPlayer mMediaPlayer;

    /**
     * Public constructor.
     */
    public RingTonePlayer(@NonNull final Context context) {
        mContext = context;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setLooping(true);
    }



    /**
     * Play the ringtone for the given uri.
     *
     * @param uri uri of the ringtone to play.
     * @throws IOException if it cannot play the ringtone.
     */
    public void playRingtone(@Nullable final Uri uri) throws IOException,
            IllegalArgumentException,
            SecurityException,
            IllegalStateException {

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.reset();

        if (uri == null || uri == Uri.EMPTY) {
            Log.w(RingTonePlayer.class.getName(), "playRingtone: Uri is null or empty.");
            return;
        }

        mMediaPlayer.setDataSource(mContext, uri);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
    }

    public void stopRingtone(){
        if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
    }
    /**
     * Release the {@link MediaPlayer} instance. Remember to call this method in on destroy.
     */
    @Override
    public void close() {
        if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
        mMediaPlayer.release();
    }
}
