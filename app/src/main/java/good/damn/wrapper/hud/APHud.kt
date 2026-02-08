package good.damn.wrapper.hud

import android.os.Handler
import android.os.Looper
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
import kotlin.math.min

class APHud(
    switcherDrawMode: MGRunglCycleDrawerModes,
    requesterUserContent: APIRequestUserContent,
    width: Float,
    height: Float
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

    val layerEditor = APUILayerEditor(
        bridgeMatrix = mBridgeMatrix
    ).apply {

        val btnSize = min(
            width, height
        )


        buttons.add(
            UIButton(
                APClickImport(
                    requesterUserContent
                ).apply {
                    imports.add(
                        mImportImage
                    )

                    imports.add(
                        mImportLight
                    )

                    imports.add(
                        APImportImplTempFile(
                            mImportTempLevel
                        )
                    )

                    imports.add(
                        APImportImplTempFile(
                            mImportTempModel
                        )
                    )

                    imports.add(
                        APImportImplTempFile(
                            mImportTempA3d
                        )
                    )
                }
            ).apply {
                x = width - btnSize
                y = 0f
                this.width = btnSize
                this.height = btnSize
            }
        )

        buttons.add(
            UIButton(
                APClickPlaceMesh(
                    mBridgeMatrix
                )
            ).apply {
                x = (width - btnSize) * 0.5f
                y = height - btnSize
                this.width = btnSize
                this.height = btnSize
            }
        )

        buttons.add(
            UIButton(
                APClickSwitchDrawMode(
                    switcherDrawMode
                )
            ).apply {
                x = width - btnSize
                y = height - btnSize
                this.width = btnSize
                this.height = btnSize
            }
        )

        buttons.add(
            UIButton(
                APClickTriggerDrawingFlag()
            ).apply { 
                x = 0f
                y = height - btnSize
                this.width = btnSize
                this.height = btnSize
            }
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

    override fun registerGlProvider(
        provider: MGMProviderGL
    ) {
        mCallbackModelSpawn.glProvider = provider
        mCallbackOnCameraMove.glProvider = provider
        mImportImage.glProvider = provider
        mImportLight.glProvider = provider
        mImportTempLevel.glProvider = provider
    }
}