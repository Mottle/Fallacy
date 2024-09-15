package dev.deepslate.fallacy.util

import com.google.common.reflect.ClassPath
import dev.deepslate.fallacy.util.announce.Autoload
import kotlin.reflect.full.superclasses

class Loader(private val packageName: String = "dev.deepslate") {
    private val systemLoader = ClassLoader.getSystemClassLoader()

    private val localLoader = javaClass.classLoader

    private fun checkPackage(info: ClassPath.ClassInfo): Boolean = info.name.startsWith(packageName)

    private fun loadClass(info: ClassPath.ClassInfo) = systemLoader.loadClass(info.name)

    private fun checkInterface(clazz: Class<*>) = clazz.isInterface

    private inline fun <reified T> checkSuperclass(clazz: Class<*>) =
        clazz.kotlin.superclasses.any { it.qualifiedName == T::class.java.name }

    private inline fun <reified T : Annotation> checkAnnounce(clazz: Class<*>) =
        clazz.annotations.any { it.annotationClass.qualifiedName == T::class.java.name }

    private inline fun <reified T> createInstance(clazz: Class<*>) =
        localLoader.loadClass(clazz.name).getConstructor().newInstance() as T

    internal inline fun <reified T> load(then: (T) -> Unit) {
        val classes = ClassPath.from(systemLoader).topLevelClasses
        classes.filter(::checkPackage).forEach {
            val clazz = loadClass(it)

            if (checkInterface(clazz)) return@forEach
            if (!checkAnnounce<Autoload>(clazz)) return@forEach
            if (!checkSuperclass<T>(clazz)) return@forEach

            val instance = createInstance<T>(clazz)

            then(instance)
        }
    }
}