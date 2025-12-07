package good.damn.engine.shader.generators

import android.util.Log
import good.damn.engine.models.MGMGeneratorMaterial
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.shader.MGShaderSource
import java.util.LinkedList

class MGGeneratorMaterial(
    private val source: MGShaderSource
) {

    companion object {
        const val ID_MATERIAL_FUNC = "calculateMaterial_"
    }

    private val mBuilderSourceFragment = StringBuilder()

    private val listIds = LinkedList<MGMMaterialGen>()

    fun mapTexture(
        idSampler: String,
        type: MGEnumTextureType,
        texCoordsScale: Float
    ): MGGeneratorMaterial {
        appendIndexedFuncWithDefValue(
            idSampler,
            source.fragTexture,
            texCoordsScale
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
        type: MGEnumTextureType,
        defaultValue: Float
    ): MGGeneratorMaterial {
        appendIndexedFuncWithDefValue(
            idSampler,
            source.fragTextureNo,
            defaultValue
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
        idSampler: String,
        texCoordsScale: Float
    ): MGGeneratorMaterial {
        appendIndexedFuncWithDefValue(
            idSampler,
            source.fragNormalMap,
            texCoordsScale
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
    ): MGMGeneratorMaterial {
        var srcMaterial = source.fragMaterial

        for (i in listIds) {
            srcMaterial = srcMaterial.replace(
                "$${i.type.v+1}",
                i.id
            )
        }

        val src = srcMaterial.replace(
            "$0",
            idMaterial
        )

        return MGMGeneratorMaterial(
            idMaterial,
            mBuilderSourceFragment.append(
                src
            ).toString()
        )
    }

    private inline fun appendIndexedFunc(
        id: String,
        src: String
    ) {
        val indexedSrc = replaceParam(
            src, 0, id
        )

        mBuilderSourceFragment.append(
            indexedSrc
        )
    }

    private inline fun appendIndexedFuncWithDefValue(
        id: String,
        src: String,
        defaultValue: Float
    ) {
        val indexedSrc = replaceParam(
            src, 0,
            id
        )

        val result = replaceParam(
            indexedSrc, 1,
            defaultValue.toString()
        )

        mBuilderSourceFragment.append(
            result
        )
    }

    private inline fun replaceParam(
        src: String,
        param: Int,
        arg: String
    ) = src.replace(
        "$$param",
        arg
    )

    private data class MGMMaterialGen(
        val type: MGEnumTextureType,
        val id: String
    )
}