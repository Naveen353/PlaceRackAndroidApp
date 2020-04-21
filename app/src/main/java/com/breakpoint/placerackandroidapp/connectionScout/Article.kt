/*
 * Copyright (c) 2020. Breakpoint Software Inc.
 */

package com.breakpoint.placerackandroidapp.connectionScout

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
data class Article(
    @field:Element(name = "title")
    val title: String,

    @field:Element(name = "link")
    val link: String,

    @field:Element(name = "description")
    val description: String
)