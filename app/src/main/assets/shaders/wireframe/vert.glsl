attribute vec4 position;
attribute vec3 normal;
attribute vec2 texCoord;

uniform mat4 projection;
uniform mat4 model;
uniform mat4 view;

varying vec3 outNormal;
varying vec2 outTexCoords;

void main() {

    vec3 spaceWorld = vec3(model * position);
    mat4 spaceView = view * model;
    mat4 spaceClip = projection * spaceView;

    outNormal = normal;
    outTexCoords = texCoord;

    gl_Position = spaceClip * position;
}