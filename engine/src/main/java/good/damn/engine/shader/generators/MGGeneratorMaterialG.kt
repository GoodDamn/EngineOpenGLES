package good.damn.engine.shader.generators

import good.damn.engine.shader.MGShaderSource

class MGGeneratorMaterialG(
    private val source: MGShaderSource
) {
    private val mBuilderSrcFragment = StringBuilder().append(
        source.fragDefer1
    )

    fun diffuse() = mBuilderSrcFragment.append(
        source.fragDeferDiffuse
    )

    fun diffuseNo() = mBuilderSrcFragment.append(
        source.fragDeferDiffuseNo
    )

    fun specular() = apply {
        mBuilderSrcFragment.append(
            source.fragDeferSpecular
        )
    }

    fun specularNo() = apply {
        mBuilderSrcFragment.append(
            source.fragDeferSpecularNo
        )
    }

    fun opacity() = apply {
        mBuilderSrcFragment.append(
            source.fragDeferOpacity
        )
    }

    fun opacityNo() = apply {
        mBuilderSrcFragment.append(
            source.fragDeferOpacityNo
        )
    }

    fun build() = mBuilderSrcFragment.append(
        source.fragDeferMain
    ).toString()

}