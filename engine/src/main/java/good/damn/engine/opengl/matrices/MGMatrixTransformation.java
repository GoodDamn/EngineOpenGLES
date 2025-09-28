package good.damn.engine.opengl.matrices;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.shaders.MGIShader;
import good.damn.engine.opengl.shaders.MGIShaderNormal;

public final class MGMatrixTransformation<
    T extends MGMatrixTranslate
> {
    @NonNull public final MGMatrixNormal normal;
    @NonNull public final T model;

    public MGMatrixTransformation(
        @NonNull final T model,
        @NonNull final MGIShaderNormal shader
    ) {
        normal = new MGMatrixNormal(
            shader,
            model.model
        );
        this.model = model;
    }
}
