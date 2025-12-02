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

    fun normalMapping(): MGGeneratorShader {
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