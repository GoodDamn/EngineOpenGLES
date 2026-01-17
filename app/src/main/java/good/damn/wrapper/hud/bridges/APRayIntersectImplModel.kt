package good.damn.wrapper.hud.bridges

import good.damn.logic.MGMatrixTriggerMesh
import good.damn.engine.sdk.SDVector3

class APRayIntersectImplModel(
    private val modelMatrix: good.damn.logic.MGMatrixTriggerMesh
): APIRayIntersectUpdate {

    companion object {
        private const val SCALE_FACTOR = 0.0001f
    }

    override fun updateIntersectPosition(
        v: SDVector3
    ) {
        modelMatrix.apply {
            setPosition(
                v.x,
                v.y,
                v.z,
            )
            invalidatePosition()
        }
    }

    override fun updateScale(
        dtScale: Float
    ) {
        modelMatrix.apply {
            val scale = dtScale * SCALE_FACTOR
            subtractScale(
                scale,
                scale,
                scale
            )
            invalidateScaleRotation()
            calculateInvertTrigger()
            calculateNormals()
        }
    }

    override fun updateDelta(
        dx: Float,
        dy: Float
    ) {
        modelMatrix.apply {
            addRotation(
                0f,
                dx * 0.5f,
                dy * -0.5f,
            )
            invalidateScaleRotation()
            calculateInvertTrigger()
            calculateNormals()
        }
    }
}