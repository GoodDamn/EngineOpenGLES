package good.damn.common.matrices;

import androidx.annotation.NonNull;

public final class COMatrixTransformationNormal<
  T extends COMatrixTranslate
> {
    @NonNull public final T model;
    @NonNull public final COMatrixNormal normal;
    public COMatrixTransformationNormal(
        @NonNull T model
    ) {
        this.model = model;
        normal = new COMatrixNormal(
            model.model
        );
    }
}
