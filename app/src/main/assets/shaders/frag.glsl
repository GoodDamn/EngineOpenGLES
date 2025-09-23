precision mediump float;

struct LightPoint {
    lowp vec3 color;
    lowp vec3 position;

    lowp float constant;
    lowp float linear;
    lowp float quad;
};

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
uniform LightPoint lightPoint;
uniform lowp vec3 cameraPosition;

varying lowp vec3 outFragPosition;
varying lowp vec3 outNormal;
varying lowp vec2 outTexCoord;

vec3 calculateLightPoint(
    LightPoint light,
    Material material,
    vec3 norm,
    vec3 viewDirection,
    vec3 fragPosition,
    vec3 colorAmbient
) {
    vec3 direction = normalize(
        light.position - fragPosition
    );

    // Diffuse
    float diffFactor = max(dot(norm, direction), 0.01);
    vec3 colorDiff = light.color * diffFactor;

    // Specular
    lowp vec3 reflection = reflect(
        -direction,
        norm
    );

    lowp float specFactor = pow(
        max(0.0, dot(viewDirection, reflection)),
        16.0
    );

    lowp vec3 colorSpec = light.color * specFactor;

    float dst = length(
        light.position - fragPosition
    );

    float attenuation = 1.0 / (
        light.constant + light.linear * dst + light.quad * dst * dst
    );

    colorSpec *= attenuation;
    colorDiff *= attenuation;

    return colorDiff + colorSpec + colorAmbient * attenuation;
}

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

    return dirLight.ambientColor + diffColor + dirLight.color * specFactor;
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

    vec3 lightPointColor = calculateLightPoint(
        lightPoint,
        material,
        norm,
        viewDirection,
        outFragPosition,
        dirLight.ambientColor
    );

    gl_FragColor = texture2D(
        texture,
        outTexCoord
    ) * vec4(lightDirColor, 1.0);

}