package good.damn.wrapper.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.opengl.GLES30
import android.opengl.GLES30.GL_UNIFORM_BUFFER
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.WindowManager
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import good.damn.apigl.GLApi
import good.damn.apigl.buffers.GLBuffer
import good.damn.apigl.buffers.GLBufferUniformCamera
import good.damn.common.COHandlerGl
import good.damn.common.COHandlerGlExecutor
import good.damn.engine2.camera.GLCameraFree
import good.damn.engine2.camera.GLCameraProjection
import good.damn.wrapper.interfaces.APIListenerOnGetUserContent
import good.damn.wrapper.interfaces.APIRequestUserContent
import good.damn.wrapper.models.APMUserContent
import good.damn.wrapper.callbacks.APCallbackResultAllFiles
import good.damn.wrapper.callbacks.APCallbackResultAllFilesApi30
import good.damn.wrapper.controllers.APControllerVr
import good.damn.wrapper.hud.APHud
import good.damn.wrapper.launchers.APLauncherContent
import good.damn.wrapper.renderer.APRendererEditor
import good.damn.wrapper.renderer.APRendererHandler
import good.damn.wrapper.renderer.APRendererNew
import good.damn.wrapper.viewmodels.APViewModelFileAccessApi30
import good.damn.wrapper.viewmodels.APViewModelFileAccessImpl
import good.damn.wrapper.views.APViewGlHandler

class APActivityLevelEditor
: AppCompatActivity(),
ActivityResultCallback<Array<Uri>?>,
APIRequestUserContent {

    companion object {
        private const val TAG = "APActivityLevelEditor"
    }

    private val mContentLauncher = APLauncherContent(
        this,
        this
    )

    private val mViewModelAllFiles = if (
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    ) APViewModelFileAccessApi30(
        APCallbackResultAllFilesApi30(
            this
        )
    ) else APViewModelFileAccessImpl(
        APCallbackResultAllFiles(
            this
        )
    )

    private val mControllerVr = APControllerVr()

    private var mCallbackRequestUserContent: APIListenerOnGetUserContent? = null

    override fun onResume() {
        super.onResume()
        mControllerVr.resume()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        val context = this

        val windowController = WindowCompat.getInsetsController(
            window,
            window.decorView
        )

        windowController.systemBarsBehavior = WindowInsetsControllerCompat
            .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams
                .LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        ViewCompat.setOnApplyWindowInsetsListener(
            window.decorView
        ) { view, windowInsets ->

            windowController.hide(
                WindowInsetsCompat.Type.systemBars()
            )

            return@setOnApplyWindowInsetsListener ViewCompat
                .onApplyWindowInsets(
                    view,
                    windowInsets
                )
        }

        if (mViewModelAllFiles.isExternalStorageManager(
            context
        )) {
            initContentView()
            return
        }

        mViewModelAllFiles.registerLauncher(
            this
        )

        requestPermissionAllFiles()
    }

    override fun onPause() {
        super.onPause()
        mControllerVr.pause()
    }

    override fun onDestroy() {
        mControllerVr.destroy()
        mContentLauncher.unregister()
        mViewModelAllFiles.unregisterLauncher()
        super.onDestroy()
    }

    override fun onActivityResult(
        result: Array<Uri>?
    ) {
        if (result == null) {
            return
        }

        val userContent = Array(
            result.size
        ) { generateUserContentModel(
            result[it]
        ) }

        mCallbackRequestUserContent?.onGetUserContent(
            userContent
        )
        mCallbackRequestUserContent = null
    }

    override fun requestUserContent(
        callback: APIListenerOnGetUserContent,
        mimeType: Array<String>
    ) {
        mCallbackRequestUserContent = callback
        mContentLauncher.launch(
            mimeType
        )
    }

    fun requestPermissionAllFiles() {
        mViewModelAllFiles.requestPermissionAllFiles(
            application.packageName
        )
    }

    fun initContentView() {
        val handlerExecutor = COHandlerGlExecutor()
        val glHandler = COHandlerGl(
            handlerExecutor.queue,
            handlerExecutor.queueCycle,
        )

        val handler = APRendererHandler(
            handlerExecutor,
            mControllerVr,
            resources.displayMetrics
        )

        val cameraMatrixPose = FloatArray(16)
        val cameraMatrixProjection = FloatArray(16)

        /*val cameraUniformBuffer = GLBufferUniformCamera(
            GLBuffer(
                GL_UNIFORM_BUFFER
            )
        )

        val cameraPose = GLCameraFree(
            cameraMatrixPose,
            glHandler,
            cameraUniformBuffer
        )

        val cameraProjection = GLCameraProjection(
            cameraMatrixProjection,
            glHandler,
            cameraUniformBuffer
        )*/

        mControllerVr.create { indexEye ->

            mControllerVr.getPose(
                cameraMatrixPose,
                cameraMatrixProjection,
                indexEye,
                0.0f,
                -1.7f,
                0.0f
            )

            handlerExecutor.runCycle(
                mControllerVr.width,
                mControllerVr.height
            )
        }

        val glApi = GLApi()
        val glApiRef = glApi.create()

        val renderer = APRendererNew(
            glHandler,
            glApi,
            glApiRef,
            cameraMatrixPose
        )

        glHandler.post(
            renderer
        )

        /*val hud = APHud(
            renderer.switcherDrawMode,
            this
        )



        glHandler.registerCycleTask(
            renderer.switcherDrawMode
        )

        hud.registerGlProvider(
            renderer.providerModel
        )

        loadScripts(
            renderer.providerModel
        )*/

        setContentView(
            APViewGlHandler(
                this,
                //renderer.providerModel.managers.managerProcessTime,
                handler
                //hud
            )
        )
    }

    private inline fun generateUserContentModel(
        result: Uri
    ): APMUserContent? {
        val mimeType = contentResolver.getType(
            result
        ) ?: return null

        val fileName = contentResolver.query(
            result,
            null,
            null,
            null,
            null
        )?.run {
            val nameIndex = getColumnIndex(
                OpenableColumns.DISPLAY_NAME
            )
            moveToFirst()
            val name = getString(
                nameIndex
            )
            close()
            return@run name
        } ?: return null

        val stream = contentResolver.openInputStream(
            result
        ) ?: return null

        return APMUserContent(
            fileName,
            mimeType,
            stream
        )
    }
}