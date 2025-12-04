package good.damn.engine.shader.generators

import good.damn.engine.shader.MGShaderSource
import good.damn.engine.utils.MGUtilsAsset
import good.damn.engine.utils.MGUtilsFile
import java.io.File

class MGGeneratorShader(
    private val source: MGShaderSource
) {

    private val mBuilderSourceFragment = StringBuilder()
        .append(source.frag1)

    fun metallicMap(): MGGeneratorShader {
        mBuilderSourceFragment.append(
            "uniform sampler2D textMetallic;"
        )
        mBuilderSourceFragment.append(
            source.fragMetallicMap
        )
        return this
    }

    fun metallicNo(): MGGeneratorShader {
        mBuilderSourceFragment.append(
            source.fragMetallicNo
        )
        return this
    }

    fun emissiveMap(): MGGeneratorShader {
        mBuilderSourceFragment.append(
            "uniform sampler2D textEmissive;"
        )
        mBuilderSourceFragment.append(
            source.fragEmissiveMap
        )
        return this
    }

    fun emissiveNo(): MGGeneratorShader {
        mBuilderSourceFragment.append(
            source.fragEmissiveNo
        )
        return this
    }

    fun opacityMap(): MGGeneratorShader {
        mBuilderSourceFragment.append(
            "uniform sampler2D textOpacity;"
        )
        mBuilderSourceFragment.append(
            source.fragOpacityMap
        )
        return this
    }

    fun opacityNo(): MGGeneratorShader {
        mBuilderSourceFragment.append(
            source.fragOpacityNo
        )
        return this
    }

    fun specular(): MGGeneratorShader {
        mBuilderSourceFragment.append(
            source.fragSpecular
        )
        return this
    }

    fun specularNo(): MGGeneratorShader {
        mBuilderSourceFragment.append(
            source.fragSpecularNo
        )
        return this
    }

    fun lighting(): MGGeneratorShader {
        mBuilderSourceFragment.append(
            source.fragLight
        )
        return this
    }

    fun normalMapping(): MGGeneratorShader {
        mBuilderSourceFragment.append(
            "uniform sampler2D textNormal;"
        )
        mBuilderSourceFragment.append(
            source.fragNormalMap
        )
        return this
    }

    fun normalVertex(): MGGeneratorShader {
        mBuilderSourceFragment.append(
            source.fragNormalNo
        )
        return this
    }

    fun generate() = mBuilderSourceFragment.append(
        source.frag2
    ).toString()
}