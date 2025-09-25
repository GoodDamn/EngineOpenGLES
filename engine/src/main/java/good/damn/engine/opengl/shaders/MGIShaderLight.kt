package good.damn.engine.opengl.shaders

interface MGIShaderLight {
    val lightPoints: Array<MGShaderLightPoint>
    val lightDirectional: MGShaderLightDirectional
    val material: MGShaderMaterial
}