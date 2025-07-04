package com.example.isokratis;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;

public class ToneGenerator {
    private static final int SAMPLE_RATE = 44100; // Standard quality sample rate
    private static final float AMPLITUDE = 0.8f; // Base amplitude (80% of max)
    private static final float FADE_DURATION_SEC = 0.3f; // Fade-in/out duration
    private static final double[] HARMONIC_AMPLITUDES = {1.0, 0.5, 0.25, 0.125}; // Harmonics with decreasing amplitude
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

        short[] buffer = new short[bufferSize];
        double angle = 0;
        double angleIncrement = 2 * Math.PI * frequency / SAMPLE_RATE;

        int fadeInSamples = (int) (FADE_DURATION_SEC * SAMPLE_RATE);
        int sampleCount = 0;

        while (isPlaying) {
            for (int i = 0; i < buffer.length; i++) {
                double sample = 0;

                // Generate fundamental frequency and harmonics
                for (int h = 0; h < HARMONIC_AMPLITUDES.length; h++) {
                    double harmonicAngle = angle * (h + 1);
                    sample += Math.sin(harmonicAngle) * HARMONIC_AMPLITUDES[h];
                }

                // Normalize the sample
                sample /= HARMONIC_AMPLITUDES.length;

                // Apply fade-in and base amplitude
                float fadeMultiplier = sampleCount < fadeInSamples ?
                        (float) sampleCount / fadeInSamples : 1.0f;
                sample *= AMPLITUDE * fadeMultiplier;

                // Convert to 16-bit PCM
                buffer[i] = (short) (Short.MAX_VALUE * sample);
                sampleCount++;
                angle += angleIncrement;
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
