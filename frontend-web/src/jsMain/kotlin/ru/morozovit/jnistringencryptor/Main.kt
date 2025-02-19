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

import kotlinx.browser.document
import org.w3c.dom.*
import ru.morozovit.utils.Cookies
import ru.morozovit.utils.forEach

val packageNameField = document.getElementById("packageName") as HTMLInputElement
val classNameField = document.getElementById("className") as HTMLInputElement
val methodNameField = document.getElementById("methodName") as HTMLInputElement
val nativeLibNameField = document.getElementById("nativeLibName") as HTMLInputElement
val strToEncrypt = document.getElementById("str") as HTMLInputElement
val generateButton = document.getElementById("generate") as HTMLButtonElement

val errorOutput = document.getElementById("error") as HTMLElement
val jniCodeOutput = document.getElementById("jniCode") as HTMLElement
val javaCodeOutput = document.getElementById("javaCode") as HTMLElement
val callExampleOutput = document.getElementById("callExample") as HTMLElement

val result = document.getElementById("result") as HTMLDivElement

@Suppress("unused")
interface HLJSApi {
    @JsName("highlightAll")
    fun highlightAll()
    @JsName("highlightElement")
    fun highlightElement(element: HTMLElement)
}

external val hljs: HLJSApi

fun main() {
    generateButton.addEventListener("click", {
        // Hide everything
        result.querySelectorAll(".code").forEach {
            (it as HTMLElement).classList.add("hidden")
            val highlighted = (it.querySelector(".code code.hljs") as? HTMLElement)
            if (highlighted != null) {
                js("highlighted.dataset.highlighted = \"\"")
            }
        }

        // Show result
        result.classList.remove("hidden")

        // Validate
        val error = StringBuilder()
        var smthEmpty = false
        if (nativeLibNameField.value.isBlank()) {
            error.append("Native lib name can't be empty.\n")
            smthEmpty = true
        }
        if (strToEncrypt.value.isBlank()) {
            error.append("String to encrypt can't be empty.\n")
            smthEmpty = true
        }
        if (!smthEmpty) {
            if (packageNameField.value.isNotBlank() && !validate(packageNameField.value)) {
                error.append("Package name is invalid.\n")
            }
            if (classNameField.value.isNotBlank() && !validate(classNameField.value)) {
                error.append("Class name is invalid.\n")
            }
            if (methodNameField.value.isNotBlank() && !validate(methodNameField.value)) {
                error.append("Method name is invalid.\n")
            }
        }
        if (error.isNotEmpty()) {
            error.insert(0, "The following errors have occurred:\n")
            error.append("\nResolve the errors to proceed.")
            document.getElementById("error")!!.classList.remove("hidden")
            errorOutput.textContent = error.toString()
            return@addEventListener
        }

        try {
            val (jniCode, javaCode, callExample) = encrypt(
                strToEncrypt.value,
                packageNameField.value.ifBlank { generateString() },
                classNameField.value.ifBlank { generateString() },
                methodNameField.value.ifBlank { generateString() },
                nativeLibNameField.value
            )
            jniCodeOutput.textContent = jniCode
            javaCodeOutput.textContent = javaCode
            callExampleOutput.textContent = callExample
            Cookies["lastStringToEncrypt"] = strToEncrypt.value
            Cookies["lastPackageName"] = packageNameField.value
            Cookies["lastClassName"] = classNameField.value
            Cookies["lastMethodName"] = methodNameField.value
            Cookies["lastNativeLibName"] = nativeLibNameField.value
            hljs.highlightAll()
            result.querySelectorAll(".code").forEach {
                it as HTMLElement
                if (it.id != "error")
                    it.classList.remove("hidden")
            }
        } catch (e: Exception) {
            error.append("${e::class.simpleName}: ${e.message}\n")
            errorOutput.textContent = error.toString()
            return@addEventListener
        }
    })

    // Cookies
    Cookies["lastStringToEncrypt"]?.let { strToEncrypt.value = it }
    Cookies["lastPackageName"]?.let { packageNameField.value = it }
    Cookies["lastClassName"]?.let { classNameField.value = it }
    Cookies["lastMethodName"]?.let { methodNameField.value = it }
    Cookies["lastNativeLibName"]?.let { nativeLibNameField.value = it }
}