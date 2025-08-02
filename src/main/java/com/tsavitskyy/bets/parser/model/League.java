package com.tsavitskyy.bets.parser.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class League implements Serializable {

    private Long id;
    private String name;
    private int prematch;
    private int inplay;
    private boolean top;
    private String url;
}
