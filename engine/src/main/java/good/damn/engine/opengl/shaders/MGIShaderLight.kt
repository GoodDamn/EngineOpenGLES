package good.damn.engine.opengl.shaders

interface MGIShaderLight {
    val lightPoint: MGShaderLightPoint
    val light: MGShaderLightDirectional
    val material: MGShaderMaterial
}