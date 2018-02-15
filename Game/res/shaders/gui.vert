#version 400 core

in vec2 vertexPosition;
in vec2 vertexUv;

out vec2 textureCoords;

uniform vec2 elementPosition;

void main(void){

	vec2 screenPosition = vertexPosition + elementPosition;
	gl_Position = vec4(screenPosition.xy, 0.0f, 1.0f);

	textureCoords = vertexUv;
}
