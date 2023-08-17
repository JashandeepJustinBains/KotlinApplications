package com.example.pdfreader

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView

@SuppressLint("AppCompatCustomView")
class PDFimage(context: Context?) : ImageView(context) {
    private val LOGNAME = "pdf_image"

    // able to take variables from MainActivity now
    private val mA = context as MainActivity

    // drawing path
    private var path: Path? = null
    private var paths = mA.getPaths()

    // image to display
    private var bitmap: Bitmap? = null

    // set new bitmap overtop that will store only pen paths
    private var easelBitmap: Bitmap? = null

    // set new bitmap overtop that will store only highlighter paths
    private var markerBitmap: Bitmap? = null

    // paints for different tools
    private val markerTip = Paint().apply {
        color = Color.YELLOW
        isAntiAlias = true
        style = Paint.Style.STROKE
        alpha = 128
        strokeWidth = 20.0F
    }
    private val penTip = Paint().apply {
        color = Color.DKGRAY
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 3.0F
    }
    private val eraseTip = Paint().apply {
        style = Paint.Style.STROKE
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        strokeWidth = 10.0F
    }

    // captures gestures
    private var zoomDetector = ScaleGestureDetector(context, ScaleListener(this))
    private var dragDetector = GestureDetector(context, GestureListener(this))

    var scaleFactor = 1f
    var translateX = 0f
    var translateY = 0f
    var centerX = 0f
    var centerY = 0f

    private val m = Matrix()
    private var orientationChanged = false


    // capture touch events (down/move/up) to create a path
    // and use that to create a stroke that we can draw
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        val x = (event.x - translateX - width / 2f) / scaleFactor + width / 2f
        val y = (event.y - translateY - height / 2f) / scaleFactor + height / 2f

        when (mA.getMode()) {
            1 -> {
                // set new variables that translate event.x/y so we can draw while zoomed in
                if (event.pointerCount == 2) {
                    zoomDetector.onTouchEvent(event)
                    dragDetector.onTouchEvent(event)
                }

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (event.pointerCount == 1) {
                            Log.d(LOGNAME, "Action down - PEN")
                            path = Path()
                            path!!.moveTo(x, y)
                            Log.d(LOGNAME, "start x=$x y=$y")
                        }
                    }

                    MotionEvent.ACTION_MOVE -> {
                        if (event.pointerCount == 1) {
                            Log.d(LOGNAME, "Action move - PEN")
                            path!!.lineTo(x, y)
                            // draws pen paths
                            penPath(path)
                            Log.d(LOGNAME, "END x=$x y=$y")
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        Log.d(LOGNAME, "Action up - PEN")
                        paths?.addPenPath(path)
                    }
                }
            }

            2 -> {
                if (event.pointerCount == 2) {
                    zoomDetector.onTouchEvent(event)
                    dragDetector.onTouchEvent(event)
                }
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (event.pointerCount == 1) {
                            Log.d(LOGNAME, "Action down - MARKER")
                            path = Path()
                            path!!.moveTo(x, y)
                        }
                    }

                    MotionEvent.ACTION_MOVE -> {
                        if (event.pointerCount == 1) {
                            Log.d(LOGNAME, "Action move - MARKER")
                            path!!.lineTo(x, y)
                            // draws highlighter paths
                            markerPath(path)
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        Log.d(LOGNAME, "Action up - MARKER")
                        paths?.addMarkerPath(path)
                        redrawAll()
                    }
                }
            }

            3 -> {
                if (event.pointerCount == 2) {
                    zoomDetector.onTouchEvent(event)
                    dragDetector.onTouchEvent(event)
                }
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (event.pointerCount == 1) {
                            Log.d(LOGNAME, "Action down - ERASE")
                            path = Path()
                            path!!.moveTo(x, y)
                        }
                    }

                    MotionEvent.ACTION_MOVE -> {
                        if (event.pointerCount == 1) {
                            Log.d(LOGNAME, "Action move - ERASE")
                            path!!.lineTo(x, y)
                            //draws paths that remove pixels underneath it
                            erasePath(path)
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        Log.d(LOGNAME, "Action up - ERASE")
                        paths?.addErasePath(path)
                    }
                }
            }

            else -> {
                // this is when we can pinch/zoom, pan and even switch pages
                if (event.pointerCount == 2) {
                    zoomDetector.onTouchEvent(event)
                    dragDetector.onTouchEvent(event)
                    return true
                }
            }
        }
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val visibleLeft = -translateX / scaleFactor
        val visibleTop = -translateY / scaleFactor
        val visibleRight = visibleLeft + width / scaleFactor
        val visibleBottom = visibleTop + height / scaleFactor
        val visibleCenterX = (visibleLeft + visibleRight) / 2
        val visibleCenterY = (visibleTop + visibleBottom) / 2

        // these 2 if statements only go off if the zoom is set to basic levels because we
        //   want to pan to width if there is no zoom changes or pan changes
        if ((mA.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) &&
            (scaleFactor == 1f)
        ) {
            Log.d(
                LOGNAME,
                "IF1: translateX =$translateX translateY=$translateY scaleFactor =$scaleFactor and" +
                        "\n orientation= ${mA.resources.configuration.orientation}"
            )
            scaleFactor = 2.35f

            centerX = translateX
            centerY = if (translateY == 0f) {
                1090f
            } else {
                1090f - visibleCenterY
            }
            //height / scaleFactor - translateY
            translateX = centerX
            translateY = centerY
        } else if ((mA.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) &&
            (scaleFactor == 2.35f) || (scaleFactor == 1f)
        ) {
            Log.d(
                LOGNAME,
                "IF2: translateX =$translateX translateY=$translateY scaleFactor =$scaleFactor and" +
                        "\n orientation= ${mA.resources.configuration.orientation}"
            )
            scaleFactor = 1f


            centerX = translateX
            centerY = 0f

            translateX = centerX
            translateY = centerY

        } else {
            if (mA.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.d(
                    LOGNAME,
                    "IF3: translateX =$translateX translateY=$translateY scaleFactor =$scaleFactor and" +
                            "\n orientation= ${mA.resources.configuration.orientation}"
                )


                centerX = -(visibleCenterX * scaleFactor - width / 2)
                centerY = -(visibleCenterY * scaleFactor - height / 2)
                translateX = centerX
                translateY = centerY
            } else {
                Log.d(
                    LOGNAME,
                    "IF4: translateX =$translateX translateY=$translateY scaleFactor =$scaleFactor and" +
                            "\n orientation= ${mA.resources.configuration.orientation}"
                )
                centerX = -(visibleCenterX * scaleFactor - width / 2)
                centerY = -(visibleCenterY * scaleFactor - height / 2)
                translateX = centerX
                translateY = centerY
            }
        }
        Log.d(
            LOGNAME,
            "visibleLeft    = $visibleLeft \n" +
                    "visibleTop      = $visibleTop \n" +
                    "visibleRight    = $visibleRight \n" +
                    "visibleBottom   = $visibleBottom \n" +
                    "visibleCenterX  = $visibleCenterX \n" +
                    "visibleCenterY  = $visibleCenterY "
        )

        easelBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        markerBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        orientationChanged = true
        redrawAll()

    }

    // set image as background
    fun setImage(bitmap: Bitmap?) {
        this.bitmap = bitmap

        // when a new bitmap is loaded I want to set all the old bitmaps to new ones to clear them
        if (width != 0 && height != 0) {
            easelBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            markerBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }
        paths = mA.getPaths()
        redrawAll()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        // draw background
        canvas.save()

        if (bitmap != null) {
            setImageBitmap(bitmap)
        }

        // Translate the canvas to center the zoom around the middle of the screen
        canvas.translate(width / 2f + translateX, height / 2f + translateY)
        canvas.scale(scaleFactor, scaleFactor)
        // Translate the canvas back to its original position
        canvas.translate(-width / 2f, -height / 2f)

        super.onDraw(canvas)

        canvas.drawBitmap(easelBitmap!!, 0f, 0f, null)
        canvas.drawBitmap(markerBitmap!!, 0f, 0f, null)

        canvas.restore()

    }

    // function to draw paths of pen/highlighter/eraser on a page
    private fun markerPath(path: Path?) {
        val easel = Canvas(markerBitmap!!)
        if (path != null) {
            easel.drawPath(path, markerTip)
        }
        invalidate()
    }

    private fun erasePath(path: Path?) {
        if (path != null) {
            Canvas(easelBitmap!!).drawPath(path, eraseTip)
            Canvas(markerBitmap!!).drawPath(path, eraseTip)
        }
        invalidate()
    }

    /*
    private fun penPath() {
    val easel = Canvas(easelBitmap!!)
    val path = paths?.getLastPen()
    if (path != null) {
    easel.drawPath(path, penTip)
    }
    invalidate()
    }
    */
    private fun penPath(path: Path?) {
        val easel = Canvas(easelBitmap!!)
        if (path != null) {
            easel.drawPath(path, penTip)
        }
        invalidate()
    }

    private fun redrawAll() {

        if (easelBitmap != null && markerBitmap != null) {
            val penCanvas = Canvas(easelBitmap!!)
            val markerCanvas = Canvas(markerBitmap!!)
            penCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            markerCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

            if (orientationChanged) {
                if (mA.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    m.reset()
                    penCanvas.setMatrix(m)
                    markerCanvas.setMatrix(m)
                    orientationChanged = false

                } else {
                    m.setScale(5f, 1f)
                    m.setTranslate(200f, -200f)
                    penCanvas.setMatrix(m)
                    markerCanvas.setMatrix(m)
                    orientationChanged = false

                }
            }
            var i = 0
            paths?.getAllPaths()?.forEach {
                if (it != null) {
                    if (paths?.getPathOrder()?.get(i) == 1) {
                        penCanvas.drawPath(it, penTip)
                    } else if (paths?.getPathOrder()?.get(i) == 2) {
                        markerCanvas.drawPath(it, markerTip)
                    } else if (paths?.getPathOrder()?.get(i) == 3) {
                        penCanvas.drawPath(it, eraseTip)
                        markerCanvas.drawPath(it, eraseTip)
                    }
                    i += 1
                }
            }

        }
    }

    fun undo() {
        paths?.undo()
        redrawAll()
    }

    fun redo() {
        paths?.redo()
        redrawAll()
    }

}
