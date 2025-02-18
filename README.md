# Что это?
Эта программа шифрует строку в Java через JNI и усложнение кода.

# Как она работает?
## Алгоритм
Все идентификаторы, которые вы видите, случайны и гарантированы на правильность и уникальность.

Основной код находится в файле [Encryptor.kt](backend/src/commonMain/kotlin/ru/morozovit/jnistringencryptor/Encryptor.kt#L104).

1. Программа разбивает введённую строку на "рандомные" куски;
2. Генерирует переменные для каждого куска:
   
   ```cpp
   std::string eolirgjeer = "gewbng";
   ```
3. Генерирует бесполезную переменную, чтобы ещё больше запутать мошенника:
   
   ```cpp
   std::string ZZFwFdPUHPN = CpFu9xVBzCGZQic + CR39J5cAUnAMlPuH + ejfIhwcv + KJXp2195zZ9 + Kd1W7NRHXCliF + Bsnx + 
       xkVwFCMzMB50d5 + laCM + vAluzkp2ORrGxw0 + D9G6os1r + oKywzJ4cn3fs8 + NDcI2kKa + bLycxtO2O + Y6JkIllIue5XEK + 
       Ax5lfrZDL + hp3NanZN;
   ```
4. Дальше генерирует реальный код для восстановления исходной строки:
    
    ```cpp
    std::vector<std::string> Jh = {
        Ax5lfrZDL, Y6JkIllIue5XEK, NDcI2kKa, bLycxtO2O, KJXp2195zZ9, D9G6os1r, CpFu9xVBzCGZQic, vAluzkp2ORrGxw0, Kd1W7NRHXCliF, ejfIhwcv,
        oKywzJ4cn3fs8, CR39J5cAUnAMlPuH, hp3NanZN, xkVwFCMzMB50d5, Bsnx, laCM
    };
    std::reverse(Jh.begin(), Jh.end());
    std::string IoJQA;
    for (const auto & i : IoJQA) {
        IoJQA += i;
    }
    std::wstring DZoiA(IoJQA.begin(), IoJQA.end());
    return env->NewString(
        (const jchar*)DZoiA.c_str(),
        (jsize)DZoiA.length()
    );
    ```
5. Для Вашего удобства генерирует код на Java и пример вызова. Код на Java документирован, чтобы вам было 
   проще разобраться в коде.

## Инструкция
Введите необходимые данные:
```
What string do you want to encrypt? github_pat_gVKEWGFUWKGHEURSKDBVEKRGHBIWKEGHBVIURWEHG
What package name do you want (just press Enter to auto-generate)? 
What class name do you want (just press Enter to auto-generate)? 
What method name do you want (just press Enter to auto-generate)? 
Your native library name? ultimatesecurity
```
_Токен, приведённый в примере, не является действительным._

---
На выходе Вы получаете следующее:

1. код на C++, он нужен для вставки в JNI:
    
    ```cpp
    extern "C" JNIEXPORT jstring JNICALL Java_XxfR63RwF02_neXXgfMyQB3Ceanx_TkRpIcf_A6iYUhDCE2o(
        JNIEnv* env,
        jclass,
        jstring,
        jclass,
        jthrowable
    ) {
        std::string NDcI2kKa = "pa";
        std::string Bsnx = "IURW";
        std::string vAluzkp2ORrGxw0 = "URSK";
        std::string laCM = "EHG";
        std::string hp3NanZN = "EG";
        std::string bLycxtO2O = "t_gV";
        std::string D9G6os1r = "FU";
        std::string KJXp2195zZ9 = "KEWG";
        std::string Y6JkIllIue5XEK = "b_";
        std::string CR39J5cAUnAMlPuH = "IWK";
        std::string xkVwFCMzMB50d5 = "HBV";
        std::string oKywzJ4cn3fs8 = "HB";
        std::string ejfIhwcv = "KRG";
        std::string CpFu9xVBzCGZQic = "WKGHE";
        std::string Ax5lfrZDL = "githu";
        std::string Kd1W7NRHXCliF = "DBVE";
        std::string ZZFwFdPUHPN = CpFu9xVBzCGZQic + CR39J5cAUnAMlPuH + ejfIhwcv + KJXp2195zZ9 + Kd1W7NRHXCliF + Bsnx + xkVwFCMzMB50d5 + laCM + vAluzkp2ORrGxw0 + D9G6os1r + oKywzJ4cn3fs8 + NDcI2kKa + bLycxtO2O + Y6JkIllIue5XEK + Ax5lfrZDL + hp3NanZN;
        std::vector<std::string> Jh = {
            Ax5lfrZDL, Y6JkIllIue5XEK, NDcI2kKa, bLycxtO2O, KJXp2195zZ9, D9G6os1r, CpFu9xVBzCGZQic, vAluzkp2ORrGxw0, Kd1W7NRHXCliF, ejfIhwcv,
            oKywzJ4cn3fs8, CR39J5cAUnAMlPuH, hp3NanZN, xkVwFCMzMB50d5, Bsnx, laCM
        };
        std::reverse(Jh.begin(), Jh.end());
        std::string IoJQA;
        for (const auto & i : IoJQA) {
            IoJQA += i;
        }
        std::wstring DZoiA(IoJQA.begin(), IoJQA.end());
        return env->NewString(
            (const jchar*)DZoiA.c_str(),
            (jsize)DZoiA.length()
        );
    }
    ```
2. Код на Java, нужен для доступа к зашифрованной строке из Java или Kotlin:
    
    ```java
    package XxfR63RwF02.neXXgfMyQB3Ceanx;

    /**
      * <p>
      * Class containing sensitive strings, all methods implemented in JNI
      * to make it harder for an attacker to find the source string.
      * </p>
      * <p>
      * They contain random parameters to complicate static analysis.
      * </p>
      */
    public class TkRpIcf {
        static {
            System.loadLibrary("ultimatesecurity");
        }
    
        /**
         * @param XyP3iWjS7NDoT fill as <code>"QNsPjp2Ru4HIBF3"</code>
         * @param VY fill as <code>String::class.java</code>
         * @param GYhZy fill as <code>Exception("Error occurred")</code>
         * @return the string <code>"github_pat_gVKEWGFUWKGHEURSKDBVEKRGHBIWKEGHBVIURWEHG"</code>
         */
        public static native String A6iYUhDCE2o(
            String XyP3iWjS7NDoT,
            Class<?> VY,
            Throwable GYhZy
        );
    }
    ```
3. Пример вызова, чтобы Вам было удобнее внедрить его в ваш проект:
    
    ```java
    import static XxfR63RwF02.neXXgfMyQB3Ceanx.TkRpIcf.A6iYUhDCE2o;

    String result = A6iYUhDCE2o(
        null,
        null,
        null
    );
    ```

# Модули
- [`backend`](backend) - сам код программы, функция для шифровки, другие полезные функции. Можно внедрить в Ваше 
  приложение как библиотеку.
- [`frontend-console`](frontend-console) - код консольной версии программы, написанный для Windows/Linux/macOS через 
  [Kotlin/Native](https://kotlinlang.org/docs/native-overview.html).
- [`frontend-web`](frontend-web) - код веб-версии, работает на любой платформе, удобнее пользоваться.

# Лицензия
```
JNIStringEncryptor
Copyright (C) 2025 denis0001-dev

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <https://www.gnu.org/licenses/>.
```
