precision mediump float;

uniform sampler2D texture;

varying lowp vec2 outTexCoord;

void main() {
    gl_FragColor = texture2D(
        texture,
        outTexCoord
    );
}