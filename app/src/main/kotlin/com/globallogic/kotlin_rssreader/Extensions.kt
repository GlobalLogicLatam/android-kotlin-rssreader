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

package com.globallogic.kotlin_rssreader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.globallogic.kotlin_rssreader.entity.RssEntity
import com.globallogic.kotlin_rssreader.model.Channel
import com.globallogic.kotlin_rssreader.model.Item
import com.globallogic.kotlin_rssreader.model.Rss
import org.simpleframework.xml.core.Persister
import java.text.SimpleDateFormat
import java.util.*


/**
 * Function to inflate the layout in a simple way
 */
fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

/**
 * Function to format a specific date
 */
fun Item.getPubDate(): String {
    val sdf = SimpleDateFormat("ccc, dd LLLL yyyy HH:mm:ss Z", Locale.getDefault())
    val javaDate = sdf.parse(pubDate)
    return sdf.format(javaDate)
}

/**
 * Function to map a RssEntity into Rss
 */
fun RssEntity.mapToRss(): Rss {
    // use 'map' instead of a for to fill the new list
    val items = this.channel.items.map { Item(it.title, it.link, it.date, it.description) }
    val channel = Channel(items)
    return Rss(channel)
}

/**
 * Function to parse a xml into a Rss object
 */
fun Rss.Companion.parseFromXml(xml: String): Rss {
    val serializer = Persister()
    val rssEntity = serializer.read(RssEntity::class.java, xml)
    return rssEntity.mapToRss()
}
