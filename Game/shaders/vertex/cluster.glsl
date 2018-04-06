in vec3 vertexPosition;
in vec2 vertexUV;
in vec3 vertexNormal;
in vec3 vertexTangent;

out vec2 uvPosition;
//out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;

out vec4 passLightInfo;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

uniform vec3 lightDirection;
uniform vec4 lightInfo;

void main(void){

	vec4 worldPos = modelMatrix * vec4(vertexPosition, 1.0);
	gl_Position = projectionMatrix * viewMatrix * worldPos;
	mat4 modelViewMatrix = viewMatrix * modelMatrix;
	uvPosition = vertexUV;

	vec3 surfaceNormal = (modelMatrix * vec4(vertexNormal, 0.0)).xyz;

	vec3 norm = normalize(surfaceNormal);
	vec3 tang = normalize((modelViewMatrix * vec4(vertexTangent, 0.0)).xyz);
	vec3 bitang = normalize(cross(norm, tang));

	mat3 toTangentSpace = mat3(
		tang.x, bitang.x, norm.x,
		tang.y, bitang.y, norm.y,
		tang.z, bitang.z, norm.z
	);

	toLightVector = toTangentSpace * (lightDirection);// - worldPos.xyz;
	toCameraVector = toTangentSpace * ((inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPos.xyz);

	passLightInfo = lightInfo;
}
