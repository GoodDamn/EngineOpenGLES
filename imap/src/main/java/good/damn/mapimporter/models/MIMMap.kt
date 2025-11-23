package good.damn.mapimporter.models

data class MIMMap(
    val atlases: List<MIMAtlas>,
    val batches: List<MIMBatch>,
    val collisionGeometry: MIMCollisionGeometry,
    val collisionGeometryOutsideGameZone: MIMCollisionGeometry,
    val materials: List<MIMMaterial>,
    val spawnPoints: List<MIMSpawnPoint>,
    val props: List<MIMProp>
)