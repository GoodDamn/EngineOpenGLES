package good.damn.engine.shader.generators

import android.util.Log
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.shader.MGShaderSource
import java.util.LinkedList

class MGGeneratorMaterial(
    private val source: MGShaderSource
) {

    private val mBuilderSourceFragment = StringBuilder()

    private val listIds = LinkedList<MGMMaterialGen>()

    fun mapTexture(
        idSampler: String,
        type: MGEnumTextureType
    ): MGGeneratorMaterial {
        appendTexture(
            idSampler
        )
        listIds.add(
            MGMMaterialGen(
                type,
                idSampler
            )
        )
        return this
    }

    fun mapTextureNo(
        idSampler: String,
        type: MGEnumTextureType
    ): MGGeneratorMaterial {
        appendIndexedFunc(
            idSampler,
            source.fragTextureNo
        )
        listIds.add(
            MGMMaterialGen(
                type,
                idSampler
            )
        )
        return this
    }

    fun normalMapping(
        idSampler: String
    ): MGGeneratorMaterial {
        appendSampler2D(
            idSampler
        )

        appendIndexedFunc(
            idSampler,
            source.fragNormalMap
        )

        listIds.add(
            MGMMaterialGen(
                MGEnumTextureType.NORMAL,
                idSampler
            )
        )
        return this
    }

    fun normalVertex(
        idSampler: String
    ): MGGeneratorMaterial {
        appendIndexedFunc(
            idSampler,
            source.fragNormalNo
        )
        listIds.add(
            MGMMaterialGen(
                MGEnumTextureType.NORMAL,
                idSampler
            )
        )
        return this
    }

    fun generate(
        idMaterial: String
    ): String {
        var srcMaterial = source.fragMaterial

        for (i in listIds) {
            srcMaterial = srcMaterial.replace(
                "$${i.type.v+1}".toRegex(),
                i.id
            )
        }

        return srcMaterial.replace(
            "$0".toRegex(),
            idMaterial
        )
    }

    private inline fun appendTexture(
        id: String
    ) {
        appendSampler2D(
            id
        )

        appendIndexedFunc(
            id,
            source.fragTexture
        )
    }

    private fun appendSampler2D(
        id: String
    ) {
        mBuilderSourceFragment.append(
            "uniform sampler2D $id;"
        )
    }

    private inline fun appendIndexedFunc(
        id: String,
        src: String
    ) {
        val indexedSrc = src.replace(
            "$0".toRegex(),
            id
        )

        Log.d("TAG", "appendIndexedMapFunc: $id ---> $indexedSrc")

        mBuilderSourceFragment.append(
            indexedSrc
        )
    }

    private data class MGMMaterialGen(
        val type: MGEnumTextureType,
        val id: String
    )
}