package dev.deepslate.fallacy.util.data.cow

import java.util.concurrent.locks.ReentrantLock

class CopyOnWrite<T>(base: T, value: T) {
    private val lock = ReentrantLock()

    var value: T = value
        set(newValue) {
            lock.lock()
            try {
                field = newValue
            } finally {
                lock.unlock()
            }
        }
}