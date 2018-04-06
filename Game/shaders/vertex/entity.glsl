in vec3 vertexPosition;
in vec2 vertexUV;
in vec3 vertexNormal;

out vec2 uvPosition;
out vec3 in_block_color;
out vec3 surfaceNormal;
out vec3 toLightVector;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

uniform vec3 blockColor;
uniform vec3 lightDirection;

void main(void){

	vec4 worldPos = modelMatrix * vec4(vertexPosition, 1.0);
	gl_Position = projectionMatrix * viewMatrix * worldPos;
	uvPosition = vertexUV;

	in_block_color = blockColor;

	surfaceNormal = (modelMatrix * vec4(vertexNormal, 0.0)).xyz;
	toLightVector = lightDirection;
}
