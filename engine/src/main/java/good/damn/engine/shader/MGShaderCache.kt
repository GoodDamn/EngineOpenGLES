package good.damn.engine.shader

import android.util.SparseArray
import good.damn.common.COHandlerGl
import good.damn.apigl.shaders.base.MGShaderBase
import good.damn.apigl.shaders.base.MGBinderAttribute
import good.damn.apigl.shaders.creators.MGIShaderCreator
import good.damn.apigl.runnables.MGRunglCompileShaders

class MGShaderCache<
    T: good.damn.apigl.shaders.base.MGShaderBase,
    PARAM
>(
    private val map: SparseArray<T>,
    private val glHandler: COHandlerGl,
    private val shaderCreator: good.damn.apigl.shaders.creators.MGIShaderCreator<T, PARAM>
) {
    fun loadOrGetFromCache(
        srcCode: String,
        srcCodeVertex: String,
        binder: good.damn.apigl.shaders.base.MGBinderAttribute,
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
            good.damn.apigl.runnables.MGRunglCompileShaders(
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