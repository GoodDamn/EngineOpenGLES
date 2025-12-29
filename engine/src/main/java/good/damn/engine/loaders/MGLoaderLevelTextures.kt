package good.damn.engine.loaders

import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.textures.MGTextureActive
import good.damn.engine.opengl.textures.MGTextureBitmap
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.runnables.MGRunnableTextureSetupBitmap
import good.damn.engine.utils.MGUtilsBitmap
import good.damn.mapimporter.models.MIMAtlasRect
import good.damn.mapimporter.models.MIMMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MGLoaderLevelTextures(
    private val poolTextures: MGPoolTextures,
    private val localPathLibTextures: String
) {

    fun loadTextures(
        map: MIMMap
    ) {
        for (j in map.atlases) {
            for (atlas in j.rects) {
                loadMaps(
                    atlas
                )
            }
        }
    }

    private inline fun loadMaps(
        atlas: MIMAtlasRect
    ) {
        poolTextures.loadOrGetFromCache(
            "${atlas.name}.jpg",
            localPathLibTextures
        )

        poolTextures.loadOrGetFromCache(
            "${atlas.name}_m.jpg",
            localPathLibTextures
        )

        poolTextures.loadOrGetFromCache(
            "${atlas.name}_e.jpg",
            localPathLibTextures
        )

        poolTextures.loadOrGetFromCache(
            "${atlas.name}_o.jpg",
            localPathLibTextures
        )

        poolTextures.loadOrGetFromCache(
            "${atlas.name}_n.jpg",
            localPathLibTextures
        )
    }

}