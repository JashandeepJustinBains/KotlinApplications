package com.example.spaceinvaders.model

import javafx.scene.canvas.GraphicsContext
import java.io.File
interface BattleShip {
    val type : File // what type of ship
    val speed: Double
    // var W_PLAYER: Double
    // var H_PLAYER: Double
    //var direction : Int

    //fun isHit(projectile: Projectile)
    fun getDirection(): Int
    fun shoot()
    fun update()
    fun draw(gc: GraphicsContext)
    fun resetSounds()


}