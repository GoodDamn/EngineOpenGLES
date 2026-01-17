package good.damn.engine.opengl.entities

import android.opengl.GLES30
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialMutable
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.common.matrices.COMatrixScale
import good.damn.engine.opengl.models.MGMMeshDrawer
import good.damn.engine.MGObject3d
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.shader.generators.MGMMaterialShader

class MGSky {
    lateinit var meshMaterial: MGMMeshDrawer<
        MGShaderGeometryPassModel,
        MGDrawerMeshMaterialMutable
    >
        private set

    fun configure(
        informator: MGMInformator
    ) {
        val verticesSky = MGArrayVertexConfigurator(
            MGEnumArrayVertexConfiguration.SHORT
        )

        MGObject3d.createFromAssets(
            "objs/semi_sphere.obj"
        )?.get(0)?.run {
            verticesSky.configure(
                vertices,
                indices,
                MGPointerAttribute.defaultNoTangent
            )
        }

        val localDirPath = "textures/sky"
        val materialShader = MGMMaterialShader.Builder(
            "sky",
            localDirPath,
            informator.shaders.source
        ).diffuse()
            .opacity()
            .emissive(1.0f)
            .normal()
            .useDepthConstant()
            .specular()
            .build()

        materialShader.materialTexture.load(
            informator.poolTextures,
            localDirPath
        )

        val shader = informator.shaders.cacheGeometryPass.loadOrGetFromCache(
            materialShader.srcCodeMaterial,
            informator.shaders.source.vert,
            MGBinderAttribute.Builder()
                .bindPosition()
                .bindTextureCoordinates()
                .build(),
            arrayOf(
                MGShaderMaterial(
                    materialShader.shaderTextures
                )
            )
        )

        meshMaterial = MGMMeshDrawer(
            shader,
            MGDrawerMeshMaterialMutable(
                arrayOf(
                    MGMaterial(
                        materialShader.materialTexture
                    )
                ),
                MGDrawerMeshSwitch(
                    MGDrawerVertexArray(
                        verticesSky
                    ),
                    MGDrawerPositionEntity(
                        COMatrixScale().apply {
                            setScale(
                                2000000f,
                                2000000f,
                                2000000f
                            )
                            invalidateScale()
                        }
                    ),
                    GLES30.GL_CW
                )
            )
        )
    }
}