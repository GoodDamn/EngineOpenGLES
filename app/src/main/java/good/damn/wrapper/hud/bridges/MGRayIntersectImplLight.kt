package good.damn.wrapper.hud.bridges

import android.util.Log
import good.damn.engine.opengl.triggers.MGMatrixTriggerLight
import good.damn.engine.sdk.SDVector3
import good.damn.engine.sdk.models.SDMLightPointInterpolation

class MGRayIntersectImplLight(
    private val modelMatrix: MGMatrixTriggerLight,
    private val lightInterpolation: SDMLightPointInterpolation
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
            subtractRadius(
                dtScale
            )
            invalidateRadius()
            calculateInvertTrigger()
            lightInterpolation.radius = radius
            Log.d("TAG", "updateScale: $radius")
        }
    }

    override fun updateDelta(
        dx: Float,
        dy: Float
    ) = Unit
}