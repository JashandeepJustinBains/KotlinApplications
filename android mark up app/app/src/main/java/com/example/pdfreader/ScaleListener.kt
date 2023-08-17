package com.example.pdfreader

import android.view.ScaleGestureDetector

// TODO scale factor should also affect drawn paths, currently a drawn path just scales on the screen.
//  instead the screen should scale and the paths should stay the same sizev cgfr
class ScaleListener(private val view: PDFimage) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
    override fun onScale(detector: ScaleGestureDetector): Boolean {
        view.scaleFactor *= detector.scaleFactor
        view.translateX += (detector.focusX - view.width  / 2f - view.translateX ) * (1 - detector.scaleFactor)
        view.translateY += (detector.focusY - view.height / 2f - view.translateY ) * (1 - detector.scaleFactor)

        view.invalidate()
        return true
    }
}