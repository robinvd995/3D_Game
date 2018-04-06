in vec3 vertexPosition;

out vec3 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void){
	gl_Position = projectionMatrix * viewMatrix * vec4(vertexPosition, 1.0);
	textureCoords = vertexPosition;
}
