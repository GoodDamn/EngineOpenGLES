package good.damn.wrapper.providers

import good.damn.wrapper.models.APMProviderGL

interface APIProviderGLRegister {
    fun registerGlProvider(
        provider: APMProviderGL
    )
}