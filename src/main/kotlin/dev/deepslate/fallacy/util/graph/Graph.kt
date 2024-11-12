package dev.deepslate.fallacy.util.graph

class Graph<K, V> {
    private val edges = mutableMapOf<K, MutableMap<K, V>>()

    fun addEdge(from: K, to: K, value: V) {
        edges.computeIfAbsent(from) { mutableMapOf() }
        edges[from]!![to] = value
    }

    fun getEdges(from: K): List<Pair<K, V>>? = edges[from]?.toList()

    fun getEdges(from: K, to: K): List<Pair<K, V>>? = edges[from]?.filter { it.key == to }?.toList()

    fun getEdges(from: K, to: K, value: V): List<Pair<K, V>>? =
        edges[from]?.filter { it.key == to && it.value == value }?.toList()

    fun getEdges(): Map<K, Map<K, V>> = edges

    fun getValue(from: K, to: K): V? = edges[from]?.get(to)

    fun contains(from: K, to: K): Boolean = edges[from]?.containsKey(to) == true
}