package good.damn.logic.triggers;

public interface LGIMatrixTrigger {
    void setPosition(
        float x,
        float y,
        float z
    );

    void addPosition(
        float x,
        float y,
        float z
    );

    void subtractScale(
        float x,
        float y,
        float z
    );

    void addScale(
        float x,
        float y,
        float z
    );

    void setScale(
        float x,
        float y,
        float z
    );

    void addRotation(
        float x,
        float y,
        float z
    );

    void invalidatePosition();

    void invalidateScaleRotation();

    void calculateInvertTrigger();

    void calculateNormals();
}
