package good.damn.engine.opengl.bridges.intersect

import good.damn.engine.opengl.bridges.MGIRayIntersectUpdate
import good.damn.engine.opengl.triggers.MGMatrixTriggerMesh
import good.damn.engine.sdk.SDVector3

class MGRayIntersectImplModel(
    private val modelMatrix: MGMatrixTriggerMesh
): MGIRayIntersectUpdate {

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
            subtractScale(
                dtScale,
                dtScale,
                dtScale
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