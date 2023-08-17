package com.example.spaceinvaders.view

import com.example.spaceinvaders.model.Model
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import java.io.File

class GameView(private val model: Model) : AnchorPane() {
    private val bg3 = Image(File("src/main/resources/com/example/spaceinvaders/images/background-golden-particle-dust-powder2.jpg").toURI().toString() )
    private val bg2 = Image(File("src/main/resources/com/example/spaceinvaders/images/milky-way-full-stars-space2.jpg").toURI().toString() )
    private val bg1 = Image(File("src/main/resources/com/example/spaceinvaders/images/vertical-shot-sea-starry-sky-with-milky-way3.jpg").toURI().toString() )

    private val canvas: Canvas = Canvas(1600.0, 900.0)
    init {
        style = "-fx-background-color: black"

        drawBackground()

        val myLives = Lives(model)
        val myScore = Score(model)
        val myLevel = Level(model)

        setTopAnchor(myLives, 10.0)
        setRightAnchor(myLives, 150.0)
        setTopAnchor(myScore, 10.0)
        setLeftAnchor(myScore, 25.0)
        setTopAnchor(myLevel, 10.0)
        setRightAnchor(myLevel, 25.0)

        children.addAll(myLives, myScore, myLevel)

        children.add(canvas)
        children.add(model.getPlayer())
        model.getEnemies().forEach { children.add(it) }
        drawPlayer(model, canvas)
        drawEnemies(model, canvas)
        drawProjectiles(model, canvas)


    }

    private fun drawBackground() {
        when {
            model.getLevel() == 1 -> { children.add( ImageView( bg1 ) ) }
            model.getLevel() == 2 -> { children.add( ImageView( bg2 ) ) }
            model.getLevel() == 3 -> { children.add( ImageView( bg3 ) ) }
        }
    }

    private fun drawEnemies(model: Model, canvas: Canvas) {
        model.getEnemies().forEach {
            it.draw(canvas.graphicsContext2D)
        }
    }

    private fun drawPlayer(model: Model, canvas: Canvas) {
        model.getPlayer().draw(canvas.graphicsContext2D)
    }

    private fun drawProjectiles(model: Model, canvas: Canvas) {
        model.getPlayerProjectiles().forEach {
            it.draw(canvas.graphicsContext2D)
        }
        model.getEnemyProjectiles().forEach {
            it.draw(canvas.graphicsContext2D)
        }
    }

    fun update() {
        canvas.graphicsContext2D.clearRect(0.0, 0.0, canvas.width, canvas.height)
        drawPlayer(model, canvas)
        drawEnemies(model, canvas)
        drawProjectiles(model, canvas)

    }

}