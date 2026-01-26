package good.damn.wrapper.hud

import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import good.damn.common.COHandlerGl
import good.damn.common.camera.COICameraFree
import good.damn.engine2.models.MGMInformatorShader
import good.damn.engine2.models.MGMManagers
import good.damn.engine2.models.MGMParameters
import good.damn.engine2.opengl.MGMGeometry
import good.damn.wrapper.imports.MGImportImage
import good.damn.wrapper.imports.MGImportImplLight
import good.damn.wrapper.interfaces.MGIRequestUserContent
import good.damn.engine2.opengl.MGSwitcherDrawMode
import good.damn.engine2.opengl.pools.MGMPools
import good.damn.wrapper.hud.bridges.APBridgeRayIntersect
import good.damn.wrapper.hud.callbacks.MGCallbackOnCameraMovement
import good.damn.wrapper.hud.callbacks.MGCallbackOnDeltaInteract
import good.damn.wrapper.hud.callbacks.MGCallbackOnIntersectPosition
import good.damn.wrapper.hud.callbacks.MGCallbackOnScale
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
    camera: COICameraFree,
    requesterUserContent: MGIRequestUserContent,
    switcherDrawMode: MGSwitcherDrawMode,
    parameters: MGMParameters,
    pools: MGMPools,
    shaders: MGMInformatorShader,
    managers: MGMManagers,
    geometry: MGMGeometry,
    glHandler: COHandlerGl
) {

    private val mBridgeMatrix = APBridgeRayIntersect()

    private val mCallbackOnDeltaInteract = MGCallbackOnDeltaInteract(
        mBridgeMatrix
    )

    private val mCallbackOnCameraMove = MGCallbackOnCameraMovement(
        camera,
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
        pools.meshes,
        shaders,
        geometry,
        parameters,
        managers.managerTrigger,
        managers.managerFrustrum
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
                        geometry,
                        pools,
                        shaders,
                        glHandler,
                        managers
                    ),
                    MGImportImplA3D(
                        this
                    ),
                    MGImportImage(
                        pools,
                        shaders,
                        parameters
                    ),
                    MGImportImplLight(
                        mBridgeMatrix,
                        managers
                    )
                ),
                requesterUserContent
            )
        },
        clickPlaceMesh = APClickPlaceMesh(
            mBridgeMatrix
        ),
        clickSwitchDrawerMode = APClickSwitchDrawMode(
            glHandler,
            switcherDrawMode
        ),
        clickTriggerDrawing = APClickTriggerDrawingFlag(
            parameters
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