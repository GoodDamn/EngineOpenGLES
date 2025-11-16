package good.damn.engine.loaders

import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.utils.MGUtilsFile
import good.damn.mapimporter.models.MIMAtlasRect
import good.damn.mapimporter.models.MIMMap

class MGLoaderLevelTextures(
    private val poolTextures: MGPoolTextures,
    private val localPathLibTextures: String
) {

    fun loadTextures(
        map: MIMMap
    ) {
        for (j in map.atlases) {
            for (r in j.rects) {
                if (poolTextures.get(
                        r.name
                ) != null) {
                    return
                }
            }
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
        val file = MGUtilsFile.getPublicFile(
            texturePath
        )

        if (!file.exists()) {
            return
        }

        poolTextures.add(
            textureName,
            MGTexture.createDefaultAsset(
                texturePath,
                textureType
            )
        )
    }

}