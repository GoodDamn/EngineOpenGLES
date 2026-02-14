package good.damn.wrapper.activities

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.WindowManager
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import good.damn.common.COHandlerGl
import good.damn.common.COHandlerGlExecutor
import good.damn.common.utils.COUtilsFile
import good.damn.engine2.providers.MGMProviderGL
import good.damn.engine2.providers.MGProviderGL
import good.damn.engine2.sensors.MGManagerSensor
import good.damn.engine2.sensors.MGSensorGyroscope
import good.damn.script.SCScriptLightPlacement
import good.damn.wrapper.interfaces.APIListenerOnGetUserContent
import good.damn.wrapper.interfaces.APIRequestUserContent
import good.damn.wrapper.models.APMUserContent
import good.damn.wrapper.callbacks.APCallbackResultAllFiles
import good.damn.wrapper.callbacks.APCallbackResultAllFilesApi30
import good.damn.wrapper.hud.APHud
import good.damn.wrapper.launchers.APLauncherContent
import good.damn.wrapper.renderer.APRendererEditor
import good.damn.wrapper.renderer.APRendererHandler
import good.damn.wrapper.viewmodels.APViewModelFileAccessApi30
import good.damn.wrapper.viewmodels.APViewModelFileAccessImpl
import good.damn.wrapper.views.APViewGlHandler

class APActivityLevelEditor
: AppCompatActivity(),
ActivityResultCallback<Uri?>, APIRequestUserContent {

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

    private var managerSensor: MGManagerSensor? = null

    private lateinit var managerSensorApi: SensorManager

    private var mCallbackRequestUserContent: APIListenerOnGetUserContent? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        managerSensorApi = getSystemService(
            Context.SENSOR_SERVICE
        ) as SensorManager

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

    override fun onDestroy() {
        mContentLauncher.unregister()
        mViewModelAllFiles.unregisterLauncher()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        managerSensor?.register(
            managerSensorApi
        )
    }

    override fun onPause() {
        super.onPause()
        managerSensor?.unregister(
            managerSensorApi
        )
    }

    override fun onActivityResult(
        result: Uri?
    ) {
        if (result == null) {
            return
        }

        val mimeType = contentResolver.getType(
            result
        ) ?: return

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
        } ?: return

        val stream = contentResolver.openInputStream(
            result
        ) ?: return

        mCallbackRequestUserContent?.onGetUserContent(
            APMUserContent(
                fileName,
                mimeType,
                stream
            )
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
            handlerExecutor
        )

        val renderer = APRendererEditor(
            glHandler
        )

        val sensors = arrayListOf(
            MGSensorGyroscope()
        )

        val hud = APHud(
            renderer.switcherDrawMode,
            this
        )

        glHandler.post(
            renderer
        )

        glHandler.registerCycleTask(
            renderer.switcherDrawMode
        )

        sensors.forEach {
            it.glProvider = renderer.providerModel
        }

        hud.registerGlProvider(
            renderer.providerModel
        )

        managerSensor = MGManagerSensor(
            sensors
        )

        loadScripts(
            renderer.providerModel
        )

        setContentView(
            APViewGlHandler(
                this,
                renderer.providerModel.managers.managerProcessTime,
                handler,
                hud
            )
        )
    }

    private inline fun loadScripts(
        providerModel: MGMProviderGL
    ) {
        val scriptLightPlacement = SCScriptLightPlacement(
            COUtilsFile.getPublicFile(
                "scripts"
            ),
            providerModel.managers.managerProcessTime,
            providerModel.managers.managerLight,
            providerModel.managers.managerFrustrum
        )
        scriptLightPlacement.execute()
    }
}