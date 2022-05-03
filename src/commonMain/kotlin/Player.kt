import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.view.SolidRect
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.Colors
import org.jbox2d.dynamics.BodyType

class Player(
    var x: Double,
    var y: Double,
    var width: Double,
    var height: Double,
    var isOnGround: Boolean,
    var isJumping: Boolean
) {
    constructor(x: Double, y: Double, w: Double, h: Double) : this(x, y, w, h, false, false)
    val body = SolidRect(width, height, Colors.RED).xy(x, y).registerBodyWithFixture(type = BodyType.DYNAMIC)

    fun updateX(newX: Double) {
        x = newX
        body.x = x
    }

    fun updateY(newY : Double) {
        y = newY
        body.y = y
    }

}
