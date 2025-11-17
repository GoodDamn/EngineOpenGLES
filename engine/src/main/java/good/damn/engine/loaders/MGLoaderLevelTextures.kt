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

    val isLoadCompleted: Boolean
        get() = mCurrentIndex >= mCountTextures

    private var mCurrentIndex = 0
    private var mCountTextures = 0

    fun loadTextures(
        map: MIMMap
    ) {
        mCountTextures = 0
        mCurrentIndex = 0
        for (j in map.atlases) {
            for (atlas in j.rects) {
                scope.launch {
                    if (poolTextures.get(
                            atlas.name
                        ) != null
                    ) {
                        mCurrentIndex++
                        return@launch
                    }

                    loadMaps(
                        atlas
                    )
                }
                mCountTextures++
            }
        }
    }

    private inline fun loadMaps(
        atlas: MIMAtlasRect
    ) {
        poolTexture(
            "${atlas.name}.jpg",
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

        poolTexture(
            "${atlas.name}_o.jpg",
            MGEnumTextureType.OPACITY
        )
    }

    private inline fun poolTexture(
        textureName: String,
        textureType: MGEnumTextureType
    ) {
        val texturePath = "$localPathLibTextures/$textureName"
        val bitmap = MGUtilsBitmap.loadBitmap(
            texturePath
        )

        if (bitmap == null) {
            mCurrentIndex++
            return
        }

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
            texture
        )

        mCurrentIndex++
    }

}