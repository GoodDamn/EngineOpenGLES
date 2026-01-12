package good.damn.common.matrices;

import androidx.annotation.NonNull;

public final class MGMatrixTransformationInvert<
  T extends MGMatrixTranslate
> {
    @NonNull public final T model;
    @NonNull public final MGMatrixInvert invert;
    public MGMatrixTransformationInvert(
        @NonNull T model
    ) {
        this.model = model;
        invert = new MGMatrixInvert(
            model.model
        );
    }
}
