package ru.uvuv643.ejb.remote.dto.human.response;

import  jakarta.xml.bind.annotation.XmlAccessType;
import  jakarta.xml.bind.annotation.XmlAccessorType;
import  jakarta.xml.bind.annotation.XmlElement;
import  jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "StatisticResponseDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatisticResponseDto implements Serializable {

        @XmlElement(name = "result")
        private Double result;

        public StatisticResponseDto() {
                this(null);
        }

        public StatisticResponseDto(Double result) {
                this.result = result;
        }

        public Double getResult() {
                return result;
        }

        public void setResult(Double result) {
                this.result = result;
        }
}