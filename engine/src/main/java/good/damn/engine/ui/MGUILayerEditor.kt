package good.damn.engine.ui

import android.view.MotionEvent
import good.damn.engine.opengl.ui.MGButtonGL
import good.damn.engine.opengl.ui.MGSeekBarGl
import good.damn.engine.touch.MGIListenerDelta
import good.damn.engine.touch.MGIListenerMove
import good.damn.engine.touch.MGITouchable
import good.damn.engine.touch.MGTouchFreeMove
import good.damn.engine.touch.MGTouchScale

class MGUILayerEditor(
    clickLoadUserContent: MGIClick,
    clickSwitchDrawerMode: MGIClick,
    clickPlaceMesh: MGIClick,
    seekAmbientChanged: MGIListenerValueChanged
): MGITouchable {

    private val mBtnLoadUserContent = MGButtonGL(
        click = clickLoadUserContent
    )

    private val mBtnSwitchWireframe = MGButtonGL(
        click = clickSwitchDrawerMode
    )

    private val mBtnPlaceMesh = MGButtonGL(
        click = clickPlaceMesh
    )

    private val mTouchMove = MGTouchFreeMove()
    private val mTouchScale = MGTouchScale()

    private val mBarSeekAmbient = MGSeekBarGl(
        onChangeValue = seekAmbientChanged
    )

    fun setListenerTouchMove(
        v: MGIListenerMove?
    ) = mTouchMove.setListenerMove(v)

    fun setListenerTouchDelta(
        v: MGIListenerDelta?
    ) = mTouchMove.setListenerDelta(v)

    fun setListenerTouchDeltaInteract(
        v: MGIListenerDelta?
    ) { mTouchScale.onDelta = v }

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

        val btnLen = width * 0.1f

        mTouchMove.setBoundsDelta(
            width * 0.5f,
            0f,
            width,
            height
        )

        mBarSeekAmbient.bounds(
            0f,
            0f,
            btnLen,
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

        val btnLen2 = btnLen
        val midX = (width - btnLen2) * 0.5f
        val midY = (height - btnLen2) * 0.5f
        mBtnPlaceMesh.bounds(
            midX,
            height - btnLen2,
            btnLen2,
            btnLen2
        )

        mTouchScale.setBounds(
            midX, midY,
            midX + btnLen2,
            midY + btnLen2
        )
    }

    override fun onTouchEvent(
        event: MotionEvent
    ) {
        val action = event.actionMasked
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            val x = event.getX(
                event.actionIndex
            )

            val y = event.getY(
                event.actionIndex
            )

            mBarSeekAmbient.intercept(x,y)
            mBtnLoadUserContent.intercept(x,y)
            mBtnSwitchWireframe.intercept(x,y)
            mBtnPlaceMesh.intercept(x,y)

            mTouchScale.onTouchEvent(
                event
            )

            if (mTouchScale.touchId != -1) {
                return
            }

            mTouchMove.onTouchEvent(
                event
            )
            return
        }

        mTouchScale.onTouchEvent(
            event
        )

        mTouchMove.onTouchEvent(
            event
        )
    }
}