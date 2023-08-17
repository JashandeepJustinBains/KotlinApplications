package com.example.spaceinvaders.view

import com.example.spaceinvaders.controller.AnimationTimer
import com.example.spaceinvaders.model.Model
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.*
import javafx.scene.text.Font
import java.io.File
import java.io.FileInputStream

class MenuView() : VBox() {

    private val arrayFontStream = FileInputStream(File(System.getProperty("user.dir")+
            "/src/main/resources/com/example/spaceinvaders/fonts/Array-Regular.otf"))
    private val smallArrayFont = Font.loadFont(arrayFontStream, 32.0)

    private val instruction = Label()
    private val start = Label()
    private val movement = Label()
    private val space = Label()
    private val quit = Label()
    private val level = Label()
    private val credit = Label()
    init {
        this.isFocusTraversable = true
        setOnKeyPressed{
            println("hello we are pressing ${it.code}")
        }
        alignment = Pos.CENTER
        create()
    }

     private fun create() {
        //setOnMousePressed{
        //    println("we are clicking on the MenuView background")
        // }

        // add logo to center top
        val file = File(System.getProperty("user.dir")+
                "/src/main/resources/com/example/spaceinvaders/images/logo.png")
        val logo = ImageView(Image(file.toURI().toString()))

        logo.translateY = -180.0
        children.add(logo)

        instruction.text = "Controls"
        instruction.translateY = -50.0
        instruction.font = smallArrayFont

        start.text = "Enter: Start Game"
        start.font = smallArrayFont

        movement.text = "A or left arrow key to move left. D or right arrow key to move right"
        movement.font = smallArrayFont

        space.text = "Space: Shoot"
        space.font = smallArrayFont

        quit.text = "Q: Quit Game"
        quit.font = smallArrayFont

        level.text = "1/2/3: Start game at respective level"
        level.font = smallArrayFont

        credit.text = "Implemented by: Jashandeep Justin Bains | j24bains | 20832856"
        credit.font = smallArrayFont
        credit.translateY = 200.0
        children.addAll(instruction, start, movement, space, quit, level, credit)

    }

}