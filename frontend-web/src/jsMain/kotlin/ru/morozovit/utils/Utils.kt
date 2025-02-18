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

@file:Suppress("unused")

package ru.morozovit.utils

import org.w3c.dom.ItemArrayLike

operator fun <T> ItemArrayLike<T>.iterator(): Iterator<T> {
    var i = 0

    return object : Iterator<T> {
        override fun hasNext() = i < length

        override fun next(): T {
            val item = item(i)
            i++
            return item!!
        }
    }
}

inline fun <T> ItemArrayLike<T>.forEach(action: (T) -> Unit) {
    for (i in this) {
        action(i)
    }
}

inline fun <T> ItemArrayLike<T>.forEachIndexed(action: (Int, T) -> Unit) {
    for (i in 0 until this.length) {
        action(i, item(i)!!)
    }
}