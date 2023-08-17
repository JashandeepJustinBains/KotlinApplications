package com.example.multiphotoviewer.view

import com.example.multiphotoviewer.model.Model
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.layout.AnchorPane


class CascadeView(val model: Model) : AnchorPane(), InvalidationListener {
    init {
        model.addListener(this)
        invalidated((null))
        update()
    }

    private fun update() {
        if (model.getSize() == -1) {
            children.clear()
        } else {
            model.getImages().forEach { pic ->
                val iv = pic.getIV()
                if (pic.getMovable()) {
                    iv.setOnMousePressed {
                        println("Clicked on picture number: ${pic.getNum() + 2}")
                        model.setImg(iv)
                        iv.toFront()
                        pic.setSceneX(it.sceneX)
                        pic.setSceneY(it.sceneY)
                        pic.setTranslateX(pic.getIV().translateX)
                        pic.setTranslateY(pic.getIV().translateY)
                        it.consume()
                    }

                    pic.getIV().setOnMouseDragged {
                        if (pic.getMovable()) {
                            val sceneX = it.sceneX - pic.getSceneX()
                            val sceneY = it.sceneY - pic.getSceneY()
                            val translateX = pic.getTranslateX() + sceneX
                            val translateY = pic.getTranslateY() + sceneY
                            pic.getIV().translateX = translateX
                            pic.getIV().translateY = translateY
                        }
                            it.consume()
                    }
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

