package good.damn.engine.camera

import good.damn.common.COHandlerGl
import good.damn.engine.opengl.buffers.MGBufferUniformCamera
import good.damn.common.camera.COCameraProjection
import good.damn.common.camera.COICameraProjection
import good.damn.common.matrices.COMatrixTranslate
import good.damn.engine.opengl.runnables.misc.MGRunglSendDataProjection
import good.damn.engine.utils.MGUtilsBuffer

class GLCameraProjection(
    private val camera: COCameraProjection,
    private val handler: COHandlerGl,
    private val uniformBufferCamera: MGBufferUniformCamera,
): COICameraProjection {

    private val mProjectionBuffer = MGUtilsBuffer.allocateByte(
        16 * 4
    )

    override val modelMatrix: COMatrixTranslate
        get() = camera.modelMatrix

    override val projection: FloatArray
        get() = camera.projection

    override fun setPerspective(
        width: Int,
        height: Int
    ) {
        camera.setPerspective(
            width,
            height
        )

        mProjectionBuffer.asFloatBuffer().run {
            put(camera.projection)
            position(0)
        }

        handler.post(
            MGRunglSendDataProjection(
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