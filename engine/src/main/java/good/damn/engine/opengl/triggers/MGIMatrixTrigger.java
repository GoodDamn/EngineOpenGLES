package good.damn.engine.opengl.triggers;

public interface MGIMatrixTrigger {
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
