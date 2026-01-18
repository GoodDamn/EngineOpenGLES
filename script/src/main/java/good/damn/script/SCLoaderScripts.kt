package good.damn.script

import good.damn.apigl.drawers.GLDrawerLightDirectional
import good.damn.common.utils.COUtilsFile

object SCLoaderScripts {

    @JvmStatic
    fun executeDirLight(
        directionalLight: GLDrawerLightDirectional
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