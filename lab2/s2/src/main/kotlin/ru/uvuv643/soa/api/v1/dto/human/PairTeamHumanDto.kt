package ru.uvuv643.soa.api.v1.dto.human

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class PairTeamHumanDto (

        @XmlElement(name = "humanBeingId")
        val humanBeingId : Int,

        @XmlElement(name = "teamId")
        val teamId : Int,

) {
        constructor() : this(0, 0)
}