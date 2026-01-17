package good.damn.engine.models

import good.damn.engine.opengl.models.MGMLightPass
import good.damn.apigl.shaders.MGShaderGeometryPassInstanced
import good.damn.apigl.shaders.MGShaderGeometryPassModel
import good.damn.apigl.shaders.MGShaderMaterial
import good.damn.apigl.shaders.lightpass.MGShaderLightPassPointLight
import good.damn.engine.shader.MGShaderCache
import good.damn.engine.shader.MGShaderSource

data class MGMInformatorShader(
    val source: MGShaderSource,
    val cacheGeometryPass: MGShaderCache<
        good.damn.apigl.shaders.MGShaderGeometryPassModel,
        Array<good.damn.apigl.shaders.MGShaderMaterial>
    >,

    val cacheGeometryPassInstanced: MGShaderCache<
        good.damn.apigl.shaders.MGShaderGeometryPassInstanced,
        Array<good.damn.apigl.shaders.MGShaderMaterial>
    >,

    val wireframe: good.damn.apigl.shaders.MGShaderGeometryPassModel,

    val lightPasses: Array<MGMLightPass>,
    val lightPassPointLight: good.damn.apigl.shaders.lightpass.MGShaderLightPassPointLight
)