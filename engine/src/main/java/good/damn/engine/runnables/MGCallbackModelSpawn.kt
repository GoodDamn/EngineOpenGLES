package good.damn.engine.runnables

import good.damn.engine.opengl.MGObject3d
import good.damn.engine.opengl.MGTriggerMeshGroup
import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.managers.MGManagerTriggerMesh
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.triggers.MGITrigger
import java.util.concurrent.ConcurrentLinkedQueue

class MGCallbackModelSpawn(
    private val drawerVertArrBox: MGDrawerVertexArray,
    private val bridgeRay: MGBridgeRayIntersect,
    private val triggerAction: MGITrigger,
    private val shaderDefault: MGShaderDefault,
    private val shaderWireframe: MGShaderSingleMode,
    private val managerTrigger: MGManagerTriggerMesh,
    private val listMeshes: ConcurrentLinkedQueue<MGDrawerMeshSwitch>,
    private val poolTextures: MGPoolTextures
): MGICallbackModel {

    override fun onGetObjects(
        objs: Array<MGObject3d>?
    ) {
        objs ?: return
        if (objs.isEmpty()) {
            return
        }

        val meshGroup = MGTriggerMeshGroup.createFromObjects(
            objs,
            drawerVertArrBox,
            shaderDefault,
            shaderWireframe,
            triggerAction,
            poolTextures
        )

        meshGroup.meshes.forEach {
            listMeshes.add(
                it.mesh
            )

            managerTrigger.addTrigger(
                it.triggerState
            )
        }

        bridgeRay.matrix = meshGroup.matrix
        meshGroup.matrix.run {
            addPosition(
                bridgeRay.outPointLead.x,
                bridgeRay.outPointLead.y,
                bridgeRay.outPointLead.z
            )
            invalidateScaleRotation()
            invalidatePosition()

            calculateInvertTrigger()
            calculateNormals()
        }
    }

}