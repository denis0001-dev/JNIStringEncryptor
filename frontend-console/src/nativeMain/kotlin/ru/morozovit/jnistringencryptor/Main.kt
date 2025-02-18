package ru.morozovit.jnistringencryptor

import kotlin.random.Random
import kotlin.random.nextInt

private fun validate(string: String, isPackageName: Boolean = false): Boolean {
    if (isPackageName) {
        val strs = string.split('.')
        return !strs.any {
            !validate(it)
        }
    }
    if (string.isBlank()) return false
    if (string.startsWithNumber()) return false
    val letters = "qwertyuiopasdfghjklzxcvbnm"
    val capitalizedLetters = letters.uppercase()
    val numbers = "0123456789"
    val possibleChars = "$letters$capitalizedLetters${numbers}".toCharArray()
    val chars = string.removeIfContains(*possibleChars)
    return chars.isEmpty()
}

fun main() {
    // Collect information
    print("What string do you want to encrypt? ")
    val str = readln()
    print("What package name do you want (just press Enter to auto-generate)? ")
    val packageName = run {
        var name: String
        while (true) {
            name = readlnOrNull().let {
                if (it.isNullOrBlank()) {
                    val name1 = mutableListOf<String>()
                    repeat(Random.nextInt(1..3)) {
                        name1 += generateString()
                    }
                    name1.joinToString(separator = ".")
                } else {
                    it
                }
            }
            if (!validate(name, true))
                println("Invalid package name $name, try again")
            else break
        }
        name
    }
    print("What class name do you want (just press Enter to auto-generate)? ")
    val className = run {
        var name: String
        while (true) {
            name = readlnOrNull().let {
                if (it.isNullOrBlank()) {
                    generateString()
                } else {
                    it
                }
            }
            if (!validate(name))
                println("Invalid class name $name, try again")
            else break
        }
        name
    }
    print("What method name do you want (just press Enter to auto-generate)? ")
    val methodName = run {
        var name: String
        while (true) {
            name = readlnOrNull().let {
                if (it.isNullOrBlank()) {
                    generateString()
                } else {
                    it
                }
            }
            if (!validate(name))
                println("Invalid method name $name, try again")
            else break
        }
        name
    }
    print("Your native library name? ")
    val nativeLibName = readln()
    println()
    println("Your package name: $packageName")
    println("Your class name: $className")
    println("Your method name: $methodName")
    println("All info collected, encrypting...\n")

    val (finalCode, javaCode, call) = encrypt(str, packageName, className, methodName, nativeLibName)

    // Put the C++ code all together and print it
    println("C++ (JNI) code:")
    println(finalCode)
    // Print Java part
    println()
    println("Java code:")
    println(javaCode)
    // Print a call example
    println()
    println("Call like this:")
    println(call)
}