in vec2 textureCoords;
in vec4 guiColor;

out vec4 out_color;

uniform sampler2D textureSampler;

void main(void){

	out_color = texture(textureSampler, textureCoords) * guiColor;
}
