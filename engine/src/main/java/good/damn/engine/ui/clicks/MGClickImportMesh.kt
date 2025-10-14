package good.damn.engine.ui.clicks

import android.util.Log
import good.damn.engine.MGEngine
import good.damn.engine.interfaces.MGIListenerOnGetUserContent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.extensions.copyTo
import good.damn.engine.opengl.models.MGMUserContent
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.runnables.MGICallbackModel
import good.damn.engine.runnables.MGRunnableImportModel
import good.damn.engine.ui.MGIClick
import java.io.File
import java.io.FileInputStream

class MGClickImportMesh(
    private val handler: MGHandlerGl,
    private val callbackModel: MGICallbackModel,
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
        userContent: MGMUserContent,
        extension: String
    ) {
        val temp = File(
            MGEngine.DIR_PUBLIC_TEMP,
            "${System.currentTimeMillis()}.$extension"
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
                temp
            )
        )
    }

    private inline fun actWithUri(
        userContent: MGMUserContent
    ) {
        val uri = userContent.uri
        if (uri.contains("fbx")) {
            processModel(
                userContent,
                "fbx"
            )
            return
        }

        if (uri.contains("obj")) {
            processModel(
                userContent,
                "obj"
            )
            return
        }

        if (uri.contains("3ds")) {
            processModel(
                userContent,
                "3ds"
            )
            return
        }
    }
}