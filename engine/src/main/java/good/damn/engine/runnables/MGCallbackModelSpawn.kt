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
import good.damn.engine.opengl.triggers.MGIMatrixTrigger
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

        if (objs.size == 1) {
            val mesh = MGTriggerMesh.createFromObject(
                objs[0],
                shaderDefault,
                poolTextures,
                drawerVertArrBox,
                shaderWireframe,
                triggerAction
            )
            addMesh(mesh)
            setupMatrix(
                mesh.matrix
            )
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
            addMesh(it)
        }

        setupMatrix(
            meshGroup.matrix
        )
    }

    private inline fun setupMatrix(
        matrix: MGIMatrixTrigger
    ) {
        bridgeRay.matrix = matrix
        matrix.run {
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

    private inline fun addMesh(
        mesh: MGTriggerMesh
    ) {
        listMeshes.add(
            mesh.mesh
        )

        managerTrigger.addTrigger(
            mesh.triggerState
        )
    }
}