package dev.deepslate.fallacy.common.command

import com.google.common.reflect.ClassPath
import dev.deepslate.fallacy.util.command.GameCommand
import dev.deepslate.fallacy.util.command.announce.AutoloadCommand
import kotlin.reflect.full.superclasses

class Loader {
    private val systemLoader = ClassLoader.getSystemClassLoader()

    private val localLoader = javaClass.classLoader

    private val packageName = "dev.deepslate"

    private fun checkPackage(info: ClassPath.ClassInfo): Boolean = info.name.startsWith(packageName)

//    private fun checkPackage(info: ClassPath.ClassInfo): Boolean {
//        if (info.name.startsWith("META-INF")) return false
//        if(info.name.startsWith("module-info")) return false
//        if(info.name.startsWith("io.netty")) return false
//        if(info.name.startsWith("org.apache")) return false
//        if(info.name.startsWith("org.spongepowered.asm")) return false
//        return true
//    }

    private fun loadClass(info: ClassPath.ClassInfo) = systemLoader.loadClass(info.name)

    private fun checkInterface(clazz: Class<*>) = clazz.isInterface

    private inline fun <reified T> checkSuperclass(clazz: Class<*>) =
        clazz.kotlin.superclasses.any { it.qualifiedName == T::class.java.name }

    private inline fun <reified T : Annotation> checkAnnounce(clazz: Class<*>) =
        clazz.annotations.any { it.annotationClass.qualifiedName == T::class.java.name }

    private fun createInstance(clazz: Class<*>) =
        localLoader.loadClass(clazz.name).getConstructor().newInstance() as GameCommand

    fun loadAllCommands() {
        val classes = ClassPath.from(systemLoader).topLevelClasses
        classes.filter(::checkPackage).forEach {
            val clazz = loadClass(it)

            if (checkInterface(clazz)) return@forEach
            if (!checkAnnounce<AutoloadCommand>(clazz)) return@forEach
            if (!checkSuperclass<GameCommand>(clazz)) return@forEach

            val instance = createInstance(clazz)

            FallacyCommands.add(instance)
        }
    }
}