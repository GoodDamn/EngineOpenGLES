package good.damn.engine.shader.generators

import good.damn.engine.shader.MGShaderSource

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