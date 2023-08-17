package com.example.multiphotoviewer.model

import javafx.scene.image.ImageView

class Picture(imageView: ImageView, i: Int, type: Int, title : String) {
    private var movable : Boolean
    private var selected : Boolean
    private var name : String

    private val iv : ImageView
    private var TranslateX : Double
    private var TranslateY : Double
    private var sceneX : Double
    private var sceneY : Double
    private var num : Int
    private var rot : Int
    private var zoom : Int
    init {
        num = i
        name = title
        iv = imageView
        selected = false
        iv.opacity = 0.75
        iv.isPreserveRatio = true
        iv.fitWidth = 375.0
        iv.fitHeight = 375.0
        TranslateX = 0.0
        TranslateY = 0.0
        sceneX = 0.0
        sceneY = 0.0
        rot = 0
        zoom = 0
        movable = type != 1
    }
    fun getNum() : Int { return num }
    fun getIV(): ImageView { return iv }
    fun getTranslateX(): Double { return TranslateX }
    fun getTranslateY() : Double{ return TranslateY }
    fun setTranslateX(value : Double) { TranslateX = value }
    fun setTranslateY(value : Double) { TranslateY = value }
    fun setSceneX(value : Double) { sceneX = value }
    fun setSceneY(value : Double) { sceneY = value }
    fun getSceneX(): Double { return sceneX }
    fun getSceneY(): Double { return sceneY }
    fun setZoom(value : Int) {
        if (value == 0) {
            zoom = 0
        } else {
            zoom += value
        }
        getZoom()
    }
    private fun getZoom() {
        iv.fitWidth = 375 * (1 + (zoom * 0.25))
        iv.fitHeight = 375 * (1 + (zoom * 0.25))
    }
    fun setRot(value : Int) {
        if (value == 0) {
            rot = 0
        } else {
            rot += value
        }
        getRot()
    }
    private fun getRot() { iv.rotate = rot * 10.0 }
    fun setMovable(value: Boolean) { movable = value }
    fun getMovable() : Boolean { return movable }
    fun setSelected(bool : Boolean) {
        selected = bool
        if (bool) {
            iv.opacity = 1.00
        } else {
            iv.opacity = 0.75
        }
    }

    fun getName(): String { return name }

}