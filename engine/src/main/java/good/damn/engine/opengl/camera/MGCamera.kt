package good.damn.engine.opengl.camera

import android.opengl.GLES30.*
import android.opengl.Matrix
import good.damn.engine.opengl.buffers.MGBufferUniformCamera
import good.damn.engine.opengl.matrices.MGMatrixTranslate
import good.damn.engine.opengl.runnables.misc.MGRunglSendDataProjection
import good.damn.engine.opengl.shaders.MGIShaderCameraPosition
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.utils.MGUtilsBuffer

open class MGCamera(
    protected val bufferUniform: MGBufferUniformCamera,
    var modelMatrix: MGMatrixTranslate
) {

    val projection = FloatArray(
        16
    )

    private val mProjectionBuffer = MGUtilsBuffer.allocateByte(
        16 * 4
    )

    fun setPerspective(
        width: Int,
        height: Int,
        handler: MGHandlerGl
    ) {
        Matrix.perspectiveM(
            projection,
            0,
            85.0f,
            width.toFloat() / height.toFloat(),
            0.9999999f,
            Float.MAX_VALUE / 2
        )

        mProjectionBuffer.asFloatBuffer().run {
            put(projection)
            position(0)
        }

        handler.post(
            MGRunglSendDataProjection(
                mProjectionBuffer,
                bufferUniform
            )
        )
    }

    fun drawPosition(
        shader: MGIShaderCameraPosition
    ) {
        synchronized(
            modelMatrix
        ) {
            glUniform3f(
                shader.uniformCameraPosition,
                modelMatrix.x,
                modelMatrix.y,
                modelMatrix.z
            )
        }
    }
}