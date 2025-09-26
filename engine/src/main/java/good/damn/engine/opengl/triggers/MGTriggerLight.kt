package good.damn.engine.opengl.triggers

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.entities.MGLight
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.shaders.MGIShaderModel

class MGTriggerLight(
    private val light: MGLight,
    private val lightManager: MGManagerLight,
    method: MGITriggerMethod,
    shader: MGIShaderModel,
    vertexArray: MGArrayVertex,
    model: MGMMatrix
): MGTriggerBaseDebug(
    method,
    vertexArray,
    shader,
    model
) {
    override fun onTriggerBegin() {
        lightManager.register(
            light
        )
    }

    override fun onTriggerEnd() {
        lightManager.unregister(
            light
        )
    }
}