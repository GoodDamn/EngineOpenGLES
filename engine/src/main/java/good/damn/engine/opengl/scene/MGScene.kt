package good.damn.engine.opengl.scene

import android.opengl.GLES30.GL_CLAMP_TO_EDGE
import android.opengl.GLES30.GL_CW
import android.opengl.GLES30.GL_REPEAT
import android.opengl.GLSurfaceView
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import good.damn.engine.handlers.MGRunnableCollision
import good.damn.engine.imports.MGImportA3D
import good.damn.engine.imports.MGImportLevel
import good.damn.engine.imports.MGImportMesh
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.sdk.MGVector3
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.callbacks.MGCallbackOnCameraMovement
import good.damn.engine.opengl.callbacks.MGCallbackOnDeltaInteract
import good.damn.engine.opengl.callbacks.MGCallbackOnScale
import good.damn.engine.opengl.callbacks.MGIListenerOnIntersectPosition
import good.damn.engine.opengl.camera.MGCameraFree
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshTextureSwitch
import good.damn.engine.opengl.drawers.modes.MGDrawModeOpaque
import good.damn.engine.opengl.drawers.modes.MGDrawModeSingleMap
import good.damn.engine.opengl.drawers.modes.MGDrawModeSingleShader
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.modes.MGDrawModeSingleShaderNormals
import good.damn.engine.opengl.entities.MGLight
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.managers.MGManagerTriggerLight
import good.damn.engine.opengl.managers.MGManagerTriggerMesh
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.matrices.MGMatrixTranslate
import good.damn.engine.opengl.models.MGMShader
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderOpaque
import good.damn.engine.opengl.shaders.MGShaderSingleMap
import good.damn.engine.opengl.shaders.MGShaderSingleMapInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleModeNormals
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.opengl.triggers.MGTriggerLight
import good.damn.engine.opengl.triggers.MGTriggerSimple
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodBox
import good.damn.engine.runnables.MGCallbackModelSpawn
import good.damn.engine.scripts.MGScriptDirLight
import good.damn.engine.ui.MGUILayerEditor
import good.damn.engine.ui.clicks.MGClickImport
import good.damn.engine.ui.clicks.MGClickPlaceMesh
import good.damn.engine.ui.clicks.MGClickSwitchDrawMode
import good.damn.engine.ui.clicks.MGClickTriggerDrawingFlag
import good.damn.engine.utils.MGUtilsBitmap
import good.damn.engine.utils.MGUtilsBuffer
import good.damn.engine.utils.MGUtilsFile
import good.damn.engine.utils.MGUtilsVertIndices
import java.util.concurrent.ConcurrentLinkedQueue
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.random.Random

open class MGScene(
    private val switcherDrawMode: MGSwitcherDrawMode
): GLSurfaceView.Renderer {

    private val mTextureDefault = MGTexture(
        MGEnumTextureType.DIFFUSE
    )

    private val mTextureMetallicNo = MGTexture(
        MGEnumTextureType.METALLIC
    )

    private val mTextureEmissiveNo = MGTexture(
        MGEnumTextureType.EMISSIVE
    )

    private val mTextureOpacityNo = MGTexture(
        MGEnumTextureType.OPACITY
    )

    private val mTextureNormalNo = MGTexture(
        MGEnumTextureType.NORMAL
    )


    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {

        MGUtilsBitmap.loadBitmap(
            "textures/black.jpg"
        )?.run {
            mTextureMetallicNo.glTextureSetup(
                this,
                GL_REPEAT
            )

            mTextureEmissiveNo.glTextureSetup(
                this,
                GL_REPEAT
            )

            mTextureNormalNo.glTextureSetup(
                this,
                GL_REPEAT
            )
        }

        MGUtilsBitmap.loadBitmap(
            "textures/white.jpg"
        )?.run {
            mTextureDefault.glTextureSetup(
                this,
                GL_REPEAT
            )

            mTextureOpacityNo.glTextureSetup(
                this,
                GL_REPEAT
            )
        }

        modelMatrixCamera.setPosition(
            0f, 0f, 0f
        )
        modelMatrixCamera.invalidatePosition()
        mDrawerLightDirectional.setPosition(
            2.8929129f,
            10.986f,
            -9.247298f
        )
    }

    override fun onSurfaceChanged(
        gl: GL10?,
        width: Int,
        height: Int
    ) {}

    override fun onDrawFrame(
        gl: GL10?
    ) {
        switcherDrawMode
            .currentDrawerMode
            .draw()
    }
}