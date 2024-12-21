package ru.uvuv643.ejb.remote.dto.human;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serializable;
import java.util.List;

@JacksonXmlRootElement(localName = "ListHumanBeingDto")
public class ListHumanBeingDto implements Serializable {

        @JacksonXmlProperty(namespace = "objects")
        private List<HumanBeingDto> humanBeing;

        public ListHumanBeingDto() {
                this(null);
        }

        public ListHumanBeingDto(List<HumanBeingDto> humanBeing) {
                this.humanBeing = humanBeing;
        }

        public List<HumanBeingDto> getHumanBeing() {
                return humanBeing;
        }

        public void setHumanBeing(List<HumanBeingDto> humanBeing) {
                this.humanBeing = humanBeing;
        }
}