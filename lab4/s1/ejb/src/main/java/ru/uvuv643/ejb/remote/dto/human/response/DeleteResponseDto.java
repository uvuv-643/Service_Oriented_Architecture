package ru.uvuv643.ejb.remote.dto.human.response;

import ru.uvuv643.ejb.remote.dto.human.HumanBeingDto;

import  jakarta.xml.bind.annotation.XmlAccessType;
import  jakarta.xml.bind.annotation.XmlAccessorType;
import  jakarta.xml.bind.annotation.XmlElementWrapper;
import  jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "DeleteResponseDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteResponseDto implements Serializable {

        @XmlElementWrapper(name = "teamList")
        private List<HumanBeingDto> teamList;

        public DeleteResponseDto() {
                this(null);
        }

        public DeleteResponseDto(List<HumanBeingDto> teamList) {
                this.teamList = teamList;
        }

        public List<HumanBeingDto> getTeamList() {
                return teamList;
        }

        public void setTeamList(List<HumanBeingDto> teamList) {
                this.teamList = teamList;
        }
}