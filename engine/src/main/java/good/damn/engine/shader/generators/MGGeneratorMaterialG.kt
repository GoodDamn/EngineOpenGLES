package good.damn.engine.shader.generators

import good.damn.engine.shader.MGShaderSource

class MGGeneratorMaterialG(
    private val source: MGShaderSource
) {

    private val mBuilderSrcFragment = StringBuilder().append(
        source.fragDefer1
    )

    fun diffuse(): MGGeneratorMaterialG {
        mBuilderSrcFragment.append(
            source.fragDeferDiffuse
        )
        return this
    }

    fun diffuseNo(): MGGeneratorMaterialG {
        mBuilderSrcFragment.append(
            source.fragDeferDiffuseNo
        )
        return this
    }

    fun specular(): MGGeneratorMaterialG {
        mBuilderSrcFragment.append(
            source.fragDeferSpecular
        )
        return this
    }

    fun specularNo(): MGGeneratorMaterialG {
        mBuilderSrcFragment.append(
            source.fragDeferSpecularNo
        )
        return this
    }

    fun build() = mBuilderSrcFragment.append(
        source.fragDeferMain
    ).toString()

}