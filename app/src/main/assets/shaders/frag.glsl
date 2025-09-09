precision mediump float;

struct Light {
    lowp vec3 color;
    lowp float factorAmbient;
    lowp float intensity;
    lowp vec3 direction;
};

uniform sampler2D texture;
uniform Light light;

uniform lowp float shine;
uniform lowp float specularIntensity;

varying lowp vec3 outFragPosition;
varying lowp vec3 outNormal;
varying lowp vec2 outTexCoord;

void main() {

    lowp vec3 ambColor = vec3(0.75) * light.factorAmbient;

    // Diffuse
    vec3 norm = normalize(outNormal);
    float diffFactor = max(dot(norm, light.direction), 0.01);
    vec3 diffColor = light.color * diffFactor;

    // Specular
    //lowp vec3 eye = normalize(posOut);
    //lowp vec3 reflection = reflect(light.direction, normal);
    //lowp float specFactor = pow(max(0.0, -dot(reflection, eye)), shine);
    /*lowp vec3 specColor = light.color * specularIntensity * specFactor;*/

    gl_FragColor = texture2D(
        texture,
        outTexCoord
    ) * vec4(ambColor + diffColor, 1.0);

}