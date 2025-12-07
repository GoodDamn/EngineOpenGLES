package good.damn.engine.models

import good.damn.engine.opengl.models.MGMShader
import good.damn.engine.opengl.shaders.MGShaderOpaqueSingle
import good.damn.engine.opengl.shaders.MGShaderOpaque
import good.damn.engine.opengl.shaders.MGShaderSingleMap
import good.damn.engine.opengl.shaders.MGShaderSingleMapInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleModeNormals
import good.damn.engine.opengl.shaders.base.MGShaderSky
import good.damn.engine.shader.MGShaderCache
import good.damn.engine.shader.MGShaderSource

data class MGMInformatorShader(
    val source: MGShaderSource,
    val opaqueGenerated: MGShaderCache<MGShaderOpaqueSingle>,
    val opaqueGeneratedInstanced: MGShaderCache<MGShaderOpaque>,
    val wireframe: MGMShader<MGShaderSingleMode, MGShaderSingleModeInstanced>,
    val normals: MGMShader<MGShaderSingleModeNormals, MGShaderSingleModeInstanced>,
    val texCoords: MGMShader<MGShaderSingleMode, MGShaderSingleModeInstanced>,
    val map: MGMShader<MGShaderSingleMap, MGShaderSingleMapInstanced>,
    val sky: MGShaderSky
) {
    companion object {
        const val SIZE_LIGHT_POINT = 4
    }
}