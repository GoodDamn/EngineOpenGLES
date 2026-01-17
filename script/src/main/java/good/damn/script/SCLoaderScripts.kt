package good.damn.script

import good.damn.common.utils.COUtilsFile
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional

object SCLoaderScripts {

    @JvmStatic
    fun executeDirLight(
        directionalLight: MGDrawerLightDirectional
    ) {
        val scriptDirLight = SCScriptDirLight(
            COUtilsFile.getPublicFile(
                "scripts"
            ),
            directionalLight
        )

        scriptDirLight.execute()
    }

}