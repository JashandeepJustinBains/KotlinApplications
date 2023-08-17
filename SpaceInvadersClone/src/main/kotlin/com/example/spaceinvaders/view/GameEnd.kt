package com.example.spaceinvaders.view

import javafx.beans.property.ReadOnlyIntegerWrapper
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import java.io.File
import java.io.FileInputStream

class GameEnd(private val score: ReadOnlyIntegerWrapper) : VBox() {

    private val arrayFontStream = FileInputStream(
        File(System.getProperty("user.dir")+
            "/src/main/resources/com/example/spaceinvaders/fonts/Array-Regular.otf")
    )
    private val smallArrayFont = Font.loadFont(arrayFontStream, 32.0)

    private val instruction = Label()
    private val start = Label()
    private val quit = Label()
    private val credit = Label()
    init {
        alignment = Pos.CENTER
        create()
    }

    private fun create() {
        // add logo to center top
        val file = File(System.getProperty("user.dir")+
                "/src/main/resources/com/example/spaceinvaders/images/logo.png")
        val logo = ImageView(Image(file.toURI().toString()))

        logo.translateY = -180.0
        children.add(logo)

        instruction.text = "FINAL HIGH SCORE: ${score.value}"
        instruction.translateY = -50.0
        instruction.font = smallArrayFont

        start.text = "Enter: Head Back to Main Menu"
        start.font = smallArrayFont

        quit.text = "Q: Quit Game"
        quit.font = smallArrayFont

        credit.text = "Implemented by: Jashandeep Justin Bains | j24bains | 20832856"
        credit.font = smallArrayFont
        credit.translateY = 200.0
        children.addAll(instruction, start, quit, credit)

    }

}