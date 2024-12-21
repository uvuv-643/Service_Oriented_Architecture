package ru.uvuv643.ejb.remote.dto.human.response;

import ru.uvuv643.ejb.remote.dto.human.HumanBeingDto;

import  jakarta.xml.bind.annotation.XmlAccessType;
import  jakarta.xml.bind.annotation.XmlAccessorType;
import  jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class AllResponseDto implements Serializable {

        @XmlElementWrapper(name = "teamList")
        private List<HumanBeingDto> humanBeingDto;

        public AllResponseDto() {
                this(null);
        }

        public AllResponseDto(List<HumanBeingDto> humanBeingDto) {
                this.humanBeingDto = humanBeingDto;
        }

        public List<HumanBeingDto> getHumanBeingDto() {
                return humanBeingDto;
        }

        public void setHumanBeingDto(List<HumanBeingDto> humanBeingDto) {
                this.humanBeingDto = humanBeingDto;
        }
}