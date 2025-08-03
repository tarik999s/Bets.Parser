package com.tsavitskyy.bets.parser.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsavitskyy.bets.parser.model.BetLine;
import com.tsavitskyy.bets.parser.model.Sports;
import com.tsavitskyy.bets.parser.wrapper.BetLineWrapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.client.utils.URIBuilder;

public class LeonbetsClient {

    private static final String BASE_URL = "https://leonbets.com";

    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public LeonbetsClient(OkHttpClient client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    public List<Sports> getAllSports() throws URISyntaxException {
        URI uri = new URIBuilder(BASE_URL + "/api-2/betline/sports")
            .addParameter("ctag", "en-US")
            .addParameter("flags", "urlv2")
            .build();

        Request request = new Request.Builder()
            .url(uri.toString())
            .header("Accept", "application/json")
            .build();

        try {
            Response response = client.newCall(request).execute();
            checkResponse(response);

            String responseBody = response.body().string();
            return mapper.readValue(responseBody, new TypeReference<List<Sports>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch sports data", e);
        }
    }

    //https://leonbets.com/api-2/betline/changes/all?ctag=en-US&vtag=9c2cd386-31e1-4ce9-a140-28e9b63a9300&league_id=1970324836991678&hideClosed=true&flags=reg,urlv2,mm2,rrc,nodup
    public List<BetLine> getBetLinesByLeague(String leagueId) throws URISyntaxException {
        URI uri = new URIBuilder(BASE_URL + "/api-2/betline/changes/all")
            .addParameter("ctag", "en-US")
            .addParameter("league_id", leagueId)
            .addParameter("flags", "reg,urlv2,mm2,rrc,nodup")
            .addParameter("hideClosed", "true")
            .addParameter("vtag", "9c2cd386-31e1-4ce9-a140-28e9b63a9300")
            .build();

        Request request = new Request.Builder()
            .url(uri.toString())
            .header("Accept", "application/json")
            .build();

        try {
            Response response = client.newCall(request).execute();
            checkResponse(response);

            String responseBody = response.body().string();
            BetLineWrapper betLineWrapper = mapper.readValue(responseBody, BetLineWrapper.class);
            return betLineWrapper.getData();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch bet lines data", e);
        }
    }

    private void checkResponse(Response response) throws Exception {
        if (!response.isSuccessful()) {
            System.out.println("Actual URL: " + response.request().url());
            System.out.println("Status code: " + response.code());

            String responseBody = response.body() != null ? response.body().string() : "No response body";
            System.out.println("Response: " + responseBody);
            throw new RuntimeException("Failed to fetch data");
        }
    }
}
