package good.damn.engine.opengl.shaders

interface MGIShaderLight {
    val lightPoints: Array<MGShaderLightPoint>
    val light: MGShaderLightDirectional
    val material: MGShaderMaterial
}