package com.example.spaceinvaders.model

import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.shape.Rectangle
import java.io.File

const val enemyW = 13.0 * 2 / 3
const val enemyH = 44.0 * 2 / 3
class EnemyProjectile(private val model: Model,
                      initX: Double,
                      initY: Double,
                      variation: Int) : Rectangle(initX,initY,enemyW,enemyH), Projectile {

    override val speed = 8.0 * ( 1 + 0.5 * model.getLevel() )

    override val type : File

    init{
        type = File("src/main/resources/com/example/spaceinvaders/images/bullet${variation}.png")
        x = initX
        y = initY
    }

    override fun update() {
        this.y += speed
        if (this.y > 850) delete()
    }

    override fun getW(): Double { return enemyW }
    override fun getH(): Double { return enemyH }

    override fun delete() { model.removeEnemyProjectile(this) }

    override fun draw(gc: GraphicsContext) {
        gc.apply{
            save()
            drawImage(Image(type.toURI().toString()), x, y, enemyW, enemyH)
            restore()
        }
    }
}