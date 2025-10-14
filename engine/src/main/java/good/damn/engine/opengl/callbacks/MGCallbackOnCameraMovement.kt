package good.damn.engine.opengl.callbacks

import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.camera.MGCameraFree
import good.damn.engine.opengl.rays.MGRayIntersection
import good.damn.engine.touch.MGIListenerDelta
import good.damn.engine.touch.MGIListenerMove

class MGCallbackOnCameraMovement(
    private val camera: MGCameraFree,
    private val bridge: MGBridgeRayIntersect
): MGIListenerDelta,
MGIListenerMove {

    private var mListenerIntersect: MGIListenerOnIntersectPosition? = null

    private val mRayIntersection = MGRayIntersection()
    private val mPointCamera = MGVector(0f)

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
        camera.invalidatePosition()
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
        camera.invalidatePosition()
        updateIntersection()
    }

    private inline fun updateIntersection() {
        mPointCamera.x = camera.modelMatrix.x
        mPointCamera.y = camera.modelMatrix.y
        mPointCamera.z = camera.modelMatrix.z

        mRayIntersection.intersect(
            mPointCamera,
            camera.direction,
            bridge.outPointLead
        )

        mListenerIntersect?.onIntersectPosition(
            bridge.outPointLead
        )
    }

}