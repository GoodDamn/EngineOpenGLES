package good.damn.engine.models

import good.damn.engine.opengl.models.MGMLightPass
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

    val wireframe: MGShaderGeometryPassModel,

    val lightPasses: Array<MGMLightPass>,
) {
    companion object {
        const val SIZE_LIGHT_POINT = 64
    }
}