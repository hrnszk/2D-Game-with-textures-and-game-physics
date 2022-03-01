import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL //# GL# we need this for the constants declared ˙HUN˙ a constansok miatt kell
import kotlin.js.Date
import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.Vec1
import vision.gears.webglmath.Vec2
import vision.gears.webglmath.Vec4
import vision.gears.webglmath.Vec3
import vision.gears.webglmath.Mat4
import kotlin.math.pow
import kotlin.math.exp
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.floor

class Scene (
  val gl : WebGL2RenderingContext)  : UniformProvider("scene") {

  val vsTextured = Shader(gl, GL.VERTEX_SHADER, "textured-vs.glsl")
  val vsBackground = Shader(gl, GL.VERTEX_SHADER, "background-vs.glsl")  
  val fsTextured = Shader(gl, GL.FRAGMENT_SHADER, "textured-fs.glsl")
  val texturedProgram = Program(gl, vsTextured, fsTextured)
  val backgroundProgram = Program(gl, vsBackground, fsTextured)

  //TODO: create various materials with different solidColor settings
  val fighterMaterial = Material(texturedProgram).apply{
    this["colorTexture"]?.set(Texture2D(gl, "media/fighter.png"))
  }
  val flameMaterial = Material(texturedProgram).apply{
    this["colorTexture"]?.set(Texture2D(gl, "media/flame.png"))
  }
  val backgroundMaterial = Material(backgroundProgram).apply{
    this["colorTexture"]?.set(Texture2D(gl, "media/space.png"))
  }
  val boomMaterial = Material(texturedProgram).apply{
    this["colorTexture"]?.set(Texture2D(gl, "media/boom.png"))
  }
  val asteroidMaterial = Material(texturedProgram).apply{
    this["colorTexture"]?.set(Texture2D(gl, "media/asteroid.png"))
  }

  val texturedQuadGeometry = TexturedQuadGeometry(gl)
  val backgroundMesh = Mesh(backgroundMaterial, texturedQuadGeometry)
  val fighterMesh = Mesh(fighterMaterial, texturedQuadGeometry)
  val flameMesh = Mesh(flameMaterial, texturedQuadGeometry)
  val boomMesh = Mesh(boomMaterial, texturedQuadGeometry)
  val asteroidMesh = Mesh(asteroidMaterial, texturedQuadGeometry)
  
  val camera = OrthoCamera(*Program.all).apply{
    position.set(1f, 1f)
    windowSize.set(20f, 20f)
    updateViewProjMatrix()
  }

  val gameObjects = ArrayList<GameObject>()
  val spawn = ArrayList<GameObject>()
  val killList = ArrayList<GameObject>()

  val avatar = object : GameObject(fighterMesh){ //the override below is closed under the object avatar
    //for rotation
    var angularMomentum : Float = 0.5f
    var angularMass : Float = 1.0f
    var angularVelocity : Float = 0.0f
    //for drag/stop
    var angularDrag = 0.5f
    var drag:Float = 0.5f
    //changing position
    val acceleration = Vec3()
    val velocity = Vec3()
    
    override fun move(
      dt: Float,
      t :Float,
      keysPressed : Set<String>,
      gameObjects : List<GameObject>,
      spawn : List<GameObject>,
      killList : List<GameObject>
    ): Boolean{
      if("q" in keysPressed){
        //angularMomentum += 100.0f * dt
        angularVelocity += 1.0f * dt
      }
      if("e" in keysPressed){
       // angularMomentum -= 100.0f * dt
       angularVelocity -= 1.0f * dt
      }
      angularVelocity *= exp(-angularDrag * dt)
      roll += angularVelocity * dt

      acceleration.set()
      if("w" in keysPressed){
        val modelSpaceAcceleration =  Vec3(10f,0f,0f)
        acceleration.set(
          Vec4(modelSpaceAcceleration, 0f) * modelMatrix
        ) //obtain the world coordiante
        acceleration.set(cos(roll) * 20f, sin(roll) * 20f)
      }
      if("s" in keysPressed){
        val modelSpaceAcceleration =  Vec3(10f,0f,0f)
        acceleration.set(
          Vec4(modelSpaceAcceleration, 0f) * modelMatrix
        ) //obtain the world coordiante
        acceleration.set(-cos(roll) * 20f, -sin(roll) * 20f)
      }
      
      velocity += acceleration * dt //integration of acceleration
      velocity *= exp(-drag * dt) //slows down the change in position
      position += velocity * dt //integration of velocity

      gameObjects.forEach{
        if(it != this){
          if(it == Asteroid()){
            val distance = (this.position - it.position).length()
            if(distance < 2){ //assume all  radius are  1 and mass are 1
              val normal = (this.position - it.position).normalize()
              this.position -= normal
              it.position += normal
              val relativeVelocity = (this.position - it.position)/dt
              val impMag: Float = normal.dot(relativeVelocity)/ 2f * 1.5f
              this.velocity += (-impMag * normal)
              it.velocity += (-impMag * normal)

              killList += this
              spawn += boom
              this.scale.set(Vec3(0f,0f,0f))
            }
          }
        }
      }

      this["textureOffset"]?.set(0f,0f)
      this["phases"]?.set(1f)
      return true
    }
  }

  val flame = GameObject(flameMesh)

  val flameR = GameObject(flameMesh)

  val flameL = GameObject(flameMesh)


  val boom = object : GameObject(boomMesh){

    var counter = 0
    override fun move(
      dt: Float,
      t :Float,
      keysPressed : Set<String>,
      gameObjects : List<GameObject>,
      spawn : List<GameObject>,
      killList : List<GameObject>
    ): Boolean{
      this.scale.set(Vec3(2f,2f,2f))
      val phase = floor(t * 36) % 36
      counter++
      if(counter > 36f){
        this.scale.set(Vec3(0f,0f,0f))
      }
        this["textureOffset"]?.set( phase % 6f, floor(phase / 6f))
        this["phases"]?.set(6.0f)
      
      return false
    }
  }



  init {
    gameObjects += GameObject(backgroundMesh)
    gameObjects += avatar
    avatar.roll = 1.6f
    gameObjects += flame.apply{
      parent = avatar
      roll = -1.6f
      position.set(-1.5f)
      scale.set(Vec3(0f,0f,0f))
    }
    gameObjects += flameR.apply{
      parent = avatar
      position.set(Vec3(-1.15f,-1.0f,0f))
      roll = -0.8f
      scale.set(Vec3(0f,0f,0f))
    }
    gameObjects += flameL.apply{
      parent = avatar
      position.set(Vec3(-1.3f,0.9f,0f))
      roll = -2.1f
      scale.set(Vec3(0f,0f,0f))
    }
    gameObjects += Asteroid(asteroidMesh).apply{
      initialPosition = 5f
    }
    gameObjects += Asteroid(asteroidMesh).apply{
      initialPosition = -5f
    }
  }

  fun resize(canvas : HTMLCanvasElement) {
    gl.viewport(0, 0, canvas.width, canvas.height)//#viewport# tell the rasterizer which part of the canvas to draw to ˙HUN˙ a raszterizáló ide rajzoljon
    camera.setAspectRatio(canvas.width.toFloat()/canvas.height)
  }

  val timeAtFirstFrame = Date().getTime()
  var timeAtLastFrame =  timeAtFirstFrame
  //TODO: add property reflecting uniform scene.time
  //TODO: add all programs as child components



  @Suppress("UNUSED_PARAMETER")
  fun update(keysPressed : Set<String>) {
    val timeAtThisFrame = Date().getTime() 
    val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
    val t = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f
    //TODO: set property time (reflecting uniform scene.time) 
    timeAtLastFrame = timeAtThisFrame
    
    camera.position.set(avatar.position)
    camera.updateViewProjMatrix()

    gl.clearColor(0.3f, 0.0f, 0.3f, 1.0f)//## red, green, blue, alpha in [0, 1]
    gl.clearDepth(1.0f)//## will be useful in 3D ˙HUN˙ 3D-ben lesz hasznos
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)//#or# bitwise OR of flags

    gl.enable(GL.BLEND)
    gl.blendFunc(
      GL.SRC_ALPHA,
      GL.ONE_MINUS_SRC_ALPHA)

  gameObjects.forEach{
    it.move(dt, t, keysPressed, gameObjects, spawn, killList)
  }

  gameObjects.removeAll(killList)
  gameObjects.addAll(spawn)

    //move and then move the camera - resolves the jumpiness?
    camera.position.set(avatar.position)
    camera.updateViewProjMatrix()

    
    gameObjects.forEach{
      it.update()
    }
    gameObjects.forEach{
      it.draw(this, camera)
    }

  
  }
}
