/*
 * Copyright (c) 2020. Breakpoint Software Inc.
 */

package com.breakpoint.placerackandroidapp.connectionScout

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root
data class PlacesModel (
    @field:Element(name = "VSNAME", required = false)
    val placeName: String,

    @field:Element(name = "VSGPLACEID", required = false)
    val placeId: String
)