#version 400 core

in vec2 vertexPosition;

out vec3 color;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform vec3 lineColor;

void main(void){
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(vertexPosition.x, 0.0, vertexPosition.y, 1.0);
	color = lineColor;
}
