package good.damn.wrapper.hud

import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import good.damn.wrapper.imports.MGImportImage
import good.damn.wrapper.imports.MGImportImplLight
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.wrapper.hud.bridges.MGBridgeRayIntersect
import good.damn.wrapper.hud.callbacks.MGCallbackOnCameraMovement
import good.damn.wrapper.hud.callbacks.MGCallbackOnDeltaInteract
import good.damn.wrapper.hud.callbacks.MGCallbackOnIntersectPosition
import good.damn.wrapper.hud.callbacks.MGCallbackOnScale
import good.damn.engine.opengl.triggers.MGTriggerSimple
import good.damn.wrapper.imports.MGCallbackModelSpawn
import good.damn.wrapper.hud.ui.MGUILayerEditor
import good.damn.wrapper.hud.ui.clicks.MGClickImport
import good.damn.wrapper.hud.ui.clicks.MGClickPlaceMesh
import good.damn.wrapper.hud.ui.clicks.MGClickSwitchDrawMode
import good.damn.wrapper.hud.ui.clicks.MGClickTriggerDrawingFlag
import good.damn.wrapper.imports.MGImportImplA3D
import good.damn.wrapper.imports.MGImportImplLevel
import good.damn.wrapper.imports.MGImportImplModel
import good.damn.wrapper.imports.MGMImportMisc

class APHud(
    requesterUserContent: MGIRequestUserContent,
    informator: MGMInformator,
    switcherDrawMode: MGSwitcherDrawMode
) {

    private val mBridgeMatrix = MGBridgeRayIntersect()

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

    private val mLayerEditor = MGUILayerEditor(
        clickLoadUserContent = MGMImportMisc(
            Handler(
                Looper.getMainLooper()
            ),
            mCallbackModelSpawn,
            ByteArray(1024)
        ).run {
            MGClickImport(
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
        clickPlaceMesh = MGClickPlaceMesh(
            mBridgeMatrix
        ),
        clickSwitchDrawerMode = MGClickSwitchDrawMode(
            informator,
            switcherDrawMode
        ),
        clickTriggerDrawing = MGClickTriggerDrawingFlag(
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