package com.example.isokratis;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView currentPitchText;
    private ToneGenerator toneGenerator;
    private Button currentlyPlayingButton = null;
    private ImageView backgroundImage;

    private final PitchData[] PITCHES = {
        new PitchData(R.id.upperPA_Button, "ΠΑ'", 293.66f),
        new PitchData(R.id.upperNH_Button, "ΝΗ'", 261.63f),
        new PitchData(R.id.ZW_Button, "ΖΩ", 242.23f),
        new PitchData(R.id.KE_Button, "ΚΕ", 220.00f),
        new PitchData(R.id.DI_Button, "ΔΙ", 196.00f),
        new PitchData(R.id.GA_Button, "ΓΑ", 174.61f),
        new PitchData(R.id.BOU_Button, "ΒΟΥ", 161.67f),
        new PitchData(R.id.PA_Button, "ΠΑ", 146.83f),
        new PitchData(R.id.NH_Button, "ΝΗ", 130.81f),
        new PitchData(R.id.zw_Button, "ζω", 121.12f)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentPitchText = findViewById(R.id.currentPitchText);
        backgroundImage = findViewById(R.id.backgroundImage);
        currentPitchText.setText("Current pitch: -");

        toneGenerator = new ToneGenerator();

        setupButtons();
    }

    private void setupButtons() {
        for (PitchData pitch : PITCHES) {
            Button button = findViewById(pitch.buttonId);
            button.setTag(pitch);
            button.setBackgroundResource(R.drawable.pitch_button_selector);
            button.setTextColor(getResources().getColorStateList(R.color.button_text_color, getTheme()));
            button.setOnClickListener(v -> handleButtonClick(button, (PitchData) button.getTag()));
        }
    }

    private void handleButtonClick(Button clickedButton, PitchData pitchData) {
        if (clickedButton == currentlyPlayingButton) {
            stopTone();
            return;
        }

        stopTone();
        toneGenerator.playTone(pitchData.frequency);
        currentlyPlayingButton = clickedButton;
        clickedButton.setSelected(true);
        updatePitchDisplay(pitchData);
    }

    private void stopTone() {
        if (currentlyPlayingButton != null) {
            currentlyPlayingButton.setSelected(false);
            toneGenerator.stopTone();
            currentlyPlayingButton = null;
        }
    }

    private void updatePitchDisplay(PitchData pitch) {
        currentPitchText.setText(getString(R.string.current_pitch_format,
                pitch.greekName, pitch.frequency));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toneGenerator.release();
    }

    private static class PitchData {
        int buttonId;
        String greekName;
        float frequency;

        PitchData(int buttonId, String greekName, float frequency) {
            this.buttonId = buttonId;
            this.greekName = greekName;
            this.frequency = frequency;
        }
    }
}
