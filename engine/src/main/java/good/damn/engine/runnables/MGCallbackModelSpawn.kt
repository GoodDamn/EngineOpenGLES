package good.damn.engine.runnables

import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.MGTriggerMeshGroup
import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialMutable
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.models.MGMPoolMeshMutable
import good.damn.engine.opengl.models.MGMPoolVertexArray
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.opengl.triggers.MGIMatrixTrigger
import good.damn.engine.opengl.triggers.MGITrigger
import good.damn.engine.opengl.triggers.MGTriggerMesh

class MGCallbackModelSpawn(
    private val bridgeRay: MGBridgeRayIntersect,
    private val triggerAction: MGITrigger,
    private val informator: MGMInformator
): MGICallbackModel {

    override fun onGetObjectsCached(
        poolMesh: Array<MGMPoolVertexArray>
    ) {
        if (poolMesh.isEmpty()) {
            return
        }

        if (poolMesh.size == 1) {
            processMesh(
                getCachedMaterial(
                    null
                ),
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
                processMesh(
                    this
                )
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
        material: MGMaterial,
        mesh: MGTriggerMesh
    ) {
        addMesh(
            arrayOf(material),
            mesh
        )
        setupMatrix(
            mesh.matrix
        )
    }

    private inline fun getCachedMaterial(
        fileNameDiffuse: String?
    ): MGMaterial? {
        val poolMaterials = informator
            .poolMaterials

        if (fileNameDiffuse == null) {
            return null
        }

        val cachedMaterial = poolMaterials[
            fileNameDiffuse
        ]

        if (cachedMaterial != null) {
            return cachedMaterial
        }

        return MGMaterial.generateShaderAndMaterial(
            fileNameDiffuse,
            informator
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
        materials: Array<MGMaterial>,
        mesh: MGTriggerMesh
    ) {
        informator.meshes.add(
            MGDrawerMeshMaterialMutable(
                materials,
                mesh.mesh
            )
        )

        informator.managerTrigger.addTrigger(
            mesh.triggerState
        )
    }
}