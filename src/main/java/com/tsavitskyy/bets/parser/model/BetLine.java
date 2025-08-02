package com.tsavitskyy.bets.parser.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BetLine implements Serializable {

    private Long id;
    private String name;
    private String status;
    private long kickoff;
    private long lastUpdated;
    private BetLineLeague league;
    private List<Market> markets;
}
