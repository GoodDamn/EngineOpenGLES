package good.damn.wrapper.renderer

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30.*
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import good.damn.apigl.arrays.GLArrayVertexConfigurator
import good.damn.apigl.arrays.pointers.GLPointerAttribute
import good.damn.apigl.buffers.GLBuffer
import good.damn.apigl.buffers.GLBufferUniform
import good.damn.apigl.buffers.GLBufferUniformCamera
import good.damn.apigl.drawers.GLDrawerFramebufferG
import good.damn.apigl.drawers.GLDrawerLightDirectional
import good.damn.apigl.drawers.GLDrawerLightPass
import good.damn.apigl.drawers.GLDrawerLights
import good.damn.apigl.drawers.GLDrawerVertexArray
import good.damn.apigl.drawers.GLDrawerVolumes
import good.damn.apigl.enums.GLEnumArrayVertexConfiguration
import good.damn.apigl.framebuffer.GLFrameBufferG
import good.damn.apigl.framebuffer.GLFramebuffer
import good.damn.apigl.shaders.GLShaderGeometryPassModel
import good.damn.apigl.shaders.GLShaderMaterial
import good.damn.apigl.shaders.base.GLBinderAttribute
import good.damn.apigl.shaders.creators.GLShaderCreatorGeomPassInstanced
import good.damn.apigl.shaders.creators.GLShaderCreatorGeomPassModel
import good.damn.apigl.shaders.lightpass.GLShaderLightPass
import good.damn.apigl.shaders.lightpass.GLShaderLightPassPointLight
import good.damn.common.COHandlerGl
import good.damn.common.COIRunnableBounds
import good.damn.wrapper.interfaces.APIRequestUserContent
import good.damn.engine2.loaders.texture.MGLoaderTextureAsync
import good.damn.engine2.models.MGMInformatorShader
import good.damn.common.vertex.COMArrayVertexManager
import good.damn.common.camera.COCameraFree
import good.damn.common.COHandlerGlExecutor
import good.damn.common.camera.COCameraProjection
import good.damn.common.camera.COMCamera
import good.damn.common.matrices.COMatrixTranslate
import good.damn.common.utils.COUtilsFile
import good.damn.common.volume.COManagerFrustrum
import good.damn.engine2.opengl.models.MGMLightPass
import good.damn.engine.ASObject3d
import good.damn.engine2.camera.GLCameraFree
import good.damn.engine2.camera.GLCameraProjection
import good.damn.engine2.models.MGMManagers
import good.damn.engine2.opengl.MGMGeometry
import good.damn.engine2.opengl.MGMVolume
import good.damn.engine2.opengl.MGSky
import good.damn.engine2.opengl.MGSwitcherDrawMode
import good.damn.engine2.opengl.pools.MGPoolMaterials
import good.damn.engine2.opengl.pools.MGPoolMeshesStatic
import good.damn.engine2.opengl.pools.MGPoolTextures
import good.damn.engine2.shader.MGShaderCache
import good.damn.engine2.shader.MGShaderSource
import good.damn.engine.ASUtilsBuffer
import good.damn.engine2.models.MGMParameters
import good.damn.engine2.opengl.MGRunnableCycleSwitcherDrawMode
import good.damn.engine2.opengl.pools.MGMPools
import good.damn.engine2.sensors.MGSensorGyroscope
import good.damn.engine2.utils.MGUtilsFile
import good.damn.engine2.utils.MGUtilsVertIndices
import good.damn.logic.process.LGManagerProcessTime
import good.damn.logic.triggers.managers.LGManagerTriggerMesh
import good.damn.script.SCLoaderScripts
import good.damn.script.SCScriptLightPlacement
import good.damn.wrapper.files.APFile
import good.damn.wrapper.hud.APHud
import java.util.concurrent.ConcurrentLinkedQueue

class APRendererHandler
: GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "MGRendererLevelEditor"
    }

    private val mHandlerGlExecutor = COHandlerGlExecutor()

    private val mHandlerGl = COHandlerGl(
        mHandlerGlExecutor.queue,
        mHandlerGlExecutor.queueCycle,
    )

    init {
        mHandlerGl.registerCycleTask(
            MGRunnableCycleSwitcherDrawMode(
                mSwitcherDrawMode
            )
        )
    }

    private var mWidth = 0
    private var mHeight = 0

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {
        MGUtilsFile.glWriteExtensions()
    }

    override fun onSurfaceChanged(
        gl: GL10?,
        width: Int,
        height: Int
    ) {
        Log.d(TAG, "onSurfaceChanged: ${Thread.currentThread().name}")

        mWidth = width
        mHeight = height
    }

    override fun onDrawFrame(
        gl: GL10?
    ) {
        mHandlerGlExecutor.runTasksBounds(
            mWidth, mHeight
        )

        mHandlerGlExecutor.runCycle(
            mWidth, mHeight
        )
    }
}