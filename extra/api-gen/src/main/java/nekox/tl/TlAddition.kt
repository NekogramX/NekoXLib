/*
 * Copyright (c) 2019 - 2020 KazamaWataru
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nekox.tl

import nekox.tl.TlAddition.*

sealed class TlAddition {
    interface WithMessage {
        val message: String?
    }

    interface Annotation {
        val annotation: String
    }

    data class Nullable(override val message: String? = null) : TlAddition(), WithMessage

    object JvmOverloads : TlAddition(), Annotation {
        override val annotation: String = "JvmOverloads"
    }

    data class Other(override val message: String? = null) : TlAddition(), WithMessage
    data class SizeRequired(val min: Int, val max: Int) : TlAddition()
    data class StartWithRequired(val list: List<String>) : TlAddition()

    object NonEmpty : TlAddition()

    object Sync : TlAddition()

    companion object {
        fun annotations() = listOf<Annotation>(JvmOverloads)
    }
}

@Suppress("FunctionName")
fun TlAddition(addition: String): TlAddition = when (addition.toLowerCase()) {
    "must be non-empty" -> NonEmpty
    "may be null",
    "optional",
    "may be empty" -> Nullable()
    "may be empty to de-register a device",
    "may be empty if not applicable",
    "pass null to stop sharing the live location" ->
        Nullable(addition)
    //"for testing only"                                                                                                     -> TestingOnly
    //"for bots only",
    //"can be used only by bots and only in private chats",
    //"only available to bots",
    //"to log in as a bot"                                                                                                   -> BotsOnly
    """should begin with "https://t.me/joinchat/", "https://telegram.me/joinchat/", or "https://telegram.dog/joinchat/"""" ->
        StartWithRequired(listOf("https://t.me/joinchat/", "https://telegram.me/joinchat/", "https://telegram.dog/joinchat/"))
    "up to 100" ->
        SizeRequired(0, 100)
    "use 65" ->
        SizeRequired(65, 65)
    "must be non-negative" ->
        SizeRequired(0, Int.MAX_VALUE)
    else -> {
        val splitted = addition.substringBefore(spaceToken).split(dashToken).mapNotNull(String::toIntOrNull)
        if (splitted.size > 1) {
            val (min, max) = splitted
            SizeRequired(min, max)
        } else {
            Other(addition)
        }
    }
}
