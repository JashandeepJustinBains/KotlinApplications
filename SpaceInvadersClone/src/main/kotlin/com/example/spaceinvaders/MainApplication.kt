package com.example.spaceinvaders

import com.example.spaceinvaders.controller.AnimationTimer
import com.example.spaceinvaders.model.Model
import com.example.spaceinvaders.view.*
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.ReadOnlyIntegerWrapper
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.stage.Stage
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.io.File

class MainApplication : Application() {
    private lateinit var s: Stage


    // create roiws
    private val whichView = ReadOnlyIntegerWrapper(-1)
    private val score = ReadOnlyIntegerWrapper(0)

    // create models and timers
    private var myMenu: MenuView = MenuView()

    private var myModel: Model? = null
    private var myView: GameView? = null
    private var myTimer: AnimationTimer? = null


    private val ear = ChangeListener<Number> { _, oldValue, newValue ->
        changed(oldValue.toInt(), newValue.toInt())
    }

    // create all event filters so that they can be added/removed when switching scenes
    private val menuPressFilter = EventHandler<KeyEvent> { e ->
        println("we are in the menu press filter and are pressing ${e.code}")
        when (e.code) {
            KeyCode.ENTER, KeyCode.DIGIT1 -> {
                whichView.value = 1
            }

            KeyCode.DIGIT2 -> {
                whichView.value = 2
            }

            KeyCode.DIGIT3 -> {
                whichView.value = 3
            }

            KeyCode.Q -> {
                Platform.exit()
            }

            else -> {}
        }
    }
    private val movementPressFilter = EventHandler<KeyEvent> {
        when (it.code) {
            KeyCode.A, KeyCode.LEFT -> {
                myModel?.playerMove(-1)
            }

            KeyCode.D, KeyCode.RIGHT -> {
                myModel?.playerMove(1)
            }

            KeyCode.SPACE -> {
                myModel?.playerShoot()
            }

            KeyCode.K -> {
                myModel?.getEnemies()?.forEach { e -> myModel?.kill(e) }
            }

            KeyCode.Q -> {
                Platform.exit()
            }

            else -> {}
        }
    }
    private val movementReleaseFilter = EventHandler<KeyEvent> {
        when (it.code) {
            KeyCode.A, KeyCode.LEFT -> {
                myModel?.playerMove(0)
            }

            KeyCode.D, KeyCode.RIGHT -> {
                myModel?.playerMove(0)
            }

            else -> {}
        }
    }
    private val endingPressFilter = EventHandler<KeyEvent> {
        println("we have won the game and are pressing ${it.code}")
        when (it.code) {
            KeyCode.ENTER -> {
                whichView.value = -1
            }

            KeyCode.Q -> {
                Platform.exit()
            }

            else -> {}
        }

    }

    private val soundTrack = Media(File("src/main/resources/com/example/spaceinvaders/sounds/AjniaLaGranRutaDelSur.mp3").toURI().toString())
    private val mediaPlayer = MediaPlayer(soundTrack)
    override fun start(stage: Stage) {

        // Create a Media object for the music file

        mediaPlayer.volume = 0.15
        mediaPlayer.play()
        mediaPlayer.setOnEndOfMedia {
            // Reset the MediaPlayer and play the media again
            mediaPlayer.stop()
            mediaPlayer.play()
        }

        whichView.addListener(ear)

        stage.apply {
            title = "Space Invaders - Menu"
            width = 1600.0
            maxWidth = 1600.0
            minWidth = 1600.0
            height = 900.0
            maxHeight = 900.0
            minHeight = 900.0
            isResizable = false
            myMenu = MenuView()
            scene = Scene(myMenu)
        }.show()
        s = stage
        s.scene.addEventFilter(KeyEvent.KEY_PRESSED, menuPressFilter)
    }

    private fun changed(old: Int, new: Int) {
        when (old) {
            -1 -> s.scene.removeEventFilter(KeyEvent.KEY_PRESSED, menuPressFilter)
            0, 4 -> s.scene.removeEventFilter(KeyEvent.KEY_PRESSED, endingPressFilter)
            1,2,3 -> {
                s.scene.removeEventFilter(KeyEvent.KEY_PRESSED, movementPressFilter)
                s.scene.removeEventFilter(KeyEvent.KEY_PRESSED, movementReleaseFilter)
            }
        }
        when (new) {
            1, 2, 3 -> {
                endLevel()
                makeLevel()
            }
            4 -> {
                endLevel()
                makeGameEnd()
            }
            0 -> {
                endLevel()
                makeGameOver()
            }
            -1 -> {
                endLevel()
                makeMenu()
            }
        }
    }

    private fun endLevel() {
        myTimer?.stop()
        myTimer = null
        myModel = null
        myView = null
    }

    private fun makeMenu() {
        val myMenu = MenuView()

        s.scene = Scene(myMenu)
        s.title = "Space Invaders - Main Menu"

        s.scene.addEventFilter(KeyEvent.KEY_PRESSED, menuPressFilter)
        s.show()
    }
    private fun makeLevel() {
        myModel = Model(whichView, score)
        myView = GameView(myModel!!)
        myTimer = AnimationTimer(myModel!!, myView!!)
        myTimer!!.stop()
        s.scene = Scene(myView)
        s.title = "Space Invaders - Level ${whichView.value}"
        myTimer!!.start()

        s.scene.addEventFilter(KeyEvent.KEY_PRESSED, movementPressFilter)
        s.scene.addEventFilter(KeyEvent.KEY_RELEASED, movementReleaseFilter)
        s.show()
    }
    private fun makeGameEnd() {
        val myGameEnd = GameEnd(score)
        s.scene = Scene(myGameEnd)
        s.title = "Space Invaders - WIN"
        score.value = 0
        myModel = Model(whichView, score)
        s.scene.addEventFilter(KeyEvent.KEY_PRESSED, endingPressFilter)
        s.show()
    }
    private fun makeGameOver() {
        val myGameOver = GameOver(score)
        s.scene = Scene(myGameOver)
        s.title = "Space Invaders - LOSS"
        s.scene.addEventFilter(KeyEvent.KEY_PRESSED, endingPressFilter)
        s.show()
    }

}