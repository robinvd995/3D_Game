#version 400 core

in vec3 vertexPosition;
in vec2 vertexUV;
in vec3 vertexNormal;

out vec2 uvPosition;
out vec3 surfaceNormal;
out vec3 toLightVector;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform vec3 lightPosition;

void main(void){

	vec4 worldPos = modelMatrix * vec4(vertexPosition, 1.0);
	gl_Position = projectionMatrix * viewMatrix * worldPos;
	uvPosition = vertexUV;

	surfaceNormal = (modelMatrix * vec4(vertexNormal, 0.0)).xyz;
	toLightVector = lightPosition - worldPos.xyz;
}
