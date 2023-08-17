package com.example.spaceinvaders.view

import com.example.spaceinvaders.model.Model
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.control.Label
import javafx.scene.paint.Color
import javafx.scene.text.Font
import java.io.File
import java.io.FileInputStream

class Level(private val model: Model) : Label(), InvalidationListener {

    private val arrayFontStream = FileInputStream(
        File(System.getProperty("user.dir")+
            "/src/main/resources/com/example/spaceinvaders/fonts/Array-Regular.otf"))
    private val smallArrayFont = Font.loadFont(arrayFontStream, 32.0)

    init {
        font = smallArrayFont
        textFill = Color.WHITE

        model.addListener(this)
        invalidated(null)
    }
    override fun invalidated(observable: Observable?) {
        text = "Level: ${model.getLevel()}"
    }
}
