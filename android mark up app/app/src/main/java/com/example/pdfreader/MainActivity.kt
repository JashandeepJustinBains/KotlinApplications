package com.example.pdfreader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

// PDF sample code from
// https://medium.com/@chahat.jain0/rendering-a-pdf-document-in-android-activity-fragment-using-pdfrenderer-442462cb8f9a
// Issues about cache etc. are not at all obvious from documentation, so we should expect people to need this.
// We may wish to provide this code.
class MainActivity : AppCompatActivity() {
    private val LOGNAME = "pdf_viewer"
    private val FILENAME = "shannon1948.pdf"
    private val FILERESID = R.raw.shannon1948

    // manage the pages of the PDF, see below
    private lateinit var pdfRenderer: PdfRenderer
    private lateinit var parcelFileDescriptor: ParcelFileDescriptor
    private var currentPage: PdfRenderer.Page? = null

    // custom ImageView class that captures strokes and draws them over the image
    private lateinit var pageImage: PDFimage

    // updates page number TextView
    private var pageNumber: Int = 0

    // keep track of what drawing mode we are in, PEN, MARKER, ERASER
    private var mode: Int = 0

    // create a list of paths for each page in the document so we can switch to and fro
    // everytime a page is changed
    private var drawnPaths = mutableListOf<DrawnPaths>()
    private var curDrawnPage: DrawnPaths? = null

    override fun onSaveInstanceState(outState: Bundle) {
        // this is to keep the page number the same when we switch orientations
        super.onSaveInstanceState(outState)
        outState.putInt("pageNumber", pageNumber)
        outState.putParcelableArrayList("drawnPathsList", ArrayList(drawnPaths))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layout = findViewById<LinearLayout>(R.id.pdfLayout)
        layout.isEnabled = true

        if (savedInstanceState == null) {
            // initialize all path holding variables for each page
            for (i in 0 until 55) {
                val newDrawing = DrawnPaths()
                drawnPaths.add(newDrawing)
            }
            setPaths()

            pageImage = PDFimage(this)

            layout.addView(pageImage)

            pageImage.minimumWidth = 1550
            pageImage.minimumHeight = 2000
            // open page 0 of the PDF
            // it will be displayed as an image in the pageImage (above)
            try {
                openRenderer(this)
                showPage(pageNumber)
            } catch (exception: IOException) {
                Log.d(LOGNAME, "Error opening PDF")
            }

        } else {
            // reset variables to previous savedInstantState before orientation change
            pageNumber = savedInstanceState.getInt("pageNumber", 0)
            drawnPaths = savedInstanceState.getParcelableArrayList("drawnPathsList")!!

            pageImage = PDFimage(this)
            layout.addView(pageImage)

            pageImage.minimumWidth = 1550
            pageImage.minimumHeight = 2000
            // open page 0 of the PDF
            // it will be displayed as an image in the pageImage (above)
            try {
                openRenderer(this)
                showPage(pageNumber)
            } catch (exception: IOException) {
                Log.d(LOGNAME, "Error opening PDF")
            }

        }

        // set the title of the app
        findViewById<TextView>(R.id.TitleBar)?.text = FILENAME

        // initialize all buttons and currently activated mode
        val penMode = findViewById<Button>(R.id.penMode)
        val highlighterMode = findViewById<Button>(R.id.highlighterMode)
        val eraseMode = findViewById<Button>(R.id.eraseMode)
        penMode.setOnClickListener {
            if (mode == 1) {
                mode = 0
                penMode.setBackgroundColor(Color.TRANSPARENT)
                highlighterMode.setBackgroundColor(Color.TRANSPARENT)
                eraseMode.setBackgroundColor(Color.TRANSPARENT)
            } else {
                mode = 1
                penMode.setBackgroundColor(Color.LTGRAY)
                highlighterMode.setBackgroundColor(Color.TRANSPARENT)
                eraseMode.setBackgroundColor(Color.TRANSPARENT)
            }
        }
        highlighterMode.setOnClickListener {
            if (mode == 2) {
                mode = 0
                penMode.setBackgroundColor(Color.TRANSPARENT)
                highlighterMode.setBackgroundColor(Color.TRANSPARENT)
                eraseMode.setBackgroundColor(Color.TRANSPARENT)

            } else {
                mode = 2
                penMode.setBackgroundColor(Color.TRANSPARENT)
                highlighterMode.setBackgroundColor(Color.LTGRAY)
                eraseMode.setBackgroundColor(Color.TRANSPARENT)
            }

        }
        eraseMode.setOnClickListener {
            if (mode == 3) {
                mode = 0
                penMode.setBackgroundColor(Color.TRANSPARENT)
                highlighterMode.setBackgroundColor(Color.TRANSPARENT)
                eraseMode.setBackgroundColor(Color.TRANSPARENT)

            } else {
                mode = 3
                penMode.setBackgroundColor(Color.TRANSPARENT)
                highlighterMode.setBackgroundColor(Color.TRANSPARENT)
                eraseMode.setBackgroundColor(Color.LTGRAY)
            }
        }

        val undoButton = findViewById<ImageButton>(R.id.undoButton)
        val redoButton = findViewById<ImageButton>(R.id.redoButton)
        undoButton.setOnClickListener {
            if ( curDrawnPage?.getUndoable() == 0 ) {
                Toast.makeText(this, "Nothing to Undo", Toast.LENGTH_SHORT).show()
            } else {
                pageImage.undo()
            }
        }
        redoButton.setOnClickListener {
            if (curDrawnPage?.getRedoable() == 0) {
                Toast.makeText(this, "Nothing to Redo", Toast.LENGTH_SHORT).show()
            } else {
                pageImage.redo()
            }
        }

        // handle changing the page and updating the TextView that displays the curPage/totalPage
        val prevButton = findViewById<ImageButton>(R.id.prevPageButton)
        val nextButton = findViewById<ImageButton>(R.id.nextPageButton)
        nextButton.setOnClickListener { nextPage() }
        prevButton.setOnClickListener { prevPage() }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            closeRenderer()
        } catch (ex: IOException) {
            Log.d(LOGNAME, "Unable to close PDF renderer")
        }
    }

    @Throws(IOException::class)
    private fun openRenderer(context: Context) {
        // In this sample, we read a PDF from the assets directory.
        val file = File(context.cacheDir, FILENAME)
        if (!file.exists()) {
            // pdfRenderer cannot handle the resource directly,
            // so extract it into the local cache directory.
            val asset = this.resources.openRawResource(FILERESID)
            val output = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var size: Int
            while (asset.read(buffer).also { size = it } != -1) {
                output.write(buffer, 0, size)
            }
            asset.close()
            output.close()
        }
        parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)

        // capture PDF data
        // all this just to get a handle to the actual PDF representation
        pdfRenderer = PdfRenderer(parcelFileDescriptor)
    }

    // do this before you quit!
    @Throws(IOException::class)
    private fun closeRenderer() {
        currentPage?.close()
        pdfRenderer.close()
        parcelFileDescriptor.close()
    }

    private fun showPage(index: Int) {
        val pageCount = pdfRenderer.pageCount
        if (pageCount <= index) {
            return
        }

        val pageNum = findViewById<TextView>(R.id.pageNumber)
        pageNum.text = buildString {
            append("Page ")
            append(pageNumber + 1)
            append(" / ")
            append(pageCount)
        }
        setPaths()

        // Close the current page before opening another one.
        currentPage?.close()
        // Use `openPage` to open a specific page in PDF.
        currentPage = pdfRenderer.openPage(index)

        if (currentPage != null) {
            // Important: the destination bitmap must be ARGB (not RGB).
            val bitmap : Bitmap = Bitmap.createBitmap(
                    2720, 3520,
                    Bitmap.Config.ARGB_8888
                )
            Log.d(LOGNAME, "w=${resources.displayMetrics.densityDpi * currentPage!!.width / 72} h=${resources.displayMetrics.densityDpi * currentPage!!.height / 72}")
            // Here, we render the page onto the Bitmap.
            // To render a portion of the page, use the second and third parameter. Pass nulls to get the default result.
            // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
            currentPage!!.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            // Display the page
            pageImage.setImage(bitmap)
        }
    }
    fun nextPage() {
        try {
            if (pageNumber + 1 < pdfRenderer.pageCount) {
                pageNumber += 1
                showPage(pageNumber)
            }
        } catch (exception: IOException) {
            prevPage()
            pageNumber -= 1
            Log.d(LOGNAME, "Error opening PDF")
        }
    }
    fun prevPage() {
        try {
            if (pageNumber - 1 >= 0) {
                pageNumber -= 1
                showPage(pageNumber)
            }
        } catch (exception: IOException) {
            // if we cannot open previous page do not change page Number
            pageNumber += 1
            Log.d(LOGNAME, "Error opening PDF")
        }
    }

    fun getMode(): Int { return mode }

    private fun setPaths() { curDrawnPage = drawnPaths[pageNumber] }

    fun getPaths(): DrawnPaths? { return curDrawnPage }

}