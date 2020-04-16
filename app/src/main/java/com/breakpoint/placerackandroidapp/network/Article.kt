/*
 * Copyright (c) 2020. Breakpoint Software Inc.
 */

package com.breakpoint.placerackandroidapp.network

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name="item")
data class Article (
    //@Attribute val id : Int,
    @PropertyElement val title : String,
    @PropertyElement val link : String,
    @PropertyElement val description : String
)