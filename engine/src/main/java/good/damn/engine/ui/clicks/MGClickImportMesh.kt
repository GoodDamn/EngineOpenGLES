package good.damn.engine.ui.clicks

import android.util.Log
import good.damn.engine.MGEngine
import good.damn.engine.interfaces.MGIListenerOnGetUserContent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.extensions.copyTo
import good.damn.engine.opengl.models.MGMUserContent
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.runnables.MGICallbackModel
import good.damn.engine.runnables.MGRunnableImportModel
import good.damn.engine.ui.MGIClick
import java.io.File
import java.io.FileInputStream

class MGClickImportMesh(
    private val handler: MGHandlerGl,
    private val callbackModel: MGICallbackModel,
    private val mPoolMeshes: MGPoolMeshesStatic,
    private val requester: MGIRequestUserContent
): MGIClick,
MGIListenerOnGetUserContent {


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
        actWithUri(
            userContent
        )
    }

    private fun processModel(
        userContent: MGMUserContent
    ) {
        val temp = File(
            MGEngine.DIR_PUBLIC_TEMP,
            userContent.fileName
        )

        if (temp.exists()) {
            temp.delete()
        }

        if (!temp.createNewFile()) {
            return
        }

        userContent.stream.copyTo(
            temp.outputStream()
        )

        handler.post(
            MGRunnableImportModel(
                callbackModel,
                mPoolMeshes,
                temp
            )
        )
    }

    private inline fun actWithUri(
        userContent: MGMUserContent
    ) {
        val uri = userContent.fileName
        if (uri.contains("fbx") ||
            uri.contains("obj") ||
            uri.contains("3ds")
        ) {
            processModel(
                userContent
            )
            return
        }
    }
}