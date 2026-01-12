package good.damn.common.matrices;

import androidx.annotation.NonNull;

public final class MGMatrixTransformationNormal<
  T extends MGMatrixTranslate
> {
    @NonNull public final T model;
    @NonNull public final MGMatrixNormal normal;
    public MGMatrixTransformationNormal(
        @NonNull T model
    ) {
        this.model = model;
        normal = new MGMatrixNormal(
            model.model
        );
    }
}
