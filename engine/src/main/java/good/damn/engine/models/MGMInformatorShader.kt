package good.damn.engine.models

import good.damn.engine.opengl.shaders.MGShaderLightPass
import good.damn.engine.opengl.shaders.MGShaderGeometryPassInstanced
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel
import good.damn.engine.opengl.shaders.base.MGShaderSky
import good.damn.engine.shader.MGShaderCache
import good.damn.engine.shader.MGShaderSource

data class MGMInformatorShader(
    val source: MGShaderSource,
    val cacheGeometryPass: MGShaderCache<MGShaderGeometryPassModel>,
    val opaqueGeneratedInstanced: MGShaderCache<MGShaderGeometryPassInstanced>,

    val lightPassDiffuse: MGShaderLightPass,
    val lightPassOpaque: MGShaderLightPass,

    val sky: MGShaderSky,
) {
    companion object {
        const val SIZE_LIGHT_POINT = 16
    }
}