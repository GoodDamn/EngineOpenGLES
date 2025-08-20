attribute vec4 position;

uniform mat4 projection;
uniform mat4 model;
uniform mat4 view;

void main() {

    vec3 spaceWorld = vec3(model * position);
    mat4 spaceView = view * model;
    mat4 spaceClip = projection * spaceView;

    gl_Position = spaceClip * position;
}