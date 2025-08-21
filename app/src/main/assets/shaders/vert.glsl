attribute vec4 position;
attribute vec3 normal;
attribute vec2 texCoord;

uniform mat4 projection;
uniform mat4 model;
uniform mat4 view;

uniform float textureOffset;

varying lowp vec3 outFragPosition;
varying lowp vec3 outNormal;
varying lowp vec2 outTexCoord;

void main() {

    vec3 spaceWorld = vec3(model * position);
    mat4 spaceView = view * model;
    mat4 spaceClip = projection * spaceView;

    outFragPosition = spaceWorld;
    outNormal = normal;
    outTexCoord = texCoord * vec2(textureOffset);

    gl_Position = spaceClip * position;
}