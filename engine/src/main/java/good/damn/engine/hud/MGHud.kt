package good.damn.engine.hud

import android.view.MotionEvent
import good.damn.engine.imports.MGImportA3D
import good.damn.engine.imports.MGImportLevel
import good.damn.engine.imports.MGImportMesh
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.callbacks.MGCallbackOnCameraMovement
import good.damn.engine.opengl.callbacks.MGCallbackOnDeltaInteract
import good.damn.engine.opengl.callbacks.MGCallbackOnScale
import good.damn.engine.opengl.callbacks.MGIListenerOnIntersectPosition
import good.damn.engine.opengl.triggers.MGTriggerSimple
import good.damn.engine.runnables.MGCallbackModelSpawn
import good.damn.engine.sdk.SDVector3
import good.damn.engine.ui.MGUILayerEditor
import good.damn.engine.ui.clicks.MGClickImport
import good.damn.engine.ui.clicks.MGClickPlaceMesh
import good.damn.engine.ui.clicks.MGClickSwitchDrawMode
import good.damn.engine.ui.clicks.MGClickTriggerDrawingFlag

class MGHud(
    requesterUserContent: MGIRequestUserContent,
    informator: MGMInformator,
    switcherDrawMode: MGSwitcherDrawMode
): MGIListenerOnIntersectPosition {

    private val mBridgeMatrix = MGBridgeRayIntersect()

    private val mCallbackOnDeltaInteract = MGCallbackOnDeltaInteract(
        mBridgeMatrix
    )

    private val mCallbackOnCameraMove = MGCallbackOnCameraMovement(
        informator.camera,
        mBridgeMatrix
    ).apply {
        setListenerIntersection(
            this@MGHud
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
        clickLoadUserContent = MGClickImport(
            MGImportLevel(
                informator
            ),
            MGImportMesh(
                informator.poolMeshes,
                mCallbackModelSpawn
            ),
            MGImportA3D(
                informator.poolMeshes,
                mCallbackModelSpawn
            ),
            requesterUserContent
        ),
        clickPlaceMesh = MGClickPlaceMesh(
            mBridgeMatrix
        ),
        clickSwitchDrawerMode = MGClickSwitchDrawMode(
            informator,
            switcherDrawMode
        ),
        clickTriggerDrawing = MGClickTriggerDrawingFlag(
            informator
        )
    ).apply {
        setListenerTouchMove(
            mCallbackOnCameraMove
        )

        setListenerTouchDelta(
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

    override fun onIntersectPosition(
        p: SDVector3
    ) {
        mBridgeMatrix.matrix?.run {
            setPosition(
                p.x,
                p.y,
                p.z
            )
            invalidatePosition()
        }
    }
}