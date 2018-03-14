#version 400

in vec2 pass_textureCoords;
in vec3 passColor;

out vec4 out_colour;

uniform sampler2D sunTexture;

void main(void){

    out_colour = texture(sunTexture, pass_textureCoords) * vec4(passColor, 1.0);

}
