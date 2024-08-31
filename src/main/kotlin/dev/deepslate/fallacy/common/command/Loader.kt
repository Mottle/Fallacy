package dev.deepslate.fallacy.common.command

import com.google.common.reflect.ClassPath
import dev.deepslate.fallacy.common.command.FallacyCommands.add
import kotlin.reflect.full.superclasses

class Loader {
    private val systemLoader = ClassLoader.getSystemClassLoader()

    private val localLoader = javaClass.classLoader

    private val packageName = javaClass.`package`.name

    private fun checkPackage(info: ClassPath.ClassInfo): Boolean = info.name.startsWith(packageName)

    private fun loadClass(info: ClassPath.ClassInfo) = systemLoader.loadClass(info.name)

    private fun checkInterface(clazz: Class<*>) = clazz.isInterface

    private fun checkImplOf(clazz: Class<*>, supeClass: Class<*>) =
        clazz.kotlin.superclasses.any { it.qualifiedName == supeClass.name }

    private fun createInstance(clazz: Class<*>) =
        localLoader.loadClass(clazz.name).getConstructor().newInstance() as FallacyCommand

    fun loadAllCommands() {
        val classes = ClassPath.from(systemLoader).topLevelClasses

        classes.forEach {
            if (!checkPackage(it)) return@forEach
            val clazz = loadClass(it)
            if (!checkImplOf(clazz, FallacyCommand::class.java) || checkInterface(clazz)) return@forEach
            add(createInstance(clazz))
        }
    }
}