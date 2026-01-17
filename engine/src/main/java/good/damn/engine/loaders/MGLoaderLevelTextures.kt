package good.damn.engine.loaders

import good.damn.logic.pools.MGPoolTextures
import good.damn.mapimporter.models.MIMAtlasRect
import good.damn.mapimporter.models.MIMMap

class MGLoaderLevelTextures(
    private val poolTextures: good.damn.logic.pools.MGPoolTextures,
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