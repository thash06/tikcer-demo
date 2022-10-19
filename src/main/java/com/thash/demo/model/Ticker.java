package com.thash.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Ticker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //todo convert to ENUM
    private int quarter;
    private String stock;
    @JsonFormat(pattern = "M/d/yyyy")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;
    private String open;
    private String high;
    private String low;
    private String close;

    private BigDecimal volume;
    private BigDecimal percentChangePrice;
    private BigDecimal percentChangeVolumeOverLastWeek;
    private BigDecimal previousWeeksVolume;
    private String nextWeeksOpen;
    private String nextWeeksClose;

    private BigDecimal percentChangeNextWeeksPrice;
    private BigDecimal daysToNextDividend;
    private BigDecimal percentReturnNextDividend;
}
