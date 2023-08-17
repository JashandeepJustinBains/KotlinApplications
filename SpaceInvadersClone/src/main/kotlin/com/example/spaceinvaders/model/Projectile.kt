package com.example.spaceinvaders.model

import javafx.scene.canvas.GraphicsContext
import java.io.File

interface Projectile {
    val speed: Double
    val type : File
    // jellyfish bullet: W_PLAYER=26 H_PLAYER=50
    // skull bullet:     W_PLAYER=28 H_PLAYER=55
    // bug bullet:       W_PLAYER=29 H_PLAYER=56
    // player bullet:    W_PLAYER=13 H_PLAYER=44
    fun update()
    fun delete()
    fun getH(): Double
    fun getW(): Double
    fun draw(gc: GraphicsContext)
}