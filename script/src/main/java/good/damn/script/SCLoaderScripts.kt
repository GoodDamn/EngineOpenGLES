package good.damn.script

import good.damn.common.utils.COUtilsFile
import good.damn.apigl.drawers.MGDrawerLightDirectional

object SCLoaderScripts {

    @JvmStatic
    fun executeDirLight(
        directionalLight: good.damn.apigl.drawers.MGDrawerLightDirectional
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