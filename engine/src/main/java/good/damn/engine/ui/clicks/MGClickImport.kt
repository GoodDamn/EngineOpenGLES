package good.damn.engine.ui.clicks

import android.util.Log
import good.damn.engine.MGEngine
import good.damn.engine.imports.MGImportA3D
import good.damn.engine.imports.MGImportLevel
import good.damn.engine.imports.MGImportMesh
import good.damn.engine.interfaces.MGIListenerOnGetUserContent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.extensions.copyTo
import good.damn.engine.opengl.models.MGMUserContent
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.runnables.MGICallbackModel
import good.damn.engine.runnables.MGRunnableImportA3D
import good.damn.engine.runnables.MGRunnableImportFileTemp
import good.damn.engine.ui.MGIClick
import good.damn.ia3d.A3DImport
import good.damn.ia3d.stream.A3DInputStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.File
import kotlin.experimental.or

class MGClickImport(
    importLevel: MGImportLevel,
    importMesh: MGImportMesh,
    importA3D: MGImportA3D,
    private val requester: MGIRequestUserContent
): MGIClick,
MGIListenerOnGetUserContent {

    private val mBuffer = ByteArray(
        8192
    )

    private val runnableImportMesh = MGRunnableImportFileTemp(
        importMesh,
        mBuffer
    )

    private val runnableImportLevel = MGRunnableImportFileTemp(
        importLevel,
        mBuffer
    )

    private val runnableImportA3D = MGRunnableImportA3D(
        importA3D
    )

    private val mScope = CoroutineScope(
        Dispatchers.IO
    )

    override fun onClick() {
        requester.requestUserContent(
            this,
            arrayOf(
                "*/*",
                //"application/3ds",
                //"application/obj"
            )
        )
    }

    override fun onGetUserContent(
        userContent: MGMUserContent
    ) {
        val uri = userContent.fileName
        if (uri.contains(".fbx") ||
            uri.contains(".obj") ||
            uri.contains(".3ds")
        ) {
            createTempFile(
                userContent
            )?.run {
                processModel(
                    this
                )
            }
            return
        }

        if (uri.contains(".a3d")) {
            val asset = A3DImport.createFromStream(
                A3DInputStream(
                    userContent.stream
                ),
                mBuffer
            ) ?: return

            runnableImportA3D.asset = asset
            runnableImportA3D.fileName = userContent.fileName
            runnableImportA3D.run()

            return
        }

        if (uri.contains(".map")) {
            createTempFile(
                userContent
            )?.run {
                processLevel(
                    this
                )
            }
            return
        }
    }

    private inline fun processModel(
        temp: File
    ) {
        runnableImportMesh.fileTemp = temp
        runnableImportMesh.run()
    }

    private inline fun processLevel(
        temp: File
    ) {
        runnableImportLevel.fileTemp = temp
        mScope.launch {
            runnableImportLevel.run()
        }
    }

    private fun createTempFile(
        userContent: MGMUserContent
    ): File? {
        val temp = File(
            MGEngine.DIR_PUBLIC_TEMP,
            userContent.fileName
        )

        if (temp.exists()) {
            temp.delete()
        }

        if (!temp.createNewFile()) {
            return null
        }

        userContent.stream.copyTo(
            temp.outputStream()
        )

        return temp
    }
}