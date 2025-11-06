package good.damn.engine.runnables

import good.damn.engine.opengl.MGObject3d
import good.damn.engine.opengl.MGTriggerMeshGroup
import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitchNormals
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.managers.MGManagerTriggerMesh
import good.damn.engine.opengl.models.MGMPoolMesh
import good.damn.engine.opengl.models.MGMPoolMeshMutable
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
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
    private val listMeshes: ConcurrentLinkedQueue<MGDrawerMeshSwitchNormals>,
    private val poolTextures: MGPoolTextures,
    private val poolMeshes: MGPoolMeshesStatic
): MGICallbackModel {

    override fun onGetObjectsCached(
        poolMesh: Array<MGMPoolMesh>
    ) {
        if (poolMesh.isEmpty()) {
            return
        }

        if (poolMesh.size == 1) {
            processMesh(
                MGTriggerMesh.createFromMeshPool(
                    shaderDefault,
                    poolMesh[0],
                    triggerAction,
                    shaderWireframe
                )
            )
            return
        }

        processGroupMesh(
            MGTriggerMeshGroup.createFromPool(
                poolMesh,
                shaderDefault,
                shaderWireframe,
                triggerAction
            )
        )
    }

    override fun onGetObjects(
        fileName: String,
        objs: Array<MGObject3d>?
    ) {
        objs ?: return
        if (objs.isEmpty()) {
            return
        }

        if (objs.size == 1) {
            val outPoolMesh = MGMPoolMeshMutable()
            MGTriggerMesh.createFromObject(
                objs[0],
                shaderDefault,
                poolTextures,
                shaderWireframe,
                outPoolMesh,
                triggerAction
            ).run {
                poolMeshes[fileName] = arrayOf(
                    outPoolMesh.toImmutable()
                )
                processMesh(this)
            }
            return
        }

        val outPoolMeshes = Array(
            objs.size
        ) { MGMPoolMeshMutable() }

        MGTriggerMeshGroup.createFromObjects(
            objs,
            outPoolMeshes,
            shaderDefault,
            shaderWireframe,
            triggerAction,
            poolTextures
        ).run {
            poolMeshes[fileName] = Array(
                outPoolMeshes.size
            ) { outPoolMeshes[it].toImmutable() }

            processGroupMesh(
                this
            )
        }
    }

    private fun processMesh(
        mesh: MGTriggerMesh
    ) {
        addMesh(mesh)
        setupMatrix(
            mesh.matrix
        )
    }

    private fun processGroupMesh(
        meshGroup: MGTriggerMeshGroup
    ) {
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