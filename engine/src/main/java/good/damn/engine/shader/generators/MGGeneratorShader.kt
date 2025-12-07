package good.damn.engine.shader.generators

import good.damn.engine.models.MGMGeneratorMaterial
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

    fun material(
        srcCodeMaterial: MGMGeneratorMaterial
    ): MGGeneratorShader {
        mBuilderSourceFragment.append(
            srcCodeMaterial.srcCodeFragment
        )

        return this
    }

    fun generate(
        material: Array<MGMGeneratorMaterial>
    ): String {

        return mBuilderSourceFragment.append(
                source.fragMain.replace(
                    "$0",
                    "${MGGeneratorMaterial.ID_MATERIAL_FUNC}${material[0].idMethod}();"
                )
            ).toString()


    }
}