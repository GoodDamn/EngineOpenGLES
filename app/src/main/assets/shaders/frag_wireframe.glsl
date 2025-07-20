precision mediump float;

struct Light {
    lowp vec3 color;
    lowp float factorAmbient;
    lowp float intensity;
    lowp vec3 position;
};

uniform Light light;

varying lowp vec3 outFragPosition;
varying lowp vec3 outNormal;
varying lowp vec2 outTexCoord;

void main() {
    gl_FragColor = vec4(0.9);
}