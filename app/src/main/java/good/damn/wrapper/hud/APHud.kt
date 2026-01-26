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
import good.damn.wrapper.imports.APImportImage
import good.damn.wrapper.imports.APImportImplLight
import good.damn.wrapper.interfaces.APIRequestUserContent
import good.damn.engine2.opengl.MGSwitcherDrawMode
import good.damn.engine2.opengl.pools.MGMPools
import good.damn.wrapper.hud.bridges.APBridgeRayIntersect
import good.damn.wrapper.hud.callbacks.APCallbackOnCameraMovement
import good.damn.wrapper.hud.callbacks.APCallbackOnDeltaInteract
import good.damn.wrapper.hud.callbacks.APCallbackOnIntersectPosition
import good.damn.wrapper.hud.callbacks.APCallbackOnScale
import good.damn.wrapper.imports.APCallbackModelSpawn
import good.damn.wrapper.hud.ui.APUILayerEditor
import good.damn.wrapper.hud.ui.clicks.APClickImport
import good.damn.wrapper.hud.ui.clicks.APClickPlaceMesh
import good.damn.wrapper.hud.ui.clicks.APClickSwitchDrawMode
import good.damn.wrapper.hud.ui.clicks.APClickTriggerDrawingFlag
import good.damn.wrapper.imports.APImportImplA3D
import good.damn.wrapper.imports.APImportImplLevel
import good.damn.wrapper.imports.APImportImplModel
import good.damn.wrapper.imports.APMImportMisc

class APHud(
    camera: COICameraFree,
    requesterUserContent: APIRequestUserContent,
    switcherDrawMode: MGSwitcherDrawMode,
    parameters: MGMParameters,
    pools: MGMPools,
    shaders: MGMInformatorShader,
    managers: MGMManagers,
    geometry: MGMGeometry,
    glHandler: COHandlerGl
) {

    private val mBridgeMatrix = APBridgeRayIntersect()

    private val mCallbackOnDeltaInteract = APCallbackOnDeltaInteract(
        mBridgeMatrix
    )

    private val mCallbackOnCameraMove = APCallbackOnCameraMovement(
        camera,
        mBridgeMatrix
    ).apply {
        setListenerIntersection(
            APCallbackOnIntersectPosition(
                mBridgeMatrix
            )
        )
    }

    private val mCallbackModelSpawn = APCallbackModelSpawn(
        mBridgeMatrix,
        pools.meshes,
        shaders,
        geometry,
        parameters,
        managers.managerTrigger,
        managers.managerFrustrum
    )

    private val mLayerEditor = APUILayerEditor(
        clickLoadUserContent = APMImportMisc(
            Handler(
                Looper.getMainLooper()
            ),
            mCallbackModelSpawn,
            ByteArray(1024)
        ).run {
            APClickImport(
                arrayOf(
                    APImportImplModel(
                        this
                    ),
                    APImportImplLevel(
                        this,
                        geometry,
                        pools,
                        shaders,
                        glHandler,
                        managers
                    ),
                    APImportImplA3D(
                        this
                    ),
                    APImportImage(
                        pools,
                        shaders,
                        parameters
                    ),
                    APImportImplLight(
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
            APCallbackOnScale(
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