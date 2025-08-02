package com.tsavitskyy.bets.parser.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Market implements Serializable {

    private Long id;
    private String name;
    private long marketTypeId;
    private boolean open;
    private boolean primary;
    private List<Runner> runners;
}
