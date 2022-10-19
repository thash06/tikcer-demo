package com.thash.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class IndexData {
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
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
//    @OneToOne(cascade=CascadeType.PERSIST)
//    private TickerCurrency open;
//    @OneToOne(cascade=CascadeType.PERSIST)
//    private TickerCurrency high;
//    @OneToOne(cascade=CascadeType.PERSIST)
//    private TickerCurrency low;
//    @OneToOne(cascade=CascadeType.PERSIST)
//    private TickerCurrency close;

    private BigDecimal volume;
    private BigDecimal percentChangePrice;
    private BigDecimal percentChangeVolumeOverLastWeek;
    private BigDecimal previousWeeksVolume;
    private BigDecimal nextWeeksOpen;
    private BigDecimal nextWeeksClose;
//    @OneToOne(cascade=CascadeType.PERSIST)
//    private TickerCurrency nextWeeksOpen;
//    @OneToOne(cascade=CascadeType.PERSIST)
//    private TickerCurrency nextWeeksClose;
    private BigDecimal percentChangeNextWeeksPrice;
    private BigDecimal daysToNextDividend;
    private BigDecimal percentReturnNextDividend;

}
