package com.example.spaceinvaders.model

import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.shape.Rectangle
import java.io.File
import java.util.Random

class Enemy(
    private val model: Model,
    initX: Double,
    initY: Double,
    private var variation: Int,
) : Rectangle(), BattleShip {

    override var type: File = File("src/main/resources/com/example/spaceinvaders/images/enemy${variation}.png")

    override var speed = 5.0 * (0.75 + 0.25 * model.getLevel())
    private val h = 70.0 / 2
    private val w = 90.0 / 2
    private var direction = 1
    private var verticalSpeed = 10.0 * (0.75 + 0.25 * model.getLevel())
    private var initialX: Double
    private val initialY: Double
    private var initialEnemies = 50
    private val shootSFX =
        Media(File("src/main/resources/com/example/spaceinvaders/sounds/shoot.wav").toURI().toString())
    private val deathSFX =
        Media(File("src/main/resources/com/example/spaceinvaders/sounds/explosion.wav").toURI().toString())
    private val mediaEnemyDeath = MediaPlayer(deathSFX)
    private val mediaEnemyShoot = MediaPlayer(shootSFX)

    init {
        initialX = initX
        initialY = initY
        x = initX
        y = initY
        mediaEnemyShoot.volume = 0.1
        mediaEnemyDeath.volume = 0.1
    }

    override fun draw(gc: GraphicsContext) {
        gc.apply {
            save()
            clearRect(x, y, w, h)
            drawImage(Image(type.toURI().toString()), x, y, w, h)
            restore()
        }
    }

    fun isHit(proj: PlayerProjectile): Boolean {
        return x < proj.x + proj.getW() &&
                x + w > proj.x &&
                y < proj.y + proj.getH() &&
                y + h > proj.y
    }

    fun death(proj: PlayerProjectile) {
        model.removePlayerProjectile(proj)
        model.setScore((4 - variation) * 5)
        mediaEnemyDeath.stop()
        mediaEnemyDeath.play()
        model.kill(this)
    }
    fun death() { // if enemy ship collides with player ship
        model.setScore((4 - variation) * 5)
        mediaEnemyDeath.stop()
        mediaEnemyDeath.play()
        model.kill(this)
    }

    override fun getDirection(): Int { return direction }

    fun getH() : Double { return h }
    fun getW() : Double { return w }

    override fun shoot() {
        val projectile = EnemyProjectile(model, this.x + w / 2 - 5.5 / 2, this.y, variation)
        model.addEnemyProjectile(projectile)
        mediaEnemyShoot.stop()
        mediaEnemyShoot.play()
    }

    private fun hitEdgeL(): Boolean { return (initialX - 925.0 >= x && direction == -1) }
    private fun hitEdgeR(): Boolean { return (initialX + 925.0 <= x && direction == 1) }

    override fun update() {
        // draw
        x += speed * direction
        val random = Random()

        if (random.nextDouble() < 0.00015 + 0.00005 * model.getLevel() ) {
            model.setFire(false)
            shoot()
        }

        if (model.getEnemies().size < initialEnemies) {
            initialEnemies = model.getEnemies().size
            speed *= 1.025
        }

        if (hitEdgeR()) {
            y += verticalSpeed
            speed *= 1.025
            direction = -1
            initialX = x

            if (model.getFire()) {
                model.setFire(false)
                model.getEnemies().random().shoot()
            }

        }
        if (hitEdgeL()) {
            y += verticalSpeed
            speed *= 1.025
            direction = 1
            initialX = x

            if (model.getFire()) {
                model.setFire(false)
                model.getEnemies().random().shoot()
            }
        }
    }

    override fun resetSounds() {
        mediaEnemyDeath.stop()
        mediaEnemyShoot.stop()
    }

}
