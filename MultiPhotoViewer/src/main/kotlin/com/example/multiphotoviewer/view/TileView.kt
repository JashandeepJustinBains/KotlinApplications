package com.example.multiphotoviewer.view

import com.example.multiphotoviewer.model.Model
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.layout.FlowPane

class TileView(val model: Model) : FlowPane(), InvalidationListener {
    init {
        model.addListener(this)
        invalidated((null))
        orientation = Orientation.HORIZONTAL
        alignment = Pos.TOP_LEFT
        update()
    }

    private fun update() {
        if (model.getSize() == -1) {
            children.clear()
        } else {
            model.getImages().forEach { pic ->
                val iv = pic.getIV()

                iv.setOnMousePressed {
                    println("Clicked on picture number: ${pic.getNum() + 2}")
                    model.setImg(iv)
                    it.consume()
                }

                children.clear()
                model.getImages().forEach {
                    children.add(it.getIV())
                }
            }
        }
    }

    override fun invalidated(observable: Observable?) {
        update()
    }

}