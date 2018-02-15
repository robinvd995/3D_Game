#version 400

in vec3 textureCoords;

out vec4 out_color;

uniform samplerCube cubeMap;

void main(void){
	out_color = texture(cubeMap, textureCoords);
}
