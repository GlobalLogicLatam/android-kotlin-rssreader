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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.globallogic.kotlin_rssreader.model.Item
import com.globallogic.kotlin_rssreader.model.Rss
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_item.view.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {

    // like a static variable in java
    companion object {
        val URL: String = "https://www.dropbox.com/s/bpio3vvgkt9lq5y/feed.xml?dl=1"
        val TAG: String = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getRss(URL) { rss ->
            runOnUiThread {
                val list = rss.channel.items
                recycler.layoutManager = LinearLayoutManager(this)
                recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
                recycler.adapter = RssAdapter(list) { item ->
                    // open the link in a browser
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(item.link)
                    startActivity(intent)
                }
            }
        }
    }
}

class RssAdapter(val items: List<Item>?, val listener: (Item) -> Unit) : RecyclerView.Adapter<RssAdapter.RssViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RssViewHolder {
        return RssViewHolder(parent.inflate(R.layout.view_item))
    }

    override fun onBindViewHolder(holder: RssViewHolder, position: Int) {
        with(holder.itemView) {
            val item = items!![position]
            view_title.text = item.title
            view_description.text = Html.fromHtml(item.description)
            view_date.text = item.getPubDate()
            setOnClickListener { listener(item) }
        }
    }

    override fun getItemCount() = items!!.size

    class RssViewHolder(view: View) : RecyclerView.ViewHolder(view)
}

fun getRss(url: String, listener: (Rss) -> Unit) {
    val httpClient = OkHttpClient()
    val request = Request.Builder().url(url).build()

    httpClient.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: Call?, e: IOException?) {
            Log.d(MainActivity.TAG, "Error", e)
        }

        override fun onResponse(call: Call?, response: Response?) {
            val rss = Rss.parseFromXml(response!!.body().string())
            listener(rss)
        }
    })
}