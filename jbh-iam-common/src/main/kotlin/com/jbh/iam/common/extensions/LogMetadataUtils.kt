package com.jbh.iam.common.extensions

import org.slf4j.MDC

/**
 * Sets MDC logging metadata for the duration of a given block from a list of key-value pairs.
 *
 * Saves any conflicting key/value pairs from in the current MDC logging metadata, sets the new metadata, calls the
 * block, then restores the old metadata.
 *
 * Example:
 *
 * ```
 * withMetadata("a" to 1, "b" to 2) {
 *     log.info("This message will have 'a' and 'b' keys set in MDC metadata")
 * }
 * ```
 *
 * @param data One or more Kotlin pairs (e.g. `"key" to "value"`) to add to MDC metadata
 * @return The result of the block.
 */
fun <Ret> withMetadata(vararg data: Pair<String, Any?>, block: () -> Ret): Ret {
    val keys = data.map { (key, _) -> key }
    val priorData = keys.mapNotNull { key -> MDC.get(key)?.let { Pair(key, it) } }.toMap()
    val newKeys = keys - priorData.keys

    try {
        data.forEach { (k, v) ->
            MDC.put(k, v.toString())
            //NewRelic.addCustomParameter(k, v.toString())
        }
        return block()
    } finally {
        newKeys.forEach { k ->
            MDC.remove(k)
            //NewRelic.addCustomParameter(k, null as String?)
        }
        priorData.forEach { (k, v) ->
            MDC.put(k, v)
            //NewRelic.addCustomParameter(k, v)
        }
    }
}

/**
 * Sets MDC logging metadata for the duration of a given block.  Values will be converted to non-null Strings.
 *
 * Example:
 *
 * ```
 * val map = json.toMap(someData).filter { (key, _) ->
 *     key in setOf("some", "safe", "keys", "like", "excluding", "password")
 * }
 * withMetadata(map) {
 *     log.info("MDC data will be set here")
 * }
 * log.info("Prior MDC data will be restored")
 * ```
 *
 * @param data A map of data to add to MDC (e.g. `mapOf("a" to 1, "b" to 2)`).
 * @return The result of the block.
 * @see withMetadata
 */
fun <Ret> withMetadata(data: Map<String, Any?>, block: () -> Ret): Ret =
    withMetadata(data = *data.toList().toTypedArray(), block = block)
