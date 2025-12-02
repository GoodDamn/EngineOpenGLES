package good.damn.engine.models

import good.damn.engine.opengl.models.MGMShader
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderOpaque
import good.damn.engine.opengl.shaders.MGShaderSingleMap
import good.damn.engine.opengl.shaders.MGShaderSingleMapInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleModeNormals
import java.util.concurrent.ConcurrentHashMap

data class MGMInformatorShader(
    val opaqueGenerated: ConcurrentHashMap<Int, MGShaderDefault>,
    val opaqueGeneratedInstanced: ConcurrentHashMap<Int, MGShaderOpaque>,
    val wireframe: MGMShader<MGShaderSingleMode, MGShaderSingleModeInstanced>,
    val normals: MGMShader<MGShaderSingleModeNormals, MGShaderSingleModeInstanced>,
    val texCoords: MGMShader<MGShaderSingleMode, MGShaderSingleModeInstanced>,
    val map: MGMShader<MGShaderSingleMap, MGShaderSingleMapInstanced>
) {
    companion object {
        const val SIZE_LIGHT_POINT = 4
    }
}