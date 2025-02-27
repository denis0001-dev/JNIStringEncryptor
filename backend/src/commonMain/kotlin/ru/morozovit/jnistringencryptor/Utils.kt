/*
 * JNIStringEncryptor
 * Copyright (C) 2025 denis0001-dev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ru.morozovit.jnistringencryptor

import kotlin.random.Random

fun String.splitToChunks(minChunkSize: Int, maxChunkSize: Int): List<String> {
    val chunks = mutableListOf<String>()
    var remainingString = this

    while (remainingString.isNotBlank()) {
        val chunkSize = Random.nextInt(maxChunkSize - minChunkSize + 1) + minChunkSize
        if (chunkSize > remainingString.length) {
            chunks.add(remainingString)
            break
        }
        val (chunk, rest) = remainingString.splitAt(chunkSize)
        chunks.add(chunk)
        remainingString = rest
    }

    return chunks
}

@Suppress("NOTHING_TO_INLINE", "KotlinRedundantDiagnosticSuppress")
private inline fun String.splitAt(index: Int): Pair<String, String> {
    return this.take(index) to this.drop(index)
}

fun String.startsWithNumber() =
    startsWith("0") || startsWith("1") || startsWith("2") ||
            startsWith("3") || startsWith("4") || startsWith("5") ||
            startsWith("6") || startsWith("7") || startsWith("8") ||
            startsWith("9")

fun String.removeIfContains(vararg elements: Char): Set<Char> {
    val chars = this.toList().toMutableSet()
    elements.forEach {
        if (this.contains(it)) {
            chars.remove(it)
        }
    }
    return chars
}