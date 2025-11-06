package good.damn.engine.imports

interface MGIImport<T> {
    fun onImport(
        it: T
    )
}