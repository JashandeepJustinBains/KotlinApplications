package com.example.spaceinvaders.model

import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.shape.Rectangle
import java.io.File

const val playerW : Double = 13.0 / 2
const val playerH: Double = 44.0 / 2
class PlayerProjectile(private val model: Model,
                       initX: Double,
                       initY: Double) : Rectangle(initX,initY,playerW,playerH), Projectile {

    override val speed = 10.0
    override val type = File("src/main/resources/com/example/spaceinvaders/images/player_bullet.png")

    override fun update() {
        this.y -= speed
        if (this.y <= 25.0) delete()
    }
    override fun getW() : Double { return playerW }
    override fun getH() : Double { return playerH }
    override fun delete() { model.removePlayerProjectile(this) }

    override fun draw(gc: GraphicsContext) {
        gc.apply{
            save()
            clearRect(x,y,playerW,playerH)
            drawImage(Image(type.toURI().toString()), x, y, playerW, playerH)
            restore()
        }
    }

}