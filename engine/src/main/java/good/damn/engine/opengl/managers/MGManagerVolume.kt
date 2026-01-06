package good.damn.engine.opengl.managers

import android.opengl.GLES30
import android.opengl.Matrix
import good.damn.engine.opengl.arrays.MGArrayVertexManager
import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.MGIDrawerShader
import good.damn.engine.opengl.drawers.volume.MGIVolume
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.sdk.process.SDIProcessTime
import java.util.concurrent.ConcurrentLinkedQueue

class MGManagerVolume(
    private val camera: MGCamera
): MGIDrawerShader<MGIShaderModel>,
SDIProcessTime {
    private val mVolumes = ConcurrentLinkedQueue<
        MGIVolume
    >()

    private val matrixClipSpace = FloatArray(16)
    private val matrixViewSpace = FloatArray(16)

    private val mVector4Clip = FloatArray(4)
    private lateinit var mPositions: Array<FloatArray>
    private lateinit var mDrawerPrimitive: MGDrawerVertexArray

    fun loadPositions(
        vertices: MGArrayVertexManager
    ) {
        mPositions = Array(
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

        mDrawerPrimitive = MGDrawerVertexArray(
            vertices
        )
    }

    override fun onProcessTime(
        dt: Float
    ) {
        for (volume in mVolumes) {
            var isOnFrustrum = false
            for (position in mPositions) {
                Matrix.multiplyMM(
                    matrixViewSpace,
                    0,
                    camera.modelMatrix.model,
                    0,
                    volume.drawerModel.modelMatrix.model,
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

    override fun draw(
        shader: MGIShaderModel
    ) {
        mVolumes.forEach {
            it.drawerModel.draw(
                shader
            )

            mDrawerPrimitive.draw(
                GLES30.GL_LINES
            )
        }
    }

    fun addVolume(
        drawer: MGIVolume
    ) = mVolumes.add(
        drawer
    )

    private inline fun isNotOnAxis(
        vector: FloatArray,
        axis: Int
    ) = vector[axis] > vector[3]
        || vector[axis] < -vector[3]
}