package good.damn.engine.opengl

import good.damn.engine.MGEngine
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitchNormals
import good.damn.engine.opengl.drawers.MGDrawerMeshTextureSwitch
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
import good.damn.engine.opengl.drawers.modes.MGDrawModeOpaque
import good.damn.engine.opengl.drawers.modes.MGDrawModeSingleMap
import good.damn.engine.opengl.drawers.modes.MGDrawModeSingleShader
import good.damn.engine.opengl.drawers.modes.MGDrawModeSingleShaderNormals
import good.damn.engine.opengl.enums.MGEnumDrawMode
import java.util.concurrent.ConcurrentLinkedQueue

class MGSwitcherDrawMode(
    private val informator: MGMInformator
) {
    private val drawerModeOpaque = MGDrawModeOpaque(
        informator
    )

    private val drawerModeWireframe = MGDrawModeSingleShader(
        informator.shaders.wireframe,
        informator
    )

    private val drawerModeNormals = MGDrawModeSingleShaderNormals(
        informator.shaders.normals,
        informator
    )

    private val drawerModeTexCoords = MGDrawModeSingleShader(
        informator.shaders.texCoords,
        informator
    )

    private val drawerModeTexture = MGDrawModeSingleMap(
        informator.shaders.map,
        informator
    )

    var currentDrawerMode: MGIDrawer = drawerModeOpaque
        private set

    fun switchDrawMode() = when (
        MGEngine.drawMode
    ) {
        MGEnumDrawMode.OPAQUE -> switchDrawMode(
            MGEnumDrawMode.WIREFRAME,
            drawerModeWireframe
        )

        MGEnumDrawMode.WIREFRAME -> switchDrawMode(
            MGEnumDrawMode.NORMALS,
            drawerModeNormals
        )

        MGEnumDrawMode.NORMALS -> switchDrawMode(
            MGEnumDrawMode.TEX_COORDS,
            drawerModeTexCoords
        )

        MGEnumDrawMode.TEX_COORDS -> switchDrawMode(
            MGEnumDrawMode.DIFFUSE,
            drawerModeTexture
        )

        MGEnumDrawMode.DIFFUSE -> switchDrawMode(
            MGEnumDrawMode.METALLIC,
            drawerModeTexture
        )

        MGEnumDrawMode.METALLIC -> switchDrawMode(
            MGEnumDrawMode.EMISSIVE,
            drawerModeTexture
        )

        MGEnumDrawMode.EMISSIVE -> switchDrawMode(
            MGEnumDrawMode.OPAQUE,
            drawerModeOpaque
        )
    }


    private fun switchDrawMode(
        drawMode: MGEnumDrawMode,
        currentDrawer: MGIDrawer
    ) {
        MGEngine.drawMode = drawMode
        currentDrawerMode = currentDrawer

        informator.sky.switchDrawMode(
            drawMode
        )

        informator.meshes.forEach {
            it.switchDrawMode(
                drawMode
            )
        }

        informator.meshesInstanced.forEach {
            it.switchDrawMode(
                drawMode
            )
        }
    }
}