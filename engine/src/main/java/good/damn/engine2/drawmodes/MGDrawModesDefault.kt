package good.damn.engine2.drawmodes

import good.damn.apigl.drawers.GLDrawerLightPass
import good.damn.apigl.enums.GLEnumLightPass
import good.damn.apigl.enums.GLEnumDrawModeMesh
import good.damn.engine2.providers.MGMProviderGL

class MGDrawModesDefault(
    providerModel: MGMProviderGL
) {

    private val mDrawModes = arrayOf(
        providerModel.shaders.lightPasses[
            GLEnumLightPass.OPAQUE
        ].run {
            MGDrawModeOpaque(
                GLDrawerLightPass(
                    inputTextures,
                    providerModel.drawers.drawerQuad
                ),
                shader
            )
        },
        createTextureDrawMode(
            GLEnumLightPass.DIFFUSE,
            providerModel,
            GLEnumDrawModeMesh.TRIANGLES
        ),
        createTextureDrawMode(
            GLEnumLightPass.DEPTH,
            providerModel,
            GLEnumDrawModeMesh.TRIANGLES
        ),
        createTextureDrawMode(
            GLEnumLightPass.NORMAL,
            providerModel,
            GLEnumDrawModeMesh.TRIANGLES
        ),
        createTextureDrawMode(
            GLEnumLightPass.DIFFUSE,
            providerModel,
            GLEnumDrawModeMesh.LINES
        )
    )

    val switcherDrawMode = MGRunglCycleDrawerModes(
        mDrawModes
    )

    private inline fun createTextureDrawMode(
        typeLightPass: GLEnumLightPass,
        providerModel: MGMProviderGL,
        drawModeMesh: GLEnumDrawModeMesh
    ) = providerModel.shaders.lightPasses[
        typeLightPass
    ].run {
        MGDrawModeTexture(
            GLDrawerLightPass(
                inputTextures,
                providerModel.drawers.drawerQuad
            ),
            drawModeMesh,
            shader
        )
    }

}