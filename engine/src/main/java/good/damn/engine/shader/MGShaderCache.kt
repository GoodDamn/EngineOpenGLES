package good.damn.engine.shader

import android.util.SparseArray
import good.damn.common.COHandlerGl
import good.damn.engine.opengl.shaders.base.MGShaderBase
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.opengl.shaders.creators.MGIShaderCreator
import good.damn.engine.runnables.MGRunnableCompileShaders

class MGShaderCache<
    T: MGShaderBase,
    PARAM
>(
    private val map: SparseArray<T>,
    private val glHandler: COHandlerGl,
    private val shaderCreator: MGIShaderCreator<T,PARAM>
) {
    fun loadOrGetFromCache(
        srcCode: String,
        srcCodeVertex: String,
        binder: MGBinderAttribute,
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
            MGRunnableCompileShaders(
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