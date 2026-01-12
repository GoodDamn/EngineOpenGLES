package good.damn.wrapper.hud.ui

import android.view.MotionEvent
import good.damn.hud.UIButton
import good.damn.hud.UIIClick
import good.damn.hud.touch.MGIListenerDelta
import good.damn.hud.touch.MGIListenerDistance
import good.damn.hud.touch.MGIListenerMove
import good.damn.hud.touch.MGIListenerScale
import good.damn.hud.touch.MGITouchable
import good.damn.hud.touch.MGTouchFreeMove
import good.damn.hud.touch.MGTouchScale
import good.damn.wrapper.hud.bridges.MGBridgeRayIntersect
import kotlin.math.min

class MGUILayerEditor(
    private val bridgeMatrix: MGBridgeRayIntersect,
    clickLoadUserContent: UIIClick,
    clickSwitchDrawerMode: UIIClick,
    clickPlaceMesh: UIIClick,
    clickTriggerDrawing: UIIClick
): MGITouchable {

    private val mBtnLoadUserContent = UIButton(
        click = clickLoadUserContent
    )

    private val mBtnSwitchWireframe = UIButton(
        click = clickSwitchDrawerMode
    )

    private val mBtnPlaceMesh = UIButton(
        click = clickPlaceMesh
    )

    private val mBtnTriggerDrawing = UIButton(
        click = clickTriggerDrawing
    )

    private val mTouchMove = MGTouchFreeMove()
    private val mTouchScale = MGTouchScale()

    fun setListenerTouchMove(
        v: MGIListenerMove?
    ) = mTouchMove.setListenerMove(v)

    fun setListenerTouchDelta(
        v: MGIListenerDelta?
    ) = mTouchMove.setListenerDelta(v)

    fun setListenerTouchDeltaInteract(
        v: MGIListenerDelta?
    ) { mTouchScale.onDelta = v }

    fun setListenerTouchScaleInteract(
        v: MGIListenerScale?
    ) { mTouchScale.onScale = v }

    fun setListenerTouch3Fingers(
        v: MGIListenerDistance?
    ) { mTouchScale.onDistance = v }

    fun layout(
        left: Float,
        top: Float,
        width: Float,
        height: Float
    ) {
        mTouchMove.setBoundsMove(
            left,
            top,
            width * 0.5f,
            height
        )

        val targetLen = min(
            width,
            height
        )

        val btnLen = targetLen * 0.1f

        mTouchMove.setBoundsDelta(
            width * 0.5f,
            0f,
            width,
            height
        )

        mBtnSwitchWireframe.bounds(
            width - btnLen,
            height - btnLen,
            btnLen,
            btnLen
        )

        mBtnLoadUserContent.bounds(
            width - btnLen,
            0f,
            btnLen,
            btnLen
        )

        mBtnPlaceMesh.bounds(
            (width - btnLen) * 0.5f,
            height - btnLen,
            btnLen,
            btnLen
        )

        mBtnTriggerDrawing.bounds(
            0f,
            height - btnLen,
            btnLen,
            btnLen
        )

        val btnLen2 = min(
            width,
            height
        ) * 0.8f
        val midX = (width - btnLen2) * 0.5f
        val midY = (height - btnLen2) * 0.5f

        mTouchScale.setBounds(
            midX, midY,
            midX + btnLen2,
            midY + btnLen2
        )
    }

    override fun onTouchEvent(
        event: MotionEvent
    ): Boolean {
        val action = event.actionMasked
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            val index = event.actionIndex
            val x = event.getX(
                index
            )

            val y = event.getY(
                index
            )

            mBtnLoadUserContent.intercept(x,y)
            mBtnSwitchWireframe.intercept(x,y)
            mBtnPlaceMesh.intercept(x,y)
            mBtnTriggerDrawing.intercept(x,y)

            if (bridgeMatrix.intersectUpdate != null && mTouchScale.onTouchEvent(
                event
            )) {
                return true
            }

            mTouchMove.onTouchEvent(
                event
            )

            return true
        }

        if (bridgeMatrix.intersectUpdate != null) {
            mTouchScale.onTouchEvent(
                event
            )
        }

        mTouchMove.onTouchEvent(
            event
        )

        return true
    }
}