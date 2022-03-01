import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.WebGLTexture
import org.w3c.dom.Image
import org.w3c.dom.events.Event
import vision.gears.webglmath.Texture

class Texture2D(
  gl : WebGL2RenderingContext,
  val mediaFileUrl : String
  ) : Texture {
  override val glTexture : WebGLTexture? = gl.createTexture()
  init {
    val image = Image() //image  created
    image.onload = {
      gl.bindTexture(GL.TEXTURE_2D, glTexture) //selected as  current
      gl.texImage2D(GL.TEXTURE_2D, 0, GL.RGBA, GL.RGBA, GL.UNSIGNED_BYTE, image) //uploaded
      gl.texParameteri(GL.TEXTURE_2D, GL.TEXTURE_MAG_FILTER, GL.LINEAR) //when we're close
      gl.texParameteri(GL.TEXTURE_2D, GL.TEXTURE_MIN_FILTER, GL.LINEAR_MIPMAP_LINEAR) //when we're far use mipmap and  linear interpolation between the different resolution images
      gl.generateMipmap(GL.TEXTURE_2D); 
      gl.bindTexture(GL.TEXTURE_2D, null); 
    }
    image.src = mediaFileUrl //trigger loading
  }
}
