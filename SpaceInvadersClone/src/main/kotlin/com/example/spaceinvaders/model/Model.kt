package com.example.spaceinvaders.model

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.beans.property.ReadOnlyIntegerWrapper


class Model(private val whichLevel: ReadOnlyIntegerWrapper,
            private val score: ReadOnlyIntegerWrapper) : Observable {

    private var fire: Boolean = true
    private var level = whichLevel.value
    private var initLevel = level
    private var health: Int = 3
    private val views = mutableListOf<InvalidationListener?>()
    private val player : Player = Player(this, 762.5, 800.0)
    private val playerProj = mutableListOf<PlayerProjectile>()
    private val enemies = mutableListOf<Enemy>()
    private val enemyProj = mutableListOf<EnemyProjectile>()
    private val deletedPlayerProj = mutableListOf<PlayerProjectile>()
    private val deletedEnemyProj = mutableListOf<EnemyProjectile>()
    private val deletedEnemy = mutableListOf<Enemy>()

    // ----------------------------- GAME VARIABLES --------------------------------------------------------- //
    init {

        if (level in 1..3) {
            var enemyX = 100.0
            var enemyY = 50.0
            for (i in 0..49) {

                var variation = 1
                when (i) {
                    in 0..9 -> {
                        addEnemies(enemyX, enemyY, variation)
                    }

                    in 10..29 -> {
                        variation = 2
                        addEnemies(enemyX, enemyY, variation)
                    }

                    in 30..49 -> {
                        variation = 3
                        addEnemies(enemyX, enemyY, variation)
                    }
                }
                if (i == 9 || i == 19 || i == 29 || i == 39) {
                    enemyY += 40.0
                    enemyX = 50.0
                }
                enemyX += 50.0

            }
        }
    }

    fun update() {
        // called by the AnimationTimer class to update each frame
        player.update()
        playerProj.forEach { it.update() }
        enemies.forEach { it.update() }
        enemyProj.forEach { it.update() }

    }
    fun getLevel(): Int { return level }
    fun getLives(): Int { return health }
    private fun setLives() {
        health -= 1
        if (health == 0) { gameLoss() }
        views.forEach { it?.invalidated(this) }
    }
    private fun gameLoss() { whichLevel.value = 0 }

    fun getScore(): Int { return score.value }
    fun setScore(points: Int) {
        score.value += points
        views.forEach { it?.invalidated(this) }
    }

    fun clearDebris() {
        playerProj.removeAll(deletedPlayerProj)
        enemyProj.removeAll(deletedEnemyProj)
        enemies.removeAll(deletedEnemy)

        if (enemies.size == 0 && initLevel == whichLevel.value) {
            whichLevel.value += 1
        }
    }

    // -----------------------------------  ENEMY FUNCTIONS ------------------------------------- //

    fun getEnemies(): MutableList<Enemy> { return enemies }
    private fun addEnemies(x: Double, y: Double, variation: Int) { enemies.add( Enemy(this, x, y, variation) ) }

    fun getEnemyProjectiles(): MutableList<EnemyProjectile> { return enemyProj }
    fun addEnemyProjectile( eProj: EnemyProjectile ) { enemyProj.add(eProj) }
    fun removeEnemyProjectile(eProj: EnemyProjectile) {
        if (!deletedEnemyProj.contains(eProj)) {
            deletedEnemyProj.add(eProj)
        }
    }
    fun kill(enemy: Enemy) { deletedEnemy.add(enemy) }

    // -----------------------------------  PLAYER FUNCTIONS ------------------------------------- //

    fun getPlayer(): Player { return player }
    fun playerMove(direction: Int) { player.setDirection( direction ) }
    fun playerShoot() { player.shoot() }
    fun addPlayerProjectile( pProj: PlayerProjectile ) { playerProj.add(pProj) }
    fun removePlayerProjectile( pProj: PlayerProjectile ) { deletedPlayerProj.add(pProj) }
    fun getPlayerProjectiles(): MutableList<PlayerProjectile> { return playerProj }

    override fun addListener(listener: InvalidationListener?) { views.add(listener) }
    override fun removeListener(listener: InvalidationListener?) { views.remove(listener) }
    fun kill () { setLives() }
    fun getFire(): Boolean { return fire }
    fun setFire(bool : Boolean) { fire = bool }

}