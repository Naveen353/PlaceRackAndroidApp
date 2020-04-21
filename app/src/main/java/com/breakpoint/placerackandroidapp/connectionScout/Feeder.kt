/*
 * Copyright (c) 2020. Breakpoint Software Inc.
 */

package com.breakpoint.placerackandroidapp.connectionScout

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class Feeder(
    @field:Element(name = "title")
    @field:Path("channel")
    val channnelTitle: String,

    @field:ElementList(name = "item", inline = true)
    @field:Path("channel")
    val articleList:List<Article>
)