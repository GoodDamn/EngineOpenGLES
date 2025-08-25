attribute vec4 position;
attribute vec3 normal;

uniform mat4 projection;
uniform mat4 model;
uniform mat4 view;
uniform mat4 normalMatrix;

varying vec3 outNormal;

void main() {

    vec3 spaceWorld = vec3(model * position);
    mat4 spaceView = view * model;
    mat4 spaceClip = projection * spaceView;

    outNormal = mat3(normalMatrix) * normal;

    gl_Position = spaceClip * position;
}