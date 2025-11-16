package good.damn.engine.loaders

import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.runnables.MGRunnableGenTexture
import good.damn.engine.utils.MGUtilsBitmap
import good.damn.engine.utils.MGUtilsFile
import good.damn.mapimporter.models.MIMAtlasRect
import good.damn.mapimporter.models.MIMMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MGLoaderLevelTextures(
    private val scope: CoroutineScope,
    private val handlerGl: MGHandlerGl,
    private val poolTextures: MGPoolTextures,
    private val localPathLibTextures: String
) {

    var isLoadCompleted = false
        private set

    fun loadTextures(
        map: MIMMap
    ) {
        isLoadCompleted = false
        scope.launch {
            for (j in map.atlases) {
                for (atlas in j.rects) {
                    if (poolTextures.get(
                            atlas.name
                        ) != null
                    ) {
                        continue
                    }
                    loadMaps(
                        atlas
                    )
                }
            }
            isLoadCompleted = true
        }
    }

    private inline fun loadMaps(
        atlas: MIMAtlasRect
    ) {

        poolTexture(
            "${atlas.name}.png",
            MGEnumTextureType.DIFFUSE
        )

        poolTexture(
            "${atlas.name}_m.jpg",
            MGEnumTextureType.METALLIC
        )

        poolTexture(
            "${atlas.name}_e.jpg",
            MGEnumTextureType.EMISSIVE
        )

    }

    private inline fun poolTexture(
        textureName: String,
        textureType: MGEnumTextureType
    ) {
        val texturePath = "$localPathLibTextures/$textureName"
        val bitmap = MGUtilsBitmap.loadBitmap(
            texturePath
        ) ?: return

        val texture = MGTexture(
            textureType
        )

        handlerGl.post(
            MGRunnableGenTexture(
                texture,
                bitmap
            )
        )

        poolTextures.add(
            textureName,
            MGTexture(
                textureType
            )
        )
    }

}