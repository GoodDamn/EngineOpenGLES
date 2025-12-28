package good.damn.engine.models

import good.damn.engine.opengl.shaders.MGShaderLightPass
import good.damn.engine.opengl.shaders.MGShaderGeometryPassInstanced
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.shader.MGShaderCache
import good.damn.engine.shader.MGShaderSource

data class MGMInformatorShader(
    val source: MGShaderSource,
    val cacheGeometryPass: MGShaderCache<
        MGShaderGeometryPassModel,
        Array<MGShaderMaterial>
    >,

    val cacheGeometryPassInstanced: MGShaderCache<
        MGShaderGeometryPassInstanced,
        Array<MGShaderMaterial>
    >,

    val lightPassDiffuse: MGShaderLightPass,
    val lightPassOpaque: MGShaderLightPass,
) {
    companion object {
        const val SIZE_LIGHT_POINT = 64
    }
}