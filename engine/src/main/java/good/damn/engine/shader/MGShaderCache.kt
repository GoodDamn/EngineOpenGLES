package good.damn.engine.shader

import android.util.SparseArray
import good.damn.engine.opengl.shaders.base.MGShaderBase
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.runnables.MGRunnableCompileShaders

class MGShaderCache<T: MGShaderBase>(
    private val map: SparseArray<T>
) {
    operator fun get(
        srcCode: String
    ) = map[srcCode.hashCode()]

    fun cacheAndCompile(
        srcCode: String,
        srcCodeVertex: String,
        shader: T,
        glHandler: MGHandlerGl,
        binder: MGBinderAttribute
    ) {
        map[srcCode.hashCode()] = shader
        glHandler.post(
            MGRunnableCompileShaders(
                srcCode,
                shader,
                srcCodeVertex,
                binder
            )
        )
    }

}