package com.tsavitskyy.bets.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsavitskyy.bets.parser.client.LeonbetsClient;
import com.tsavitskyy.bets.parser.service.LeonbetsService;

import okhttp3.OkHttpClient;

public class LeonbetsParser {

    public static void main(String[] args) throws Exception {
        LeonbetsClient leonbetsClient = new LeonbetsClient(new OkHttpClient(), new ObjectMapper());
        LeonbetsService leonbetsService = new LeonbetsService(leonbetsClient);
        leonbetsService.parseTopMatchesAsync();
    }
}
