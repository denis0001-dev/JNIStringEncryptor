package ru.morozovit

import kotlin.random.Random
import kotlin.random.nextInt

val reservedDeclarations = mutableListOf<String>()

const val INDENT = 4
val SPACE = run {
    var str = ""
    repeat(INDENT) {
        str += " "
    }
    str
}
const val LINE_LIMIT = 128

data class Type(
    val jniName: String,
    val javaName: String,
    val create: () -> String
)

val jniTypes = mutableListOf(
    Type("jobject", "Object") {
        if (Random.nextInt(1..2) == 2) "new Object()" else "null"
    },
    Type("jclass", "Class") {
        if (Random.nextInt(1..2) == 2) "String::class.java" else "null"
    },
    Type("jstring", "String") {
        if (Random.nextInt(1..2) == 2) "\"${generateString()}\"" else "null"
    },
    Type("jobjectArray", "Object[]") {
        val length = Random.nextInt(1..16)
        "new Object[] {${List(length) { "new Object()" }.joinToString()}}"
    },
    Type("jbooleanArray", "boolean[]") {
        val length = Random.nextInt(1..16)
        val elements = List(length) { if (Math.random() < 0.5) "true" else "false" }
        "new boolean[] {${elements.joinToString()}}"
    },
    Type("jbyteArray", "byte[]") {
        val length = Random.nextInt(1..16)
        val elements = List(length) { Random.nextBytes(1)[0].toString() }
        "new byte[] {${elements.joinToString()}}"
    },
    Type("jcharArray", "char[]") {
        val length = Random.nextInt(1..16)
        val elements = List(length) { "'\\u${Random.nextInt().toChar().code}'" }
        "new char[] {${elements.joinToString()}}"
    },
    Type("jthrowable", "Throwable") {
        if (Random.nextInt(1..16) == 2) "Exception(\"Error occurred\")" else "null"
    }
)

fun generateString(): String {
    val letters = "qwertyuiopasdfghjklzxcvbnm"
    val capitalizedLetters = letters.uppercase()
    val numbers = "0123456789"

    val length = Random.nextInt(1..16)
    var name = ""
    for (i in 0 until length) {
        val possibleChars = if (i == 0) {
            "$letters${capitalizedLetters}"
        } else {
            "$letters$capitalizedLetters${numbers}"
        }
        name += possibleChars[Random.nextInt(possibleChars.length)]
    }
    if (name in reservedDeclarations) {
        return generateString()
    }
    reservedDeclarations.add(name)
    return name
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

fun validate(string: String, isPackageName: Boolean = false): Boolean {
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
    val classPath = "$packageName.$className.$methodName"
    print("Your native library name? ")
    val nativeLibName = readln()

    println("Your package name: $packageName")
    println("Your class name: $className")
    println("Your method name: $methodName")
    println("All info collected, encrypting...\n---------")


    // Divide into chunks & generate declarations
    val chunks = splitStringIntoRandomChunks(str, 2, 5)
    val declarations = mutableListOf<Pair<String, String>>()
    chunks.forEach {
        val name = generateString()
        declarations.add(name to "std::string $name = \"$it\";")
    }
    // Generate a completely useless declaration to confuse the attacker
    val uselessDeclaration = "std::string ${generateString()} = ${
        declarations
            .map { it.first }
            .shuffled()
            .joinToString(" + ")
    };"
    // Pretty-print & limit line length to 128
    val decSequence = declarations.joinToString { it.first }
    val decSequence2 = decSequence.toMutableList()
    var actualCharLimit = LINE_LIMIT
    var lastColonIndex = -1
    var newlinesAdded = 0
    for (i in decSequence.indices) {
        if (decSequence[i] == ',')
            lastColonIndex = i
        if (i > actualCharLimit) {
            decSequence2.add(lastColonIndex + 2 + newlinesAdded, '\n')
            newlinesAdded++
            actualCharLimit += LINE_LIMIT + 1
        }
    }
    val decryptedId = generateString()
    val finalDecSequence = decSequence2
        .joinToString("")
        .lines()
        .joinToString("\n") {
            "$SPACE$it"
        }
    // Generate a list that contains the chunks in reverse order
    val decryptionCode = """
        |std::vector<std::string> $decryptedId = {
        |$finalDecSequence
        |};
    """.trimMargin()
    declarations.shuffle()

    // Generate the random useless parameters
    val typesCount = Random.nextInt(1..4)
    val types = List(typesCount) { jniTypes[Random.nextInt(jniTypes.size)] }
    // Generate the signature of the JNI implementation
    val representations =
        types
            .joinToString(",\n") { it.jniName }
            .lines()
            .joinToString("\n") { "$SPACE$it" }
    val signature = """
        |extern "C" JNIEXPORT jstring JNICALL Java_${classPath.replace('.', '_')}(
        |    JNIEnv* env,
        |    jclass,
        |$representations
        |)
    """.trimMargin()


    // Put the C++ code all together and print it
    println("Final code:")
    val decryptedStringId = generateString()
    val finalJstringId = generateString()
    val innerCode = """
        |${declarations.joinToString("\n") { it.second }}
        |$uselessDeclaration
        |$decryptionCode
        |std::reverse($decryptedId.begin(), $decryptedId.end());
        |std::string $decryptedStringId;
        |for (const auto & i : $decryptedStringId) {
        |    $decryptedStringId += i;
        |}
        |std::wstring $finalJstringId($decryptedStringId.begin(), $decryptedStringId.end());
        |return env->NewString(
        |    (const jchar*)$finalJstringId.c_str(),
        |    (jsize)$finalJstringId.length()
        |);
    """
        .trimMargin()
        .lines()
        .joinToString("\n") { "$SPACE$it" }
    val finalCode = """
        |$signature {
        |$innerCode
        |}
    """.trimMargin()
    println(finalCode)
    // Print Java part
    println()
    println("Java code:")
    val map = types.associateBy { generateString() }

    val representations2 = map.entries.joinToString(",\n") {
        "$SPACE$SPACE${it.value.javaName} ${it.key}"
    }
    val representations3 = map
        .entries
        .joinToString("\n") {
            "$SPACE * @param ${it.key} fill as <code>${it.value.create()}</code>"
        }

    val javaCode = """
        |package $packageName;
        |
        |/**
        | * <p>
        | * Class containing sensitive strings, all methods implemented in JNI
        | * to make it harder for an attacker to find the source string.
        | * </p>
        | * <p>
        | * They contain random parameters to complicate static analysis.
        | * </p>
        | */
        |public class $className {
        |    static {
        |        System.loadLibrary("$nativeLibName");
        |    }
        |    
        |    /**
        |$representations3
        |     * @return the string <code>"$str"</code>
        |     */
        |    public static native String $methodName(
        |$representations2
        |    );
        |}
    """.trimMargin()
    println(javaCode)
    // Print a call example
    println()
    println("Call like this:")
    val callParams =
        types.joinToString(",\n") { "$SPACE${it.create()}" }
    val call = """
        |import static $classPath;
        |
        |String result = $methodName(
        |$callParams
        |)
    """.trimMargin()
    println(call)
}

fun splitStringIntoRandomChunks(input: String, minChunkSize: Int, maxChunkSize: Int): List<String> {
    val chunks = mutableListOf<String>()
    var remainingString = input

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

fun String.splitAt(index: Int): Pair<String, String> {
    return this.take(index) to this.drop(index)
}

// TODO frontend
// TODO move code to encryption function