package good.damn.common.volume

import android.opengl.Matrix
import good.damn.common.camera.COCameraProjection
import good.damn.common.vertex.MGArrayVertexManager
import good.damn.engine.sdk.process.SDIProcessTime
import java.util.concurrent.ConcurrentLinkedQueue

class COManagerFrustrum(
    private val camera: COCameraProjection,
    vertices: MGArrayVertexManager
): SDIProcessTime {
    val volumes = ConcurrentLinkedQueue<
        COIVolume
    >()

    private val matrixClipSpace = FloatArray(16)
    private val matrixViewSpace = FloatArray(16)

    private val mVector4Clip = FloatArray(4)

    private val mPositions = Array(
        vertices.countVertices
    ) {
        FloatArray(4).apply {
            for (i in 0 until 3) {
                this[i] = vertices.getVertexBufferData(
                    it, i
                )
            }
            this[3] = 1.0f
        }
    }

    override fun onProcessTime(
        dt: Float
    ) {
        for (volume in volumes) {
            var isOnFrustrum = false
            for (position in mPositions) {
                Matrix.multiplyMM(
                    matrixViewSpace,
                    0,
                    camera.modelMatrix.model,
                    0,
                    volume.modelMatrix.model,
                    0
                )

                Matrix.multiplyMM(
                    matrixClipSpace,
                    0,
                    camera.projection,
                    0,
                    matrixViewSpace,
                    0
                )

                Matrix.multiplyMV(
                    mVector4Clip,
                    0,
                    matrixClipSpace,
                    0,
                    position,
                    0
                )

                if (!isNotOnAxis(mVector4Clip, 0)) {
                    isOnFrustrum = true
                    break
                }

                if (!isNotOnAxis(mVector4Clip, 1)) {
                    isOnFrustrum = true
                    break
                }

                if (!isNotOnAxis(mVector4Clip, 2)) {
                    isOnFrustrum = true
                    break
                }
            }

            volume.isOnFrustrum(
                isOnFrustrum
            )
        }
    }

    private inline fun isNotOnAxis(
        vector: FloatArray,
        axis: Int
    ) = vector[axis] > vector[3]
        || vector[axis] < -vector[3]
}