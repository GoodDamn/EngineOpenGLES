package good.damn.engine.opengl

import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.entities.MGMesh
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.models.MGMDrawMode
import java.util.concurrent.ConcurrentLinkedQueue

class MGSwitcherDrawMode(
    private val meshSky: MGMesh,
    private val meshes: ConcurrentLinkedQueue<MGDrawerMeshSwitch>,
    initDrawerMode: MGIDrawer
) {
    var currentDrawerMode = initDrawerMode
        private set

    fun switchDrawMode(
        drawMode: MGEnumDrawMode,
        model: MGMDrawMode
    ) {
        currentDrawerMode = model.drawer

        meshSky.switchDrawMode(
            model.shaderSky,
            model.shaderNormalsSky,
            model.shaderTexture,
            drawMode,
        )

        meshes.forEach {
            it.switchDrawMode(
                model.shaderDefault,
                model.shaderNormals,
                model.shaderTexture,
                drawMode
            )
        }
    }
}