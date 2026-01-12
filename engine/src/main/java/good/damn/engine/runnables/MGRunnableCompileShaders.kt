package good.damn.engine.runnables

import good.damn.common.COIRunnableBounds
import good.damn.engine.opengl.shaders.base.MGShaderBase
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute

class MGRunnableCompileShaders(
    private val srcFragment: String,
    private val shader: MGShaderBase,
    private val srcVertex: String,
    private val bindAttr: MGBinderAttribute
): COIRunnableBounds {

    override fun run(
        width: Int,
        height: Int
    ) {
        shader.setupFromSource(
            srcVertex,
            srcFragment,
            bindAttr
        )
    }

}