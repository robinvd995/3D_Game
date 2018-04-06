in vec3 vertexPosition;

out vec3 color;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

uniform vec3 lineColor;

void main(void){

	vec4 worldPos = modelMatrix * vec4(vertexPosition, 1.0);
	gl_Position = projectionMatrix * viewMatrix * worldPos;

	color = lineColor;
}
