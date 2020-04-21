/*
 * Copyright (c) 2020. Breakpoint Software Inc.
 */

package com.breakpoint.placerackandroidapp.network

import com.tickaroo.tikxml.annotation.*

@Xml(name="rss")
data class Feed (
    //@Attribute val id : Int,
    @PropertyElement
    @Path("channel")
    val title : String

//    @Path("item")
//    @Element
//    val articleList : List<Article>
)