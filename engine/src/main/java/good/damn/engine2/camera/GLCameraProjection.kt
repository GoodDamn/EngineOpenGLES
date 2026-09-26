package good.damn.engine2.camera

import good.damn.apigl.buffers.GLBufferUniformCamera
import good.damn.apigl.runnables.GLRunglSendDataProjection
import good.damn.common.COHandlerGl
import good.damn.common.camera.COCameraProjection
import good.damn.common.camera.COICameraProjection
import good.damn.engine.sdk.matrices.SDMatrixTranslate
import good.damn.engine.ASUtilsBuffer

class GLCameraProjection(
    private val matrixProjection: FloatArray,
    private val handler: COHandlerGl,
    private val uniformBufferCamera: GLBufferUniformCamera,
) {

    private val mProjectionBuffer = ASUtilsBuffer.allocateByte(
        16 * 4
    )

    fun invalidate() {
        mProjectionBuffer.asFloatBuffer().run {
            put(matrixProjection)
            position(0)
        }

        handler.post(
            GLRunglSendDataProjection(
                mProjectionBuffer,
                uniformBufferCamera
            )
        )
    }

    /*fun drawPosition(
        shader: MGIShaderCameraPosition
    ) {
        camera.modelMatrix.apply {
            synchronized(
                this
            ) {
                glUniform3f(
                    shader.uniformCameraPosition,
                    x, y, z
                )
            }
        }
    }*/
}