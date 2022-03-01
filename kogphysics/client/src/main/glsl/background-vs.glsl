#version 300 es
in vec4 vertexPosition;
in vec4 vertexTexCoord;

uniform struct {
	mat4 viewProjMatrixInverse;
	} camera;
out vec4 texCoord;

void main(void) {
  gl_Position = vertexPosition;
  texCoord = (vertexPosition * camera.viewProjMatrixInverse); //we can transform the endpoints of the screen to the world corridninates just like we did for the mouse clicks
  texCoord.xy *= 0.02;
}
