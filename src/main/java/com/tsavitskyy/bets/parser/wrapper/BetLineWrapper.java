package com.tsavitskyy.bets.parser.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tsavitskyy.bets.parser.model.BetLine;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BetLineWrapper {

    private List<BetLine> data;
    private int totalCount;
}
