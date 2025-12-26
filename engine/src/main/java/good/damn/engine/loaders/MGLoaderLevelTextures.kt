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
    private val handlerGl: MGHandlerGl,
    private val poolTextures: MGPoolTextures,
    private val localPathLibTextures: String
) {

    val isLoadCompleted: Boolean
        get() = mCurrentIndex >= mCountTextures

    private val mLock = Any()

    private var mCurrentIndex = 0
    private var mCountTextures = 0

    fun loadTextures(
        map: MIMMap
    ) {
        mCountTextures = 0
        mCurrentIndex = 0
        for (j in map.atlases) {
            for (atlas in j.rects) {
                if (poolTextures.get(
                        atlas.name
                    ) != null
                ) {
                    incrementIndex()
                    continue
                }

                loadMaps(
                    atlas
                )
                mCountTextures += 5
            }
        }
    }

    private inline fun loadMaps(
        atlas: MIMAtlasRect
    ) {
        poolTexture(
            "${atlas.name}.jpg"
        )

        poolTexture(
            "${atlas.name}_m.jpg"
        )

        poolTexture(
            "${atlas.name}_e.jpg"
        )

        poolTexture(
            "${atlas.name}_o.jpg"
        )

        poolTexture(
            "${atlas.name}_n.jpg"
        )
    }

    private inline fun incrementIndex() {
        mCurrentIndex++
    }

    private fun poolTexture(
        textureName: String
    ) {
        val texturePath = "$localPathLibTextures/$textureName"
        val bitmap = MGUtilsBitmap.loadBitmap(
            texturePath
        )

        if (bitmap == null) {
            incrementIndex()
            return
        }

        val texture = MGTexture(
            MGTextureActive.default
        )

        handlerGl.post(
            MGRunnableTextureSetupBitmap(
                MGTextureBitmap(
                    texture
                ),
                bitmap
            )
        )

        poolTextures.add(
            textureName,
            texture
        )

        incrementIndex()
    }

}