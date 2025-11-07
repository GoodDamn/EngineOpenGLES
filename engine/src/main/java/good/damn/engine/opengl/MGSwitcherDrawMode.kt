package good.damn.engine.opengl

import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitchNormals
import good.damn.engine.opengl.drawers.MGDrawerMeshTextureSwitch
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.models.MGMDrawMode
import java.util.concurrent.ConcurrentLinkedQueue

class MGSwitcherDrawMode(
    private val meshSky: MGDrawerMeshTextureSwitch,
    private val meshes: ConcurrentLinkedQueue<MGDrawerMeshMaterialSwitch>,
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
            drawMode
        )

        meshes.forEach {
            it.switchDrawMode(
                drawMode
            )
        }
    }
}