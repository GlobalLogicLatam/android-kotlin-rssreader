/*
 * Copyright (C) 2017 Globallogic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.globallogic.kotlin_rssreader.entity

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
class ItemEntity(
        @field:Element(name = "title")
        @param:Element(name = "title")
        val title: String,

        @field:Element(name = "link")
        @param:Element(name = "link")
        val link: String,

        @field:Element(name = "pubDate")
        @param:Element(name = "pubDate")
        val date: String,

        @field:Element(name = "description")
        @param:Element(name = "description")
        val description: String
)