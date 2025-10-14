package good.damn.engine.runnables

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.MGObject3d
import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.drawers.MGDrawerMeshOpaque
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.triggers.MGDrawerTriggerStateable
import good.damn.engine.opengl.triggers.MGITrigger
import good.damn.engine.opengl.triggers.MGTriggerMesh
import java.util.concurrent.ConcurrentLinkedQueue

class MGCallbackModelSpawn(
    private val vertexArrayBox: MGArrayVertex,
    private val bridgeRay: MGBridgeRayIntersect,
    private val texture: MGTexture,
    private val material: MGMaterial,
    private val triggerAction: MGITrigger,
    private val shaderDefault: MGShaderDefault,
    private val shaderWireframe: MGShaderSingleMode,
    private val listTriggers: ConcurrentLinkedQueue<MGDrawerTriggerStateable>,
    private val listMeshes: ConcurrentLinkedQueue<MGDrawerMeshSwitch>
): MGICallbackModel {

    override fun onGetObjects(
        objs: Array<MGObject3d>?
    ) {
        objs ?: return
        if (objs.isEmpty()) {
            return
        }

        val vertexArray = MGArrayVertex()
        val obj = objs[0]
        vertexArray.configure(
            obj.vertices,
            obj.indices
        )

        val triggerMesh = MGTriggerMesh.createFromVertexArray(
            vertexArray,
            vertexArrayBox,
            shaderDefault,
            shaderWireframe,
            MGDrawerModeSwitch(
                vertexArray,
                MGDrawerMeshOpaque(
                    vertexArray,
                    texture,
                    material
                )
            ),
            triggerAction
        )

        listMeshes.add(
            triggerMesh.mesh
        )

        listTriggers.add(
            triggerMesh.triggerState
        )

        bridgeRay.matrix = triggerMesh.matrix
        triggerMesh.matrix.run {
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