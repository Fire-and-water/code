import com.soywiz.klock.timesPerSecond
import com.soywiz.klogger.AnsiEscape
import com.soywiz.korev.Key
import com.soywiz.korge.*
import com.soywiz.korge.input.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.*
import org.jbox2d.dynamics.BodyType
import com.soywiz.korge.box2d.registerBodyWithFixture

suspend fun main() = Korge(title = "Fire and Water demo", width = 1080, height = 720, bgcolor = Colors.BLACK) {
    solidRect(1080, 50, Colors.YELLOW).xy(0, 670).registerBodyWithFixture(type = BodyType.STATIC)
    solidRect(1080, 50, Colors.YELLOW).xy(0, 0).registerBodyWithFixture(type = BodyType.STATIC)
    solidRect(540, 50, Colors.YELLOW).xy(540, 320).registerBodyWithFixture(type = BodyType.STATIC)
    solidRect(540, 50, Colors.YELLOW).xy(0, 500).registerBodyWithFixture(type = BodyType.STATIC)
    solidRect(540, 50, Colors.YELLOW).xy(0, 200).registerBodyWithFixture(type = BodyType.STATIC)
    solidRect(50, 720, Colors.YELLOW).xy(0, 0).registerBodyWithFixture(type = BodyType.STATIC)
    solidRect(50, 720, Colors.YELLOW).xy(1030, 0).registerBodyWithFixture(type = BodyType.STATIC)
//    val player = solidRect(50, 50, Colors.RED).xy(600, 100)
    val player = Player(600.0, 100.0, 50.0, 50.0)
    addChild(player.body)
//    var box = solidRect(50, 50, Colors.BLUE).position(400, 100).rotation(30.degrees)
//        .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 2, friction = 0.01)
    var tick = 0
    addFixedUpdater(60.timesPerSecond) {
        if (this.input.keys.justPressed(Key.UP) && player.isOnGround) {
            player.isJumping = true
            player.isOnGround = false
        }
        if (player.isJumping && tick < 25) {
            go(player, this, true)
            tick++
        } else {
            go(player, this, false)
            tick = 0
            player.isJumping = false
        }
    }
}

fun go(player: Player, stage: Stage, isJumping: Boolean) {
    if (isJumping) {
        jump(player, stage)
    } else {
        fall(player, stage)
    }
    if (stage.input.keys.pressing(Key.RIGHT)) runRight(player, stage)
    if (stage.input.keys.pressing(Key.LEFT)) runLeft(player, stage)
}

fun fall(player: Player, stage: Stage) {
    player.updateY(player.y + 5)
    var checker = true
    player.body.onCollision({ it != stage }) {
        player.updateY(it.y - player.height - 1)
        player.isOnGround = true
        checker = false
    }
    if (checker) player.isOnGround = false
}

fun runRight(player: Player, stage: Stage) {
    player.updateX(player.x + 5)
    player.body.onCollision({ it != stage }) {
        player.updateX(it.x - player.width - 1)
    }
    if (player.x + player.width > stage.width) player.updateX(stage.width - player.width)
}

fun runLeft(player: Player, stage: Stage) {
    player.updateX(player.x - 5)
    player.body.onCollision({ it != stage }) {
        player.updateX(it.x + it.width + 1)
    }
    if (player.x < 0) player.updateX(0.0)
}

fun jump(player: Player, stage: Stage) {
    player.updateY(player.y - 10)
    player.body.onCollision({ it != stage }) {
        player.updateY(it.y + it.height + 1)
        player.isJumping = false
    }
}