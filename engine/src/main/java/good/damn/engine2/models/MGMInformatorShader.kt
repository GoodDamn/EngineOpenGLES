package good.damn.engine2.models

import good.damn.apigl.shaders.GLShaderGeometryPassInstanced
import good.damn.apigl.shaders.GLShaderGeometryPassModel
import good.damn.apigl.shaders.GLShaderMaterial
import good.damn.apigl.shaders.lightpass.GLShaderLightPassPointLight
import good.damn.engine2.opengl.models.MGMLightPass
import good.damn.engine2.shader.MGShaderCache
import good.damn.engine2.shader.MGShaderSource

data class MGMInformatorShader(
    val source: MGShaderSource,
    val cacheGeometryPass: MGShaderCache<
        GLShaderGeometryPassModel,
        Array<GLShaderMaterial>
        >,

    val cacheGeometryPassInstanced: MGShaderCache<
        GLShaderGeometryPassInstanced,
        Array<GLShaderMaterial>
        >,

    val wireframe: GLShaderGeometryPassModel,

    val lightPasses: Array<MGMLightPass>,
    val lightPassPointLight: GLShaderLightPassPointLight
)