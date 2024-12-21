package ru.uvuv643.soa.api.v1.dto.human.response

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElementWrapper
import jakarta.xml.bind.annotation.XmlRootElement
import ru.uvuv643.soa.api.v1.dto.human.PairTeamHumanDto

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class ListHumanBeingDto(

        @XmlElementWrapper(name = "pairs")
        val humanBeingPair : List<PairTeamHumanDto>,

        ) {
        constructor() : this(listOf())
}