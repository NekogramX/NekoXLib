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

sealed class TlData(
        open val type: String,
        open val parentType: String,
        open val metadata: TlMetadata
)

data class TlAbstract(
        override val type: String,
        override val metadata: TlMetadata
) : TlData(type, "Object", metadata)

sealed class TlClass(
        type: String,
        parentType: String,
        metadata: TlMetadata,
        open val crc: Int
) : TlData(type, parentType, metadata)

data class TlObject(
        override val type: String,
        override val parentType: String,
        override val metadata: TlMetadata,
        override val crc: Int
) : TlClass(type, parentType, metadata, crc)

data class TlFunction(
        override val type: String,
        val returnType: String,
        override val metadata: TlMetadata,
        override val crc: Int
) : TlClass(type, "Function", metadata, crc)
