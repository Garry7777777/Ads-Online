package com.skypro.adsonline.dto.responses;

import lombok.Data;
import java.util.Collection;
import java.util.List;

@Data
public class Response<T> {

    private int count;
    private Collection<T> results;

    public static <T> Response<T> Wrapper(List<T> results) {
        Response<T> response = new Response<>();
        if (results == null) return response;
        response.results = results;
        response.count = results.size();
        return response;
    }
}