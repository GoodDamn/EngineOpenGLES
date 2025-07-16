attribute vec4 position;
attribute vec3 normal;
attribute vec2 texCoord;

uniform mat4 projection;
uniform mat4 model;
uniform mat4 camera;

uniform float textureOffset;

varying lowp vec3 outFragPosition;
varying lowp vec3 outNormal;
varying lowp vec2 texCoordOut;

void main() {

    mat4 mv = camera * model;
    mat4 mvp = projection * mv;

    gl_Position = mvp * position;

    outFragPosition = vec3(model * position);
    outNormal = normal;

    //posOut = coord.xyz;
    texCoordOut = texCoord * vec2(textureOffset);
}