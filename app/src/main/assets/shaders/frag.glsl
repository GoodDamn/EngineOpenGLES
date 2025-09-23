precision mediump float;

struct LightDirectional {
    lowp vec3 color;
    lowp float factorAmbient;
    lowp float intensity;
    lowp vec3 direction;
};

uniform sampler2D texture;
uniform LightDirectional dirLight;
uniform lowp vec3 cameraPosition;

uniform lowp float shine;
uniform lowp float specularIntensity;

varying lowp vec3 outFragPosition;
varying lowp vec3 outNormal;
varying lowp vec2 outTexCoord;

void main() {

    lowp vec3 ambColor = vec3(0.75) * dirLight.factorAmbient;

    // Diffuse
    vec3 norm = normalize(outNormal);
    float diffFactor = max(dot(norm, dirLight.direction), 0.01);
    vec3 diffColor = dirLight.color * diffFactor;

    // Specular
    lowp vec3 eye = normalize(
        cameraPosition - outFragPosition
    );

    lowp vec3 reflection = reflect(
        -dirLight.direction,
        norm
    );

    lowp float specFactor = pow(
        max(0.0, dot(eye, reflection)),
        32.0
    );
    lowp vec3 specColor = dirLight.color * specFactor;

    gl_FragColor = texture2D(
        texture,
        outTexCoord
    ) * vec4(ambColor + diffColor + specColor, 1.0);

}