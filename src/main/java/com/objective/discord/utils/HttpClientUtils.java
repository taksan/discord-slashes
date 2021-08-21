package com.objective.discord.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpClientUtils {
	public static String toUrlParameters(String... params) {
	    if (params.length % 2 != 0) 
	        throw new IllegalArgumentException("The list must have an even number of elements");
	    
	    final Map<String, String> paramMap = new HashMap<>();
	
	    for(int i = 0; i < params.length; i += 2) 
	        paramMap.put(params[i], params[i + 1]);
	
	    return HttpClientUtils.toUrlParameters(paramMap);
	}

	public static String toUrlParameters(Map<String, String> params) {
	    return params.entrySet().stream().map((entry) -> {
	        String key = entry.getKey();
	        return key + "=" + HttpClientUtils.urlEncode(entry.getValue());
	    }).collect(Collectors.joining("&"));
	}

	public static String urlEncode(String str) {
		return URLEncoder.encode(str, StandardCharsets.UTF_8);
	}

}
