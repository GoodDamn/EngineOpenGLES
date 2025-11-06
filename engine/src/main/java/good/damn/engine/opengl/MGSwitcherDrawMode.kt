package good.damn.engine.opengl

import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitchNormals
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.models.MGMDrawMode
import java.util.concurrent.ConcurrentLinkedQueue

class MGSwitcherDrawMode(
    private val meshSky: MGDrawerMeshSwitch,
    private val meshes: ConcurrentLinkedQueue<MGDrawerMeshSwitchNormals>,
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
            model.shaderTexture,
            drawMode
        )

        meshes.forEach {
            it.switchDrawMode(
                model.shaderNormals
            )
            it.switchDrawMode(
                model.shaderDefault,
                model.shaderTexture,
                drawMode
            )
        }
    }
}