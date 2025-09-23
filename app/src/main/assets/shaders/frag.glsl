precision mediump float;

struct LightDirectional {
    lowp vec3 ambientColor;
    lowp vec3 color;
    lowp vec3 direction;
};

struct Material {
    lowp float specular;
    lowp float shine;
};

uniform sampler2D texture;
uniform LightDirectional dirLight;
uniform Material material;
uniform lowp vec3 cameraPosition;

varying lowp vec3 outFragPosition;
varying lowp vec3 outNormal;
varying lowp vec2 outTexCoord;

vec3 calculateLightDirectional(
    LightDirectional dirLight,
    Material material,
    vec3 norm,
    vec3 viewDirection
) {
    // Diffuse
    float diffFactor = max(dot(norm, dirLight.direction), 0.01);
    vec3 diffColor = dirLight.color * diffFactor;

    // Specular
    lowp vec3 reflection = reflect(
        -dirLight.direction,
        norm
    );

    lowp float specFactor = pow(
        max(0.0, dot(viewDirection, reflection)),
        16.0
    );

    return diffColor + dirLight.color * specFactor;
}

void main() {

    vec3 norm = normalize(outNormal);
    lowp vec3 viewDirection = normalize(
        cameraPosition - outFragPosition
    );

    vec3 lightDirColor = calculateLightDirectional(
        dirLight,
        material,
        norm,
        viewDirection
    );

    gl_FragColor = texture2D(
        texture,
        outTexCoord
    ) * vec4(dirLight.ambientColor + lightDirColor, 1.0);

}