package com.example.pdfreader

import android.view.GestureDetector
import android.view.MotionEvent

class GestureListener(private val view: PDFimage) : GestureDetector.SimpleOnGestureListener() {
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        // Update the translation based on the scroll gesture
        view.translateX -= distanceX
        view.translateY -= distanceY

        view.invalidate()
        return true
    }
}
