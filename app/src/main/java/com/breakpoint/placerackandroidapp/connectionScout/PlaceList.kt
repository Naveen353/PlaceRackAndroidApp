/*
 * Copyright (c) 2020. Breakpoint Software Inc.
 */

package com.breakpoint.placerackandroidapp.connectionScout

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root
data class PlaceList(
    @field:ElementList(name = "VSGPLACELIST", required = false)
    val placeName: ArrayList<PlacesModel>
)