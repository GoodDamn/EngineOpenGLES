package good.damn.engine

import android.util.Log
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.textures.MGTexture
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun checkSplitTextureName2() {
        val comTexName = "bbb"
        val textureName = "${comTexName}_s.png"
        val m = MGTexture.getNameAndTextureType(
            textureName
        )

        assertEquals(
            comTexName,
            m.first
        )

        assertEquals(
            m.second,
            MGEnumTextureType.DIFFUSE
        )
    }

    @Test
    fun checkSplitTextureNameType() {
        val comTexName = "bbb"
        val textureName = "${comTexName}_s.png"
        val m = MGTexture.getNameAndTextureType(
            textureName
        )

        assertEquals(
            comTexName,
            m.first
        )

        assertEquals(
            m.second,
            MGEnumTextureType.SPECULAR
        )
    }

    @Test
    fun checkSplitTextureName() {
        val comTexName = "bbb"
        val textureName = "$comTexName.png"
        val m = MGTexture.getNameAndTextureType(
            textureName
        )

        assertEquals(
            comTexName,
            m.first
        )

        assertEquals(
            m.second,
            MGEnumTextureType.DIFFUSE
        )
    }
}