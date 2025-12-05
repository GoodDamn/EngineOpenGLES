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
        appendIndexedFunc(
            idSampler,
            source.fragTexture
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
        idSampler: String
    ): MGGeneratorMaterial {
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

    private inline fun appendIndexedFunc(
        id: String,
        src: String
    ) {
        val indexedSrc = replaceParam(
            src, 0, id
        )

        Log.d("TAG", "appendIndexedMapFunc: $id ---> $indexedSrc")

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
        "$$param".toRegex(),
        arg
    )

    private data class MGMMaterialGen(
        val type: MGEnumTextureType,
        val id: String
    )
}