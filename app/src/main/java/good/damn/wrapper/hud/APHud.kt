package good.damn.wrapper.hud

import android.os.Handler
import android.os.Looper
import good.damn.common.COIRunnableBounds
import good.damn.wrapper.imports.APImportImage
import good.damn.wrapper.interfaces.APIRequestUserContent
import good.damn.engine2.opengl.drawmodes.MGRunglCycleDrawerModes
import good.damn.hud.UIButton
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
import good.damn.wrapper.imports.APImportLight
import good.damn.wrapper.imports.APImportLevel
import good.damn.wrapper.imports.APImportModel
import good.damn.wrapper.imports.APImportImplTempFile
import good.damn.wrapper.imports.APMImportMisc
import good.damn.engine2.providers.MGMProviderGL
import good.damn.engine2.providers.MGIProviderGLRegister
import good.damn.wrapper.export.APExportLights
import good.damn.wrapper.hud.ui.clicks.APClickExport
import java.util.LinkedList
import kotlin.math.min
import kotlin.times

class APHud(
    switcherDrawMode: MGRunglCycleDrawerModes,
    requesterUserContent: APIRequestUserContent
): MGIProviderGLRegister {

    private val mBridgeMatrix = APBridgeRayIntersect()

    private val mCallbackOnDeltaInteract = APCallbackOnDeltaInteract(
        mBridgeMatrix
    )

    private val mCallbackOnCameraMove = APCallbackOnCameraMovement(
        mBridgeMatrix
    ).apply {
        setListenerIntersection(
            APCallbackOnIntersectPosition(
                mBridgeMatrix
            )
        )
    }

    private val mCallbackModelSpawn = APCallbackModelSpawn(
        mBridgeMatrix
    )

    private val misc = APMImportMisc(
        Handler(
            Looper.getMainLooper()
        ),
        mCallbackModelSpawn,
        ByteArray(1024)
    )

    private val mImportImage = APImportImage()
    private val mImportTempLevel = APImportLevel(
        misc
    )

    private val mImportTempModel = APImportModel(
        misc
    )

    private val mImportLight = APImportLight(
        mBridgeMatrix
    )

    private val mImportTempA3d = APImportImplA3D(
        misc
    )

    private val mImportExportSceneLights = APExportLights()

    private val mClickSwitchDrawMode = APClickSwitchDrawMode(
        switcherDrawMode
    )

    private val mClickTriggerDrawingFlag = APClickTriggerDrawingFlag()

    private val mBtnImport = UIButton(
        APClickImport(
            requesterUserContent,
            arrayOf(
                mImportImage,
                mImportLight,
                APImportImplTempFile(
                    mImportTempLevel
                ),
                APImportImplTempFile(
                    mImportTempModel
                ),
                APImportImplTempFile(
                    mImportTempA3d
                ),
                mImportExportSceneLights
            )
        )
    )

    private val mBtnExport = UIButton(
        APClickExport(
            mImportExportSceneLights
        )
    )

    private val mBtnPlaceMesh = UIButton(
        APClickPlaceMesh(
            mBridgeMatrix
        )
    )

    private val mBtnSwitchDrawMode = UIButton(
        mClickSwitchDrawMode
    )

    private val mBtnTriggerDrawFlag = UIButton(
        mClickTriggerDrawingFlag
    )

    val layerEditor = APUILayerEditor(
        bridgeMatrix = mBridgeMatrix
    ).apply {
        buttons.add(
            mBtnImport
        )

        buttons.add(
            mBtnPlaceMesh
        )

        buttons.add(
            mBtnSwitchDrawMode
        )

        buttons.add(
            mBtnTriggerDrawFlag
        )

        buttons.add(
            mBtnExport
        )

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
        w: Int,
        h: Int
    ) {
        val width = w.toFloat()
        val height = h.toFloat()

        val btnSize = min(
            width, height
        ) * 0.1f

        mBtnImport.apply {
            x = width - btnSize
            y = 0f
            this.width = btnSize
            this.height = btnSize
        }

        mBtnPlaceMesh.apply {
            x = (width - btnSize) * 0.5f
            y = height - btnSize
            this.width = btnSize
            this.height = btnSize
        }

        mBtnSwitchDrawMode.apply {
            x = width - btnSize
            y = height - btnSize
            this.width = btnSize
            this.height = btnSize
        }

        mBtnTriggerDrawFlag.apply {
            x = 0f
            y = height - btnSize
            this.width = btnSize
            this.height = btnSize
        }

        mBtnExport.apply {
            x = width - btnSize
            y = (height - btnSize) * 0.5f
            this.width = btnSize
            this.height = btnSize
        }

        layerEditor.layout(
            0f,
            0f,
            width,
            height
        )
    }

    override fun registerGlProvider(
        provider: MGMProviderGL
    ) {
        mCallbackModelSpawn.glProvider = provider
        mCallbackOnCameraMove.glProvider = provider
        mImportImage.glProvider = provider
        mImportLight.glProvider = provider
        mImportTempLevel.glProvider = provider
        mClickSwitchDrawMode.glProvider = provider
        mClickTriggerDrawingFlag.glProvider = provider
        mImportExportSceneLights.glProvider = provider
    }
}