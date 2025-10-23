package good.damn.engine.runnables

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.MGObject3d
import good.damn.engine.opengl.MGObject3dGroup
import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.drawers.MGDrawerMeshOpaque
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.managers.MGManagerTrigger
import good.damn.engine.opengl.managers.MGManagerTriggerMesh
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.triggers.MGITrigger
import good.damn.engine.opengl.triggers.MGTriggerMesh
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

        val triggerMeshes = MGObject3dGroup.createFromObjects(
            objs,
            drawerVertArrBox,
            shaderDefault,
            shaderWireframe,
            triggerAction,
            poolTextures
        )

        triggerMeshes.forEach {
            listMeshes.add(
                it.mesh
            )

            managerTrigger.addTrigger(
                it.triggerState
            )
        }

        bridgeRay.matrix = triggerMeshes[0].matrix
        triggerMeshes[0].matrix.run {
            setPosition(
                bridgeRay.outPointLead.x,
                bridgeRay.outPointLead.y,
                bridgeRay.outPointLead.z
            )
            invalidateScaleRotation()
            invalidatePosition()

            calculateInvertTrigger()
            calculateNormalsMesh()
        }
    }

}