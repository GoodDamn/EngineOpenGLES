package good.damn.wrapper.hud.ui

import android.view.MotionEvent
import good.damn.wrapper.hud.bridges.MGBridgeRayIntersect
import good.damn.hud.MGIListenerDelta
import good.damn.hud.MGIListenerDistance
import good.damn.hud.MGIListenerMove
import good.damn.hud.MGIListenerScale
import good.damn.hud.MGITouchable
import good.damn.hud.MGTouchFreeMove
import good.damn.hud.MGTouchScale
import kotlin.math.min

class MGUILayerEditor(
    private val bridgeMatrix: MGBridgeRayIntersect,
    clickLoadUserContent: good.damn.hud.MGIClick,
    clickSwitchDrawerMode: good.damn.hud.MGIClick,
    clickPlaceMesh: good.damn.hud.MGIClick,
    clickTriggerDrawing: good.damn.hud.MGIClick
): good.damn.hud.MGITouchable {

    private val mBtnLoadUserContent = good.damn.hud.MGButtonGL(
        click = clickLoadUserContent
    )

    private val mBtnSwitchWireframe = good.damn.hud.MGButtonGL(
        click = clickSwitchDrawerMode
    )

    private val mBtnPlaceMesh = good.damn.hud.MGButtonGL(
        click = clickPlaceMesh
    )

    private val mBtnTriggerDrawing = good.damn.hud.MGButtonGL(
        click = clickTriggerDrawing
    )

    private val mTouchMove = good.damn.hud.MGTouchFreeMove()
    private val mTouchScale = good.damn.hud.MGTouchScale()

    fun setListenerTouchMove(
        v: good.damn.hud.MGIListenerMove?
    ) = mTouchMove.setListenerMove(v)

    fun setListenerTouchDelta(
        v: good.damn.hud.MGIListenerDelta?
    ) = mTouchMove.setListenerDelta(v)

    fun setListenerTouchDeltaInteract(
        v: good.damn.hud.MGIListenerDelta?
    ) { mTouchScale.onDelta = v }

    fun setListenerTouchScaleInteract(
        v: good.damn.hud.MGIListenerScale?
    ) { mTouchScale.onScale = v }

    fun setListenerTouch3Fingers(
        v: good.damn.hud.MGIListenerDistance?
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