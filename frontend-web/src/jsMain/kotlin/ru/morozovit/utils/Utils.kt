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

import kotlinx.browser.document
import org.w3c.dom.*
import kotlin.js.Date

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

object Cookies {
    private const val COOKIE_SEPARATOR = "; "

    operator fun get(key: String) = getCookie<String>(key)
    operator fun set(key: String, value: Any) {
        setCookie(key, value)
    }

    fun getAllCookies(): Map<String, String> {
        return document.cookie.split(COOKIE_SEPARATOR)
            .mapNotNull {
                runCatching {
                    val list = it.split("=")
                    return@mapNotNull list[0] to list.getOrElse(1) { "" }
                }
                return@mapNotNull null
            }
            .associate { it }
    }

    inline fun <reified T> getCookie(key: String): T? {
        val raw = getAllCookies()[key]
        return when (T::class) {
            String::class -> raw as? T
            Int::class -> raw?.toIntOrNull() as? T
            Double::class -> raw?.toDoubleOrNull() as? T
            Float::class -> raw?.toFloatOrNull() as? T
            Long::class -> raw?.toLongOrNull() as? T
            Short::class -> raw?.toShortOrNull() as? T
            Byte::class -> raw?.toByteOrNull() as? T
            Boolean::class -> raw?.toBooleanStrictOrNull() as? T
            else -> null
        }
    }

    fun setCookie(key: String, value: Any, expiresInDays: Int = 365) {
        val expires = if (expiresInDays > 0) {
            val date = Date(Date().getTime() + expiresInDays * 24 * 60 * 60 * 1000)
            "expires=${date.toUTCString()}; "
        } else ""
        document.cookie = "$key=$value; $expires"
    }

    fun deleteCookie(key: String) {
        setCookie(key, "", -1)
    }

    fun clearAllCookies() {
        getAllCookies().keys.forEach { deleteCookie(it) }
    }
}