package good.damn.engine.opengl.entities

import good.damn.engine.opengl.matrices.MGMMatrix
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.shaders.MGIShaderModel

class MGMesh(
    drawerSwitch: MGDrawerModeSwitch,
    shader: MGIShaderModel,
    model: MGMMatrix
): MGDrawerMeshSwitch(
    drawerSwitch,
    MGDrawerPositionEntity(
        drawerSwitch,
        shader,
        model
    )
)