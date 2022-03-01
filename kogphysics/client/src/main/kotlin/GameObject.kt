import vision.gears.webglmath.*
import kotlin.math.exp
import kotlin.math.PI
import kotlin.math.floor


//I think it's important to remember that now we are writing when the object is a child object by default -> but lso parents are nullable

open class GameObject(
  vararg val meshes : Mesh
   ) : UniformProvider("gameObject") {

  val position = Vec3()
  var roll = 0.0f
  val scale = Vec3(1.0f, 1.0f, 1.0f)

  val modelMatrix by Mat4()

  var parent : GameObject? = null //it may or may not have a parent

  init { 
    addComponentsAndGatherUniforms(*meshes)
  }

  //child * modelMatrix -> parent coordinate
  //child * modelMatrix * parent.modelMatrix -> world coordinate
  fun update() {
    modelMatrix.set().
      scale(scale).
      rotate(roll).
      translate(position)

      //safer way to write if(parent != null)
      parent?.let{ parent ->
        modelMatrix *= parent.modelMatrix
      }
  }

  open fun move(
      dt : Float = 0.016666f,
      t : Float = 0.0f,
      keysPressed : Set<String> = emptySet<String>(),
      gameObjects : List<GameObject> = emptyList<GameObject>(),
      spawn : List<GameObject> = emptyList<GameObject>(),
      killList : List<GameObject> = emptyList<GameObject>()
      ) : Boolean {
      this["textureOffset"]?.set(0f,0f)
      this["phases"]?.set(1f)
      return false
  }

}
