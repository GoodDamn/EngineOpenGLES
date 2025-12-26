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
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.models.MGMMeshMaterial
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.shader.generators.MGMMaterialShader

class MGSky {
    lateinit var meshMaterial: MGMMeshMaterial
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
            localDirPath,
            informator.glLoaderTexture
        )

        val shader = MGMaterial.generateShaderModel(
            materialShader,
            informator
        )

        meshMaterial = MGMMeshMaterial(
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
                        MGMatrixScale().apply {
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