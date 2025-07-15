attribute vec4 position;
attribute vec3 normal;
attribute vec2 texCoord;

uniform mat4 projection;
uniform mat4 model;
uniform mat4 camera;

uniform float textureOffset;

//varying lowp vec3 posOut;
varying lowp vec3 outFragPosition;
varying lowp vec3 outNormal;
varying lowp vec2 texCoordOut;

void main() {

    mat4 mv = camera * model;
    mat4 mvp = projection * mv;

    //outFragPosition = vec3(mv * position);
    //outNormal = vec3(mv * vec4(normal, 0.0));

    gl_Position = mvp * position;

    outFragPosition = vec3(model * position);
    outNormal = normal;

    //posOut = coord.xyz;
    texCoordOut = texCoord * vec2(textureOffset);
}