package good.damn.engine.touch.callbacks

import good.damn.engine.sdk.SDVector3
import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.camera.MGCameraFree
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.touch.MGIListenerDelta
import good.damn.engine.touch.MGIListenerDistance
import good.damn.engine.touch.MGIListenerMove

class MGCallbackOnCameraMovement(
    private val camera: MGCameraFree,
    private val glHandler: MGHandlerGl,
    private val bridge: MGBridgeRayIntersect
): MGIListenerDelta,
MGIListenerMove,
MGIListenerDistance {

    private var mListenerIntersect: MGIListenerOnIntersectPosition? = null
    private val mPointCamera = SDVector3(0f)

    fun setListenerIntersection(
        v: MGIListenerOnIntersectPosition?
    ) {
        mListenerIntersect = v
    }

    override fun onDelta(
        dx: Float,
        dy: Float
    ) {
        camera.addRotation(
            dx * 0.001f,
            dy * 0.001f
        )
        camera.invalidatePosition(
            glHandler
        )
        updateIntersection()
    }

    override fun onMove(
        x: Float,
        y: Float,
        directionX: Float,
        directionY: Float
    ) {
        camera.addPosition(
            x, y,
            directionX,
            directionY
        )

        camera.invalidatePosition(
            glHandler
        )

        updateIntersection()
    }

    override fun onDistance(
        dst: Float
    ) {
        bridge.distance += dst
        camera.invalidatePosition(
            glHandler
        )
        updateIntersection()
    }

    private inline fun updateIntersection() {
        mPointCamera.x = camera.modelMatrix.x
        mPointCamera.y = camera.modelMatrix.y
        mPointCamera.z = camera.modelMatrix.z

        val direction = camera.direction
        bridge.outPointLead.apply {
            x = mPointCamera.x + direction.x * bridge.distance
            y = mPointCamera.y + direction.y * bridge.distance
            z = mPointCamera.z + direction.z * bridge.distance
        }

        mListenerIntersect?.onIntersectPosition(
            bridge.outPointLead
        )
    }

}