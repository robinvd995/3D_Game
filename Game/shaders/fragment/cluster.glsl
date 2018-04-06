in vec2 uvPosition;
//in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

in vec4 passLightInfo;

out vec4 color;

uniform sampler2D diffuseSampler;
uniform sampler2D specularSampler;
uniform sampler2D normalSampler;

void main(void){

	vec4 normalMapValue = 2.0 * texture(normalSampler, uvPosition) - 1.0;

	vec3 unitNormal = normalize(normalMapValue.rgb);
	vec3 unitLightVector = normalize(toLightVector);

	float nDot1 = dot(unitNormal, unitLightVector);
	float brightness = max(nDot1, passLightInfo.w);
	vec3 diffuse = brightness * passLightInfo.xyz;

	vec3 unitCameraVector = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);

	color = vec4(diffuse, 1.0) * texture(diffuseSampler, uvPosition);

	vec4 specularInfo = texture(specularSampler, uvPosition);
	if(specularInfo.x > 0.0){
		float specularFactor = dot(reflectedLightDirection, unitCameraVector);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, 10.0);
		//multiply damped factor with light color to have effect and stuff
		vec3 finalSpecular = dampedFactor * specularInfo.x * passLightInfo.xyz;
		color += vec4(finalSpecular, 1.0);
	}
}
