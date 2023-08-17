package com.example.spaceinvaders.model

import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import java.io.File
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer


class Player(private val model : Model,
             initX: Double,
             initY: Double) : Rectangle(), BattleShip {

    private var reload: Int = 15
    override var type = File("src/main/resources/com/example/spaceinvaders/images/player.png")
    private val w = 124.0 / 2
    private val h = 75.0 / 2
    override val speed = 8.0
    private var direction = 0
    private val shootSFX = Media(File("src/main/resources/com/example/spaceinvaders/sounds/shoot.wav").toURI().toString())
    private val deathSFX = Media(File("src/main/resources/com/example/spaceinvaders/sounds/explosion.wav").toURI().toString())
    private val mediaPlayerDeath = MediaPlayer(deathSFX)
    private val mediaPlayerShoot = MediaPlayer(shootSFX)
    init {
        mediaPlayerDeath.volume = 0.1
        mediaPlayerShoot.volume = 0.1
        x = initX
        y = initY
    }

    override fun draw(gc: GraphicsContext) {
        gc.apply{
            save()
            clearRect(x,y,w,h)
            drawImage(Image(type.toURI().toString()), x, y, w, h)
            restore()
        }
        gc.apply{
            save()
            fill = reload()
            fillRect(x,y+38, (w-1.0) * reload/15, 10.0)
            restore()
        }
    }

    override fun resetSounds() {
        mediaPlayerDeath.stop()
        mediaPlayerShoot.stop()
    }

    fun isHit(proj: EnemyProjectile) {
        if (x < proj.x + proj.getW() &&
            x + w > proj.x &&
            y < proj.y + proj.getH() &&
            y + h > proj.y) {
            model.removeEnemyProjectile(proj)
            death()
        }
    }

    fun isHit(enemyShip: Enemy) {
        if (x < enemyShip.x + enemyShip.getW() &&
            x + w > enemyShip.x &&
            y < enemyShip.y + enemyShip.getH() &&
            y + h > enemyShip.y) {
            death()
            enemyShip.death()
        }
    }

    fun setDirection(dir : Int) { direction = dir }
    override fun getDirection() : Int { return direction }

    override fun shoot() {
        if (reload == 15) {
            val projectile = PlayerProjectile(model, this.x + w / 2 - 5.5 / 2, this.y)
            model.addPlayerProjectile(projectile)
            reload = 0
            mediaPlayerShoot.play()
        }
    }
    private fun reload(): Color? {
        val color = when {
            reload == 15 -> {
                mediaPlayerShoot.stop()
                Color.GREEN
            }
            reload > 10 -> Color.YELLOW
            else -> Color.ORANGE
        }
        return color
    }

    private fun death() {
        model.kill()
        val test = model.getEnemies().random()
        if (test.getDirection() == -1) {
            x = 1300.0
        } else if (test.getDirection() == 1) {
            x = 200.0
        }
        mediaPlayerDeath.play()

    }

    override fun update() {

        if (reload <= 4) { type = File("src/main/resources/com/example/spaceinvaders/images/playershoot.png") }
        if (reload > 4) { type = File("src/main/resources/com/example/spaceinvaders/images/player.png") }
        if (reload < 15) reload += 1

        if (x + speed >= 1488.0 && direction == 1) { direction = 0 }
        else if (x - speed <= 50.0 && direction == -1) { direction = 0 }
        else if (direction != 0) x += speed * direction


    }
}