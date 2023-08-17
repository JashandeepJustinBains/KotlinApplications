package com.example.multiphotoviewer.model

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.beans.property.ReadOnlyIntegerWrapper
import javafx.scene.image.Image
import javafx.scene.image.ImageView

/**
 * Model represents the "Model" in "Model-View-Controller".
 */
class Model(private var whichview : ReadOnlyIntegerWrapper) : Observable {

    private val views = mutableListOf<InvalidationListener?>()
    private val myImages = mutableListOf<Picture>()
    private var myIndex : Int = -1 // if index is -1 then there is no focussed image

    fun setImg(i: ImageView) {
        myImages.forEach {
            it.setSelected(false)
            if (it.getIV() == i) {
                setIndex(myImages.indexOf(it))
                it.setSelected(true)
            }
        }
        views.forEach { it?.invalidated(this) }
    }

    fun getImages() : MutableList<Picture> { return myImages.toMutableList() }

    fun addToList(image : Image, name : String) {
        val pic = Picture(ImageView(image), getSize(), getType(), name)
        myImages.add(pic)
        //setIndex(getSize())
        views.forEach { it?.invalidated(this) }
    }

    fun getSize(): Int { return myImages.size - 1 }

    private fun setIndex(i: Int) { myIndex = i }
    fun getIndex(): Int { return myIndex }

    fun remFromList() {
        if (myIndex != -1) {
            val tempindex = myIndex
            myIndex = -1
            myImages.removeAt(tempindex)
            views.forEach { it?.invalidated(this) }
        }
    }

    fun cascadify() {
        whichview.value = 0
        setAll()
    }
    fun tileify() {
        whichview.value = 1
        resetAll()
    }
    fun getType(): Int { return whichview.value }

    fun zoomIN() {
        myImages[myIndex].setZoom(1)
        views.forEach {it?.invalidated(this)}
    }
    fun zoomOut() {
        myImages[myIndex].setZoom(-1)
        views.forEach {it?.invalidated(this)}
    }
    fun rotateR() {
        myImages[myIndex].setRot(1)
        views.forEach {it?.invalidated(this)}
    }
    fun rotateL() {
        myImages[myIndex].setRot(-1)
        views.forEach {it?.invalidated(this)}
    }

    fun reset() {
        myImages[myIndex].setZoom(0)
        myImages[myIndex].setRot(0)
        views.forEach {it?.invalidated(this)}
    }
    private fun resetAll() {
        myImages.forEach {
            it.setRot(0)
            it.setZoom(0)
            it.setMovable(false)
            it.getIV().translateX = 0.0
            it.getIV().translateY = 0.0
        }
        views.forEach {it?.invalidated(this)}
    }

    private fun setAll() {
        myImages.forEach {
            it.setMovable(true)
        }
        views.forEach { it?.invalidated(this) }
    }

    fun resetSelected() {
        myImages.forEach {
            it.setSelected(true)
        }
        setIndex(-1)
        views.forEach { it?.invalidated(this) }
    }

    override fun addListener(listener: InvalidationListener) { views.add(listener) }
    override fun removeListener(listener: InvalidationListener) { views.remove(listener) }

}