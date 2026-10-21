package com.example.augmentedreality

/**
 * Where the photo server lives. Users never see or type this; change it here and rebuild.
 *
 * Pick the value that matches how you're running the app:
 *
 *   "http://localhost:8080"    Phone connected by USB. Run this once in a terminal (and again
 *                              if you unplug/reconnect the phone):
 *                                  adb reverse tcp:8080 tcp:8080
 *                              Works on the emulator too, and survives Wi-Fi/IP changes.
 *   "http://10.0.2.2:8080"     Android emulator, without adb reverse.
 *   "http://192.168.1.20:8080" Phone on the same Wi-Fi as your computer (use your computer's
 *                              real address: `ipconfig getifaddr en0` on a Mac).
 *
 * A deployed app would use a fixed https:// address here.
 */
const val SERVER_URL = "http://10.0.2.2:8080"