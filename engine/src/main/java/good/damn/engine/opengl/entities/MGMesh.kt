package good.damn.engine.opengl.entities

import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.matrices.MGMatrixModel
import good.damn.engine.opengl.matrices.MGMatrixNormal
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.matrices.MGMatrixTransformation
import good.damn.engine.opengl.matrices.MGMatrixTranslate
import good.damn.engine.opengl.shaders.MGIShaderModel

class MGMesh(
    drawerSwitch: MGDrawerModeSwitch,
    shader: MGIShaderModel,
    model: MGMatrixTranslate,
    normals: MGMatrixNormal?
): MGDrawerMeshSwitch(
    drawerSwitch,
    MGDrawerPositionEntity(
        drawerSwitch,
        shader,
        model
    ),
    normals
)