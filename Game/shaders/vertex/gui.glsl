in vec2 vertexPosition;
in vec2 vertexUv;

out vec2 textureCoords;
out vec4 guiColor;

uniform vec2 elementPosition;
uniform float depth;
uniform vec4 color;

void main(void){

	vec2 screenPosition = vertexPosition + elementPosition;
	gl_Position = vec4(screenPosition.xy, depth, 1.0f);

	textureCoords = vertexUv;
	guiColor = color;
}
