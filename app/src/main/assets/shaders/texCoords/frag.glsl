precision mediump float;

varying vec2 outTexCoord;

void main() {
    gl_FragColor = vec4(
        outTexCoord,
        1.0,
        1.0
    );
}