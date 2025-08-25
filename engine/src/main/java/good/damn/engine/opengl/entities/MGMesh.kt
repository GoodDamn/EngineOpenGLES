package good.damn.engine.opengl.entities

import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.shaders.MGIShader
import good.damn.engine.opengl.shaders.MGIShaderCamera

class MGMesh(
    drawerSwitch: MGDrawerModeSwitch,
    shader: MGIShaderCamera,
    model: MGMMatrix
): MGDrawerMeshSwitch(
    drawerSwitch,
    MGDrawerPositionEntity(
        drawerSwitch,
        shader,
        model
    )
)