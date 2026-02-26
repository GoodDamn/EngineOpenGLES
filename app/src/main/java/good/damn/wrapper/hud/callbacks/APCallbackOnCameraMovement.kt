package good.damn.wrapper.hud.callbacks

import good.damn.hud.touch.UIIListenerDelta
import good.damn.hud.touch.UIIListenerDistance
import good.damn.hud.touch.UIIListenerMove
import good.damn.wrapper.hud.bridges.APBridgeRayIntersect
import good.damn.engine2.providers.MGProviderGL

class APCallbackOnCameraMovement(
    private val bridge: APBridgeRayIntersect
): MGProviderGL(),
UIIListenerDelta,
UIIListenerMove,
UIIListenerDistance {

    private var mListenerIntersect: APIListenerOnIntersectPosition? = null

    fun setListenerIntersection(
        v: APIListenerOnIntersectPosition?
    ) {
        mListenerIntersect = v
    }

    override fun onDelta(
        dx: Float,
        dy: Float
    ) {
        glProvider.apply {
            camera.addRotation(
                dx * 0.001f,
                dy * 0.001f,
                0f
            )
            camera.invalidatePosition()
            updateIntersection()
        }
    }

    override fun onMove(
        x: Float,
        y: Float,
        directionX: Float,
        directionY: Float
    ) {
        glProvider.apply {
            camera.addPosition(
                x, y,
                directionX,
                directionY
            )
            camera.invalidatePosition()
            updateIntersection()
        }
    }

    override fun onDistance(
        dst: Float
    ) {
        bridge.distance += dst
        glProvider.camera.invalidatePosition()
        updateIntersection()
    }

    private inline fun updateIntersection() {
        glProvider.apply {
            val direction = camera.direction
            bridge.outPointLead.apply {
                x = camera.modelMatrix.x + direction.x * bridge.distance
                y = camera.modelMatrix.y + direction.y * bridge.distance
                z = camera.modelMatrix.z + direction.z * bridge.distance
            }

            mListenerIntersect?.onIntersectPosition(
                bridge.outPointLead
            )
        }
    }

}