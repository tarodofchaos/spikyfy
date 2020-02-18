package org.chaos.spikyfy.spiky;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.chaos.spikyfy.spiky.dto.OutputMessageDTO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InvokeByApiHandler implements RequestStreamHandler {
	
	JSONParser parser = new JSONParser();

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

    	JSONObject event;
    	JSONObject responseJson;
    	OutputStreamWriter writer;
    	ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
    	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		try {
			event = (JSONObject) this.parser.parse(reader);
		
		JSONObject pps = (JSONObject) event.get("pathParameters");
    	
		String method = (String)pps.get("proxy");
		if (method.equals("gimmefuel")) {
			Map<String, Object> result = new HashMap();
			result.put("giving fuel", true);
			OutputMessageDTO outputMsg = new OutputMessageDTO();
			outputMsg.setSuccessful(true);
			outputMsg.setOutput(result);
			responseJson = new JSONObject();
			responseJson.put("statusCode", "200");
			responseJson.put("body", mapper.writeValueAsString(outputMsg));
			writer = new OutputStreamWriter(output, "UTF-8");
			writer.write(responseJson.toJSONString());
			writer.close();
		}
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
