package good.damn.common.extensions

import good.damn.common.utils.COUtilsBytesExtractor

inline fun ByteArray.extractInt(
    index: Int
) = COUtilsBytesExtractor.readInt(
    this,
    index
)