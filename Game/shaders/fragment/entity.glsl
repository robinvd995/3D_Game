in vec2 uvPosition;
in vec3 surfaceNormal;
in vec3 toLightVector;

in vec3 in_block_color;

out vec4 color;

uniform sampler2D textureSampler;

void main(void){

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);

	float nDot1 = dot(unitNormal, unitLightVector);
	float brightness = max(nDot1, 0.3);
	vec3 diffuse = brightness * in_block_color;

	color = texture(textureSampler, uvPosition) * vec4(diffuse, 1.0);
}
