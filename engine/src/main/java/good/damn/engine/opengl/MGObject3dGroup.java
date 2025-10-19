package good.damn.engine.opengl;

import android.opengl.GLES30;
import android.util.Log;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.drawers.MGDrawerMeshOpaque;
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch;
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch;
import good.damn.engine.opengl.drawers.MGDrawerVertexArray;
import good.damn.engine.opengl.entities.MGMaterial;
import good.damn.engine.opengl.shaders.MGShaderDefault;
import good.damn.engine.opengl.shaders.MGShaderSingleMode;
import good.damn.engine.opengl.textures.MGTexture;
import good.damn.engine.opengl.triggers.MGITrigger;
import good.damn.engine.opengl.triggers.MGTriggerMesh;

public final class MGObject3dGroup {

    public static MGTriggerMesh[] createFromObjects(
        @NonNull final MGObject3d[] objs,
        @NonNull final MGDrawerVertexArray drawVertBox,
        @NonNull final MGShaderDefault shaderDefault,
        @NonNull final MGShaderSingleMode shaderWireframe,
        @NonNull final MGTexture texture,
        @NonNull final MGMaterial material,
        @NonNull final MGITrigger triggerAction
    ) {
        @NonNull
        final MGTriggerMesh[] triggerMeshes = new MGTriggerMesh[
            objs.length
        ];

        @NonNull MGObject3d obj;
        for (
            int i = 0;
            i < triggerMeshes.length;
            i++
        ) {
            obj = objs[i];
            final MGArrayVertex arrayVertex = new MGArrayVertex();
            arrayVertex.configure(
                obj.vertices,
                obj.indices,
                MGArrayVertex.STRIDE
            );

            if (obj.texturesDiffuseFileName != null) {
                for (
                    @NonNull final String fileName
                    : obj.texturesDiffuseFileName
                ) {
                    Log.d("TAG", "createFromObjects: TEXTURE: " + fileName);
                }
            }

            triggerMeshes[i] = MGTriggerMesh.createFromVertexArray(
                arrayVertex,
                drawVertBox,
                shaderDefault,
                shaderWireframe,
                new MGDrawerModeSwitch(
                    arrayVertex,
                    new MGDrawerMeshOpaque(
                        arrayVertex,
                        texture,
                        material
                    ),
                    GLES30.GL_CW
                ),
                triggerAction
            );
        }

        return triggerMeshes;
    }
}
