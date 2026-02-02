package good.damn.apigl.runnables

import good.damn.apigl.shaders.base.GLBinderAttribute
import good.damn.apigl.shaders.base.GLShaderBase
import good.damn.common.COIRunnableBounds

class GLRunglCompileShaders(
    private val srcFragment: String,
    private val shader: GLShaderBase,
    private val srcVertex: String,
    private val bindAttr: GLBinderAttribute
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