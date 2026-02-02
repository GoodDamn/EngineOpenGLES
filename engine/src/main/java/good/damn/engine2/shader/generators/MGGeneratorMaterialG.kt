package good.damn.engine2.shader.generators

import good.damn.engine2.shader.MGShaderSource

class MGGeneratorMaterialG(
    private val source: MGShaderSource
) {
    private val mBuilderSrcFragment = StringBuilder().append(
        source.fragDefer1
    )

    fun componeEntity(
        src: String
    ) = apply {
        mBuilderSrcFragment.append(
            src
        )
    }

    fun build() = mBuilderSrcFragment.append(
        source.fragDeferMain
    ).toString()

}