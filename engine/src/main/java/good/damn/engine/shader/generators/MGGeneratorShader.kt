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

    fun generate() = mBuilderSourceFragment.append(
        source.frag2
    ).toString()
}