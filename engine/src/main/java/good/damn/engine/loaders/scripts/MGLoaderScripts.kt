package good.damn.engine.loaders.scripts

import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.scripts.MGScriptDirLight
import good.damn.engine.utils.MGUtilsFile

object MGLoaderScripts {

    fun executeDirLight(
        directionalLight: MGDrawerLightDirectional
    ) {
        val scriptDirLight = MGScriptDirLight(
            MGUtilsFile.getPublicFile(
                "scripts"
            ),
            directionalLight
        )

        scriptDirLight.execute()
    }

}