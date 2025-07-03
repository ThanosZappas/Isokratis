# Pitch Player App – Specification

## Context

I am a musician (chanter) and I need a simple pitch-playing mobile app with the following features.

---

## Functionality

### 1. Buttons for Each Pitch (10 Total)

- Each button plays a specific frequency (see table below).
- Button behavior:
    - Pressing a button should play the assigned frequency in a loop or as a held tone.
    - Pressing the **same button again** stops the tone.
    - Pressing **another button** stops the current tone and starts the new one.
    - Closing the app should stop playback.

---

### 2. UI Behavior & Layout

- **Single-screen app**: Opens directly to the main screen.
- **Button layout**:
    - Arranged **vertically**, **lower frequencies at the bottom**, higher at the top.
    - Display pitch names on buttons using **Greek characters** (see below).
- **Button code naming**:
    - `zw_Button`, `NH_Button`, `PA_Button`, `BOU_Button`, `GA_Button`, `DI_Button`, `KE_Button`, `ZW_Button`, `upperNH_Button`, `upperPA_Button`
- **Pitch Label**:
    - A label (e.g., `TextView`) above the buttons should display the **last played pitch** in the format:  
      `Current Pitch: ΠΑ, 146.83 Hz`
    - This label should update dynamically with each button press.

---

### 3. Design

- Use a **dark theme**.
- Allow setting a **background wallpaper**, with options to adjust **opacity or blur**.
- UI should be **minimal, clean, and intuitive**.
- Buttons should be **visually appealing**, **modern**, and **clearly the main feature** of the app.

---

### 4. Offline Use

- The app should be fully functional **offline**.
- No internet connection or data usage required.

---

## Pitch Frequencies and Display Names

| Code Name        | Greek Label | Frequency  |
|------------------|-------------|------------|
| `zw_Button`      | Ζω          | 121.12 Hz  |
| `NH_Button`      | ΝΗ          | 130.81 Hz  |
| `PA_Button`      | ΠΑ          | 146.83 Hz  |
| `BOU_Button`     | ΒΟΥ         | 161.67 Hz  |
| `GA_Button`      | ΓΑ          | 174.61 Hz  |
| `DI_Button`      | ΔΙ          | 196.00 Hz  |
| `KE_Button`      | ΚΕ          | 220.00 Hz  |
| `ZW_Button`      | ΖΩ          | 242.23 Hz  |
| `upperNH_Button` | ΝΗ’         | 261.63 Hz  |
| `upperPA_Button` | ΠΑ’         | 293.66 Hz  |
