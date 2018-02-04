#version 400 core

in vec2 uvPosition;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;

void main(void){

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);

	float nDot1 = dot(unitNormal, unitLightVector);
	float brightness = max(nDot1, 0.3);
	vec3 diffuse = brightness * lightColor;

	color = vec4(diffuse, 1.0) * texture(textureSampler, uvPosition);
}
