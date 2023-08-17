package com.example.spaceinvaders.view

import javafx.beans.property.ReadOnlyIntegerWrapper
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import java.io.File
import java.io.FileInputStream

class GameOver(private val score: ReadOnlyIntegerWrapper) : VBox() {
    private val arrayFontStream = FileInputStream(
        File(System.getProperty("user.dir")+
                "/src/main/resources/com/example/spaceinvaders/fonts/Array-Regular.otf")
    )
    private val arraySBFontStream = FileInputStream(
        File(System.getProperty("user.dir")+
                "/src/main/resources/com/example/spaceinvaders/fonts/Array-Semibold.otf")
    )
    private val smallArrayFont = Font.loadFont(arrayFontStream, 32.0)
    private val largeArrayFont = Font.loadFont(arraySBFontStream, 48.0)

    private val instruction = Label()
    private val lose = Label()
    private val start = Label()
    private val quit = Label()
    private val credit = Label()
    init {
        alignment = Pos.CENTER
        create()
    }

    private fun create() {
        // add logo to center top

        lose.text = "One last explosion marks our fate as your last ship is torn apart."
        lose.font = largeArrayFont
        lose.translateY = -200.0

        instruction.text = "FINAL HIGH SCORE: ${score.value}"
        instruction.translateY = -50.0
        instruction.font = smallArrayFont

        start.text = "Enter: Head Back to the Main Menu"
        start.font = smallArrayFont

        quit.text = "Q: Quit Game"
        quit.font = smallArrayFont

        credit.text = "Implemented by: Jashandeep Justin Bains | j24bains | 20832856"
        credit.font = smallArrayFont
        credit.translateY = 200.0
        children.addAll(lose, instruction, start, quit, credit)

    }
}