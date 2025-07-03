package com.example.isokratis;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;

public class ToneGenerator {
    private static final int SAMPLE_RATE = 44100;
    private boolean isPlaying = false;
    private AudioTrack audioTrack = null;
    private Thread playThread = null;

    public void playTone(float frequency) {
        stopTone();

        isPlaying = true;

        // Start a new thread to avoid blocking the UI
        playThread = new Thread(() -> generateTone(frequency));
        playThread.start();
    }

    public void stopTone() {
        isPlaying = false;

        if (audioTrack != null) {
            try {
                audioTrack.stop();
                audioTrack.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            audioTrack = null;
        }

        if (playThread != null) {
            try {
                playThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            playThread = null;
        }
    }

    private void generateTone(float frequency) {
        int bufferSize = AudioTrack.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        audioTrack = new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build())
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build())
                .setBufferSizeInBytes(bufferSize)
                .build();

        audioTrack.play();

        // Generate a sine wave
        short[] buffer = new short[bufferSize];
        double angleIncrement = 2 * Math.PI * frequency / SAMPLE_RATE;
        double angle = 0;

        // Keep generating audio as long as we're playing
        while (isPlaying) {
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (short) (Short.MAX_VALUE * Math.sin(angle));
                angle += angleIncrement;
                // Keep angle in a reasonable range to avoid floating point errors
                if (angle > 2 * Math.PI) {
                    angle -= 2 * Math.PI;
                }
            }

            // Write the audio data to the track
            if (audioTrack != null && isPlaying) {
                audioTrack.write(buffer, 0, buffer.length);
            }
        }
    }

    public void release() {
        stopTone();
    }
}
