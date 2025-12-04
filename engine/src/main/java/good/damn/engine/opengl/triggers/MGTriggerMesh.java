package good.damn.engine.opengl.triggers;

import android.opengl.GLES30;
import android.util.Pair;

import androidx.annotation.NonNull;

import java.util.LinkedList;

import good.damn.engine.models.MGMInformator;
import good.damn.engine.models.MGMInformatorShader;
import good.damn.engine.opengl.arrays.MGArrayVertexManager;
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute;
import good.damn.engine.opengl.drawers.MGDrawerVertexArray;
import good.damn.engine.opengl.entities.MGMaterialTexture;
import good.damn.engine.opengl.objects.MGObject3d;
import good.damn.engine.opengl.shaders.MGShaderMaterial;
import good.damn.engine.opengl.shaders.MGShaderOpaqueSingle;
import good.damn.engine.opengl.shaders.MGShaderTexture;
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute;
import good.damn.engine.sdk.MGVector3;
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialSwitch;
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitchNormals;
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity;
import good.damn.engine.opengl.entities.MGMaterial;
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation;
import good.damn.engine.opengl.matrices.MGMatrixTransformationInvert;
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal;
import good.damn.engine.opengl.models.MGMPoolMesh;
import good.damn.engine.opengl.models.MGMPoolMeshMutable;
import good.damn.engine.opengl.thread.MGHandlerGl;
import good.damn.engine.opengl.triggers.callbacks.MGManagerTriggerStateCallback;
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodBox;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateable;
import good.damn.engine.runnables.MGRunnableConfigVertexArray;
import good.damn.engine.shader.MGShaderCache;
import good.damn.engine.shader.MGShaderSource;
import good.damn.engine.shader.generators.MGGeneratorShader;
import good.damn.engine.utils.MGUtilsAlgo;

public final class MGTriggerMesh {

    @NonNull
    public final MGMatrixTriggerMesh matrix;

    @NonNull
    public final MGDrawerMeshMaterialSwitch mesh;

    @NonNull
    public final MGDrawerTriggerStateable triggerState;

    @NonNull
    public final MGShaderOpaqueSingle shaderOpaque;

    private MGTriggerMesh(
        @NonNull final MGMatrixTriggerMesh matrix,
        @NonNull final MGDrawerMeshMaterialSwitch mesh,
        @NonNull final MGDrawerTriggerStateable triggerState,
        @NonNull final MGShaderOpaqueSingle shaderOpaque
    ) {
        this.matrix = matrix;
        this.mesh = mesh;
        this.triggerState = triggerState;
        this.shaderOpaque = shaderOpaque;
    }

    @NonNull
    public static MGTriggerMesh createFromObject(
        @NonNull final MGObject3d obj,
        @NonNull final MGMInformator informator,
        @NonNull final MGMPoolMeshMutable outPoolMesh,
        @NonNull final MGITrigger triggerAction
    ) {
        @NonNull
        final MGArrayVertexManager arrayVertex = new MGArrayVertexManager(
            obj.config
        );

        @NonNull
        final MGHandlerGl glHandler = informator
            .getGlHandler();

        @NonNull
        final MGMInformatorShader shaders = informator
            .getShaders();

        @NonNull
        final MGShaderCache<
            MGShaderOpaqueSingle
        > shaderCache = shaders.getOpaqueGenerated();

        @NonNull
        final MGShaderSource shaderSource = shaders
            .getSource();

        glHandler.post(
            new MGRunnableConfigVertexArray(
                arrayVertex,
                obj.vertices,
                obj.indices,
                MGPointerAttribute.defaultNoTangent
            )
        );

        arrayVertex.keepBufferVertices(
            obj.vertices
        );

        @NonNull
        final MGMaterialTexture.Builder builder = new MGMaterialTexture.Builder();

        @NonNull
        final MGGeneratorShader generatorShader = new MGGeneratorShader(
            shaderSource
        );

        @NonNull
        final LinkedList<
            MGShaderTexture
        > shaderTextures = new LinkedList<>();

        if (obj.texturesDiffuseFileName != null) {
            builder.textureDiffuse(
                obj.texturesDiffuseFileName[0]
            );

            shaderTextures.add(
                new MGShaderTexture(
                    "textDiffuse"
                )
            );
        }

        if (obj.texturesMetallicFileName == null) {
            generatorShader.metallicNo();
            generatorShader.specularNo();
        } else {
            shaderTextures.add(
                new MGShaderTexture(
                    "textMetallic"
                )
            );
            builder.textureMetallic(
                obj.texturesMetallicFileName[0]
            );
            generatorShader.metallicMap();
            generatorShader.specular();
        }

        generatorShader.opacityNo();
        generatorShader.normalVertex();

        if (obj.texturesEmissiveFileName == null) {
            generatorShader.emissiveNo();
        } else {
            shaderTextures.add(
                new MGShaderTexture(
                    "textEmissive"
                )
            );
            builder.textureEmissive(
                obj.texturesEmissiveFileName[0]
            );
            generatorShader.emissiveMap();
        }

        generatorShader.lighting();

        @NonNull
        final String src = generatorShader.generate();

        @NonNull
        MGShaderOpaqueSingle cachedShader = shaderCache.get(
            src
        );

        if (cachedShader == null) {
            cachedShader = new MGShaderOpaqueSingle(
                MGShaderMaterial.singleMaterial(
                    shaderTextures.toArray(new MGShaderTexture[0])
                )
            );
            shaderCache.cacheAndCompile(
                src,
                shaderSource.getVert(),
                cachedShader,
                glHandler,
                new MGBinderAttribute.Builder()
                    .bindPosition()
                    .bindTextureCoordinates()
                    .bindNormal()
                    .build()
            );
        }

        @NonNull
        final MGMaterialTexture materialTexture = builder
            .build();

        materialTexture.load(
            informator.getPoolTextures(),
            "textures",
            glHandler
        );

        @NonNull
        final MGMaterial material = new MGMaterial(
            builder.build()
        );

        return createFromVertexArray(
            arrayVertex,
            material,
            outPoolMesh,
            triggerAction,
            cachedShader
        );
    }

    @NonNull
    public static MGTriggerMesh createFromVertexArray(
        @NonNull final MGArrayVertexManager vertexArray,
        @NonNull final MGMaterial material,
        @NonNull final MGMPoolMeshMutable outPoolMesh,
        @NonNull final MGITrigger triggerAction,
        @NonNull final MGShaderOpaqueSingle shaderOpaque
    ) {
        outPoolMesh.pointMinMax = MGUtilsAlgo.findMinMaxPoints(
            vertexArray
        );

        outPoolMesh.pointMiddle = outPoolMesh.pointMinMax.first.interpolate(
            outPoolMesh.pointMinMax.second,
            0.5f
        );

        MGUtilsAlgo.offsetAnchorPoint(
            vertexArray,
            outPoolMesh.pointMiddle
        );

        vertexArray.unkeepBufferVertices();

        outPoolMesh.material = material;
        outPoolMesh.vertexArray = vertexArray;
        outPoolMesh.shaderOpaque = shaderOpaque;

        return createFromMeshPool(
            outPoolMesh.toImmutable(),
            triggerAction
        );
    }

    @NonNull
    public static MGTriggerMesh createFromMeshPool(
        @NonNull final MGMPoolMesh poolMesh,
        @NonNull final MGITrigger triggerAction
    ) {
        @NonNull final Pair<
            MGVector3, MGVector3
        > pointMinMax = poolMesh.getPointMinMax();

        @NonNull final MGVector3 pointMiddle = poolMesh
            .getPointMiddle();

        @NonNull
        final MGMatrixTriggerMesh matrix = new MGMatrixTriggerMesh(
            new MGMatrixTransformationInvert<>(
                new MGMatrixScaleRotation()
            ),
            new MGMatrixTransformationNormal<>(
                new MGMatrixScaleRotation()
            ),
            pointMinMax.first,
            pointMinMax.second
        );

        matrix.setPosition(
            pointMiddle.getX(),
            pointMiddle.getY(),
            pointMiddle.getZ()
        );

        matrix.invalidatePosition();
        matrix.invalidateScaleRotation();
        matrix.calculateInvertTrigger();
        matrix.calculateNormals();

        @NonNull
        final MGMaterial material = poolMesh.getMaterial();

        @NonNull
        final MGDrawerMeshSwitchNormals drawerMeshSwitchNormals = new MGDrawerMeshSwitchNormals(
            new MGDrawerVertexArray(
                poolMesh.getVertexArray()
            ),
            new MGDrawerPositionEntity(
                matrix.matrixMesh.model
            ),
            GLES30.GL_CCW,
            matrix.matrixMesh.normal
        );

        @NonNull
        final MGDrawerMeshMaterialSwitch meshTexture = new MGDrawerMeshMaterialSwitch(
            new MGMaterial[]{material},
            drawerMeshSwitchNormals
        );

        @NonNull
        final MGDrawerTriggerStateable triggerState = new MGDrawerTriggerStateable(
            new MGManagerTriggerStateCallback(
                new MGTriggerMethodBox(
                    matrix.matrixTrigger.invert
                ),
                triggerAction
            ),
            matrix.matrixTrigger.model
        );

        return new MGTriggerMesh(
            matrix,
            meshTexture,
            triggerState,
            poolMesh.getShader()
        );
    }
}
