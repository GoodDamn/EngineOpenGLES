package good.damn.wrapper.hud

import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import good.damn.wrapper.imports.MGImportImage
import good.damn.wrapper.imports.MGImportImplLight
import good.damn.wrapper.interfaces.MGIRequestUserContent
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.wrapper.hud.bridges.APBridgeRayIntersect
import good.damn.wrapper.hud.callbacks.MGCallbackOnCameraMovement
import good.damn.wrapper.hud.callbacks.MGCallbackOnDeltaInteract
import good.damn.wrapper.hud.callbacks.MGCallbackOnIntersectPosition
import good.damn.wrapper.hud.callbacks.MGCallbackOnScale
import good.damn.engine.opengl.triggers.MGTriggerSimple
import good.damn.wrapper.imports.MGCallbackModelSpawn
import good.damn.wrapper.hud.ui.APUILayerEditor
import good.damn.wrapper.hud.ui.clicks.APClickImport
import good.damn.wrapper.hud.ui.clicks.APClickPlaceMesh
import good.damn.wrapper.hud.ui.clicks.APClickSwitchDrawMode
import good.damn.wrapper.hud.ui.clicks.APClickTriggerDrawingFlag
import good.damn.wrapper.imports.MGImportImplA3D
import good.damn.wrapper.imports.MGImportImplLevel
import good.damn.wrapper.imports.MGImportImplModel
import good.damn.wrapper.imports.MGMImportMisc

class APHud(
    requesterUserContent: MGIRequestUserContent,
    informator: MGMInformator,
    switcherDrawMode: MGSwitcherDrawMode
) {

    private val mBridgeMatrix = APBridgeRayIntersect()

    private val mCallbackOnDeltaInteract = MGCallbackOnDeltaInteract(
        mBridgeMatrix
    )

    private val mCallbackOnCameraMove = MGCallbackOnCameraMovement(
        informator.camera.camera,
        mBridgeMatrix
    ).apply {
        setListenerIntersection(
            MGCallbackOnIntersectPosition(
                mBridgeMatrix
            )
        )
    }

    private val mCallbackModelSpawn = MGCallbackModelSpawn(
        mBridgeMatrix,
        MGTriggerSimple(
            informator.drawerLightDirectional
        ),
        informator
    )

    private val mLayerEditor = APUILayerEditor(
        clickLoadUserContent = MGMImportMisc(
            Handler(
                Looper.getMainLooper()
            ),
            mCallbackModelSpawn,
            ByteArray(1024)
        ).run {
            APClickImport(
                arrayOf(
                    MGImportImplModel(
                        this
                    ),
                    MGImportImplLevel(
                        this,
                        informator
                    ),
                    MGImportImplA3D(
                        this
                    ),
                    MGImportImage(
                        informator
                    ),
                    MGImportImplLight(
                        mBridgeMatrix,
                        informator.managerLight,
                        informator.managerLightVolumes
                    )
                ),
                requesterUserContent
            )
        },
        clickPlaceMesh = APClickPlaceMesh(
            mBridgeMatrix
        ),
        clickSwitchDrawerMode = APClickSwitchDrawMode(
            informator,
            switcherDrawMode
        ),
        clickTriggerDrawing = APClickTriggerDrawingFlag(
            informator
        ),
        bridgeMatrix = mBridgeMatrix
    ).apply {
        setListenerTouchMove(
            mCallbackOnCameraMove
        )

        setListenerTouchDelta(
            mCallbackOnCameraMove
        )

        setListenerTouch3Fingers(
            mCallbackOnCameraMove
        )

        setListenerTouchScaleInteract(
            MGCallbackOnScale(
                mBridgeMatrix
            )
        )

        setListenerTouchDeltaInteract(
            mCallbackOnDeltaInteract
        )
    }

    fun layout(
        width: Float,
        height: Float
    ) {
        mLayerEditor.layout(
            0f, 0f,
            width, height
        )
    }

    fun touchEvent(
        event: MotionEvent
    ) = mLayerEditor.onTouchEvent(
        event
    )
}