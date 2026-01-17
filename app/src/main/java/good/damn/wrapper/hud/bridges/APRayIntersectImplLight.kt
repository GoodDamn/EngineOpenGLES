package good.damn.wrapper.hud.bridges

import android.util.Log
import good.damn.logic.MGMatrixTriggerLight
import good.damn.engine.sdk.SDVector3
import good.damn.engine.sdk.models.SDMLightPointInterpolation

class APRayIntersectImplLight(
    private val modelMatrix: good.damn.logic.MGMatrixTriggerLight,
    private val lightInterpolation: SDMLightPointInterpolation
): APIRayIntersectUpdate {

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