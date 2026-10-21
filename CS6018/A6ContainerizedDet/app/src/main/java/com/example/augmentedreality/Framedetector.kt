package com.example.augmentedreality

import androidx.camera.core.ImageProxy

/**
 * The seam both models implement. The analyzer doesn't know or care which one is active.
 *
 * Contract:
 *  - Call onFinished EXACTLY ONCE per frame (null on failure). The analyzer closes the
 *    frame inside that callback, so never touch [image] after calling it.
 *  - Boxes in the result must be normalized (0..1) in upright (already rotated) space.
 *  - detect() may run synchronously or asynchronously.
 */
interface FrameDetector {
    val type: ModelType
    fun detect(image: ImageProxy, onFinished: (DetectionResult?) -> Unit)
    fun close()
}