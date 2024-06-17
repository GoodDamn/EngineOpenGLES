attribute vec4 position;
attribute vec3 normal;
attribute vec2 texCoord;

uniform mat4 projection;
uniform mat4 model;
uniform mat4 camera;

uniform float textureOffset;

varying lowp vec3 posOut;
varying lowp vec3 normalOut;
varying lowp vec2 texCoordOut;

void main() {

    mat4 cameraModel = camera * model;

    vec4 coord = cameraModel * position;

    gl_Position = projection * coord;

    posOut = coord.xyz;
    texCoordOut = texCoord * vec2(textureOffset);
    normalOut = (cameraModel * vec4(normal, 0.0)).xyz;
}