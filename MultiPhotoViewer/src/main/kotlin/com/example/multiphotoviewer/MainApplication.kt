package com.example.multiphotoviewer

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import com.example.multiphotoviewer.model.Model
import com.example.multiphotoviewer.view.*
import javafx.beans.property.ReadOnlyIntegerWrapper
import javafx.beans.value.ChangeListener
import javafx.scene.control.ScrollPane
import javafx.scene.layout.BorderPane

class MainApplication : Application() {
    private var stag: Stage? = null
    private var bp : BorderPane? = null
    private val whichView = ReadOnlyIntegerWrapper(0)
    private val myModel : Model = Model(whichView)
    private lateinit var myCascadeView : CascadeView
    private lateinit var myTileView : TileView
    private lateinit var myScrollPane : ScrollPane
    private val myController : ToolBarView = ToolBarView(myModel)
    override fun start(stage: Stage) {

        val ear = ChangeListener { _, _, newValue -> changed(newValue) }
        whichView.addListener(ear)
        val myLabel = ActiveLabelView(myModel)

        myCascadeView = CascadeView(myModel)
        myScrollPane = ScrollPane()
        myScrollPane.content = myCascadeView

        myScrollPane.setOnMousePressed{
            println("we are clicking on the CascadeView background")
            myModel.resetSelected()
        }

        stage.apply {
            title = "PhotoViewer: Justin Bains"
            this.height = 800.0// 1200.0
            this.width = 1300.0// 1600.0
            this.minHeight = 600.0
            this.minWidth = 1000.0
            bp = BorderPane()
            bp!!.top = myController
            bp!!.bottom = myLabel
            bp!!.center = myScrollPane
            bp!!.left = null
            bp!!.right = null
            scene = Scene(bp)
        }.show()
        stag = stage

        myCascadeView.prefWidth = stage.width
        myScrollPane.prefViewportWidth = stage.width
        bp!!.center = myScrollPane
        stag!!.scene.apply { bp }
        stag!!.show()

    }
    private fun changed(newValue: Any) {
        if (newValue == 0) {
            myTileView.children.clear()
            myCascadeView = CascadeView(myModel)
            myScrollPane = ScrollPane()
            myScrollPane.content = myCascadeView
            myCascadeView.prefWidth = stag!!.width
            myScrollPane.prefViewportWidth = stag!!.width
            bp!!.center = myScrollPane

            myScrollPane.setOnMousePressed{
                println("we are clicking on the CascadeView background")
                myModel.resetSelected()
            }

            stag!!.scene.apply { bp }
            stag!!.show()
        }
        if (newValue == 1) {
            myCascadeView.children.clear()
            myTileView = TileView(myModel)
            myScrollPane = ScrollPane()
            myScrollPane.content = myTileView
            myTileView.prefWidth = stag!!.width
            myScrollPane.prefViewportWidth = stag!!.width

            myScrollPane.setOnMousePressed{
                println("we are clicking on the TILEVIEW background")
                myModel.resetSelected()
            }

            bp!!.center = myScrollPane
            stag!!.scene.apply { bp }
            stag!!.show()

        }
    }
}