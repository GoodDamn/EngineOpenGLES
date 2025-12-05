package good.damn.engine.runnables

import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.opengl.MGTriggerMeshGroup
import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialSwitch
import good.damn.engine.opengl.models.MGMPoolMesh
import good.damn.engine.opengl.models.MGMPoolMeshMutable
import good.damn.engine.opengl.triggers.MGIMatrixTrigger
import good.damn.engine.opengl.triggers.MGITrigger
import good.damn.engine.opengl.triggers.MGTriggerMesh
import java.util.concurrent.ConcurrentLinkedQueue

class MGCallbackModelSpawn(
    private val bridgeRay: MGBridgeRayIntersect,
    private val triggerAction: MGITrigger,
    private val informator: MGMInformator
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
                    poolMesh[0],
                    triggerAction
                )
            )
            return
        }

        processGroupMesh(
            MGTriggerMeshGroup.createFromPool(
                poolMesh,
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
                informator,
                outPoolMesh,
                triggerAction,
            ).run {
                informator.poolMeshes[fileName] = arrayOf(
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
            triggerAction,
            informator
        ).run {
            informator.poolMeshes[fileName] = Array(
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
        informator.meshes[
            mesh.shaderOpaque
        ]?.run {
            addMeshToShader(
                this,
                mesh
            )
            return
        }

        val queue = ConcurrentLinkedQueue<
            MGDrawerMeshMaterialSwitch
        >()

        informator.meshes[
            mesh.shaderOpaque
        ] = queue

        addMeshToShader(
            queue,
            mesh
        )
    }

    private inline fun addMeshToShader(
        queue: ConcurrentLinkedQueue<MGDrawerMeshMaterialSwitch>,
        mesh: MGTriggerMesh
    ) {
        queue.add(
            mesh.mesh
        )
        informator.managerTrigger.addTrigger(
            mesh.triggerState
        )
    }
}