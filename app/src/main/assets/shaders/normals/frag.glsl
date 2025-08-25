precision mediump float;

varying vec3 outNormal;

void main() {
    gl_FragColor = vec4(
        normalize(outNormal),
        1.0
    );
}