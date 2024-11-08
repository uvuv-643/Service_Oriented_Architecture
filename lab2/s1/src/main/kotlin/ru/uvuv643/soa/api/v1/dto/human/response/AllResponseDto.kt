package ru.uvuv643.soa.api.v1.dto.human.response

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlElementWrapper
import jakarta.xml.bind.annotation.XmlRootElement
import ru.uvuv643.soa.api.v1.dto.human.HumanBeingDto

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class AllResponseDto constructor(

        @XmlElementWrapper(name = "teamList")
        val humanBeingDto: List<HumanBeingDto>?,

        ) {
        constructor() : this(null)
}
