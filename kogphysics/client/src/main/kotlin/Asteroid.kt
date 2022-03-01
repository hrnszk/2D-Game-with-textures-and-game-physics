import vision.gears.webglmath.*
import kotlin.math.exp
import kotlin.math.PI
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.atan2


//I think it's important to remember that now we are writing when the object is a child object by default -> but lso parents are nullable

open class Asteroid(
  vararg meshes : Mesh
   ) : GameObject(*meshes) {

    var initialPosition = 0f

    val velocity = Vec3(1f,1f,0f)

  override fun move(
      dt : Float,
      t : Float,
      keysPressed : Set<String>,
      gameObjects : List<GameObject>,
      spawn : List<GameObject>,
      killList : List<GameObject> 
      ) : Boolean {
        val currentPosition = this.position
        val newX = (16*sin(t)*sin(t)*sin(t))/4
        val newY = (13*cos(t)-5*cos(2*t)-2*cos(3*t)-cos(4*t))/4
        this.position.set(Vec3(initialPosition+newX, initialPosition+newY, 0f))
        val angle = atan2((newY-currentPosition.y)/dt, (newX-currentPosition.x)/dt)
        this.roll = angle
        this["textureOffset"]?.set(0f,0f)
        this["phases"]?.set(1f)
        return false
  }

}
