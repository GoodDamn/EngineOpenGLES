package good.damn.engine2.shader

import android.util.SparseArray
import good.damn.apigl.runnables.GLRunglCompileShaders
import good.damn.apigl.shaders.base.GLBinderAttribute
import good.damn.apigl.shaders.base.GLShaderBase
import good.damn.apigl.shaders.creators.GLIShaderCreator
import good.damn.common.COHandlerGl

class MGShaderCache<
    T: GLShaderBase,
    PARAM
>(
    private val map: SparseArray<T>,
    private val glHandler: COHandlerGl,
    private val shaderCreator: GLIShaderCreator<T, PARAM>
) {
    fun loadOrGetFromCache(
        srcCode: String,
        srcCodeVertex: String,
        binder: GLBinderAttribute,
        param: PARAM
    ): T {
        get(srcCode)?.run {
            return this
        }

        val shader = shaderCreator.create(
            param
        )

        map[srcCode.hashCode()] = shader
        glHandler.post(
            GLRunglCompileShaders(
                srcCode,
                shader,
                srcCodeVertex,
                binder
            )
        )

        return shader
    }

    private operator fun get(
        srcCode: String
    ) = map[srcCode.hashCode()]
}