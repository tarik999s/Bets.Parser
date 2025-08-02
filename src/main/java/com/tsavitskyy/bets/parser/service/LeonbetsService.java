package com.tsavitskyy.bets.parser.service;

import com.tsavitskyy.bets.parser.client.LeonbetsClient;
import com.tsavitskyy.bets.parser.model.BetLine;
import com.tsavitskyy.bets.parser.model.League;
import com.tsavitskyy.bets.parser.model.Market;
import com.tsavitskyy.bets.parser.model.Runner;
import com.tsavitskyy.bets.parser.model.Sports;
import com.tsavitskyy.bets.parser.utils.DateTimeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.collections4.CollectionUtils;

public class LeonbetsService {

    private static final Set<String> TARGET = Set.of("Football", "Tennis", "Hockey", "Basketball");

    private final LeonbetsClient client;
    private final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(3);

    public LeonbetsService(LeonbetsClient client) {
        this.client = client;
    }

    public void parseTopMatchesAsync() throws Exception {
        List<Sports> sports = client.getAllSports();
        List<Map.Entry<String, League>> topLeagues = sports
            .stream()
            .filter(sport -> TARGET.contains(sport.getName()))
            .flatMap(sport -> sport.getRegions()
                .stream()
                .flatMap(region -> region.getLeagues()
                    .stream()
                    .filter(league -> league.isTop() && league.getPrematch() > 0)
                    .map(league -> Map.entry(sport.getName(), league))
                )
            )
            .toList();

        List<Future<String>> futures = new ArrayList<>();
        for (Map.Entry<String, League> entry : topLeagues) {
            futures.add(EXECUTOR_SERVICE.submit(() -> processLeague(entry.getKey(), entry.getValue())));
        }

        for (Future<String> future : futures) {
            try {
                System.out.print(future.get());
            } catch (InterruptedException e) {
                System.err.println("Current Thread was interrupted");
                Thread.currentThread().interrupt(); // відновити флаг переривання
            } catch (ExecutionException e) {
                System.err.println("Error during execution: " + e.getMessage());
            }
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                System.err.println("Current Thread was interrupted");
                Thread.currentThread().interrupt(); // відновити флаг переривання
            } catch (ExecutionException e) {
                System.err.println("Error during execution: " + e.getMessage());
            }
        }

        EXECUTOR_SERVICE.shutdown();
    }

    private String processLeague(String sportName, League league) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(sportName).append(", ").append(league.getName()).append("\n");

        List<BetLine> lines = client.getBetLinesByLeague(String.valueOf(league.getId())).stream()
            .filter(match -> Objects.equals(match.getStatus(), "OPEN"))
            .toList();

        for (int i = 0; i < Math.min(2, lines.size()); i++) {
            BetLine match = lines.get(i);
            String kickoff = DateTimeUtils.formatMillis(match.getKickoff());
            sb.append("\t").append(match.getName()).append(", ").append(kickoff).append(", ").append(match.getId()).append("\n");

            if (CollectionUtils.isNotEmpty(match.getMarkets())) {
                match.getMarkets()
                    .stream()
                    .filter(Market::isOpen)
                    .forEach(market -> {
                        sb.append("\t\t").append(market.getName()).append("\n");
                        market.getRunners()
                            .stream()
                            .filter(Runner::isOpen)
                            .forEach(runner -> sb.append("\t\t\t")
                                .append(runner.getName()).append(", ")
                                .append(runner.getPrice().toPlainString()).append(", ")
                                .append(runner.getId()).append("\n"));
                    });
            } else {
                sb.append("\t\tNo markets available\n");
            }
        }

        return sb.toString();
    }
}
