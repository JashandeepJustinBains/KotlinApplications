package com.example.spaceinvaders.controller

import com.example.spaceinvaders.model.Model
import com.example.spaceinvaders.view.GameView
import javafx.animation.AnimationTimer
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.io.File

class AnimationTimer(private val model: Model, private val view: GameView) :AnimationTimer() {

    private val fps = 1_000_000_000.0 / 30
    private var frame = 0

    private val speed1 = Media(File("src/main/resources/com/example/spaceinvaders/sounds/fastinvader1.wav").toURI().toString())
    private val speed2 = Media(File("src/main/resources/com/example/spaceinvaders/sounds/fastinvader2.wav").toURI().toString())
    private val speed3 = Media(File("src/main/resources/com/example/spaceinvaders/sounds/fastinvader3.wav").toURI().toString())
    private val speed4 = Media(File("src/main/resources/com/example/spaceinvaders/sounds/fastinvader4.wav").toURI().toString())
    private val mediaSlow = MediaPlayer(speed1)
    private val mediaMedium = MediaPlayer(speed2)
    private val mediaFast = MediaPlayer(speed3)
    private val mediaExtreme = MediaPlayer(speed4)

    private var previousFrame = 0L
    override fun handle(now: Long) {
        val dur = now - previousFrame
        mediaSlow.volume = 0.1
        mediaMedium.volume = 0.1
        mediaFast.volume = 0.1
        mediaExtreme.volume = 0.1

        if (dur >= fps) {
            frame += 1

            model.update()
            view.update()

            val testEnemy : Double = model.getEnemies().random().speed

            if (frame % 4 == 0) {
                when (testEnemy) {
                    in 3.0..4.0 -> { mediaMedium.play() }
                    in 4.0..5.0 -> { mediaFast.play() }
                    in 5.0 .. 6.0 -> { mediaFast.play() }
                    else -> { mediaExtreme.play() }
                }
            }
            if (frame % 4 == 3) {
                mediaSlow.stop()
                mediaMedium.stop()
                mediaFast.stop()
                mediaExtreme.stop()
            }

            model.getPlayerProjectiles().forEach { pp ->
                model.getEnemies().forEach() { e ->
                    if (e.isHit(pp)) {
                        e.death(pp)
                    }
                }
            }

            model.getEnemyProjectiles().forEach { ep -> model.getPlayer().isHit(ep) }
            model.getEnemies().forEach { es -> model.getPlayer().isHit(es) }
            model.clearDebris()

            // reset firing pin
            model.setFire(true)
            previousFrame = now
        }
    }

}