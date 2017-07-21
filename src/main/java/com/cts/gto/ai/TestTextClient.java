package com.cts.gto.ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.Proxy.Type;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceContext;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

public class TestTextClient {
	private static final String INPUT_PROMPT = "> ";
	 
	  private static final int ERROR_EXIT_CODE = 1;

	 
	  public static void main(String[] args) {
		  System.setProperty("http.proxyHost", "proxy.cognizant.com");
		  System.setProperty("http.proxyPort", "6050");
		  System.setProperty("https.proxyHost", "proxy.cognizant.com");
		  System.setProperty("https.proxyPort", "6050");
	    if (args.length < 1) {
	      showHelp("Please specify API key", ERROR_EXIT_CODE);
	    }

	    AIConfiguration configuration = new AIConfiguration(args[0]);
	    SocketAddress addr = new InetSocketAddress("10.227.151.50", 6050);
	    Proxy proxy = new Proxy(Type.HTTP, addr);
	    configuration.setProxy(proxy);

	    AIDataService dataService = new AIDataService(configuration);

	    String line;
	    System.out.println("Hi I am Alice. How may i help u?");
	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
	      System.out.print(INPUT_PROMPT);
	      while (null != (line = reader.readLine())) {

	        try {
	          AIRequest request = new AIRequest(line);
//	          AIContext aiContext = new AIContext();
//	          aiContext.s
//	          request.addContext(aiContext);
//	          AIServiceContext context = dataService.getContext();
//	          context.getSessionId()
	          AIResponse response = dataService.request(request);

	          if (response.getStatus().getCode() == 200) {
	        	  if(response.getResult().getFulfillment().getDisplayText()!= null){
	        		  System.out.println(response.getResult().getFulfillment().getDisplayText());
	        	  }
	        	  else {
	        		  System.out.println(response.getResult().getFulfillment().getSpeech());
				}
	            
	           
	          } else {
	            System.err.println(response.getStatus().getErrorDetails());
	          }
	        } catch (Exception ex) {
	          ex.printStackTrace();
	        }

	        System.out.print(INPUT_PROMPT);
	      }
	    } catch (IOException ex) {
	      ex.printStackTrace();
	    }
	    System.out.println("Bye bye!");
	  }


	
	  private static void showHelp(String errorMessage, int exitCode) {
	    if (errorMessage != null && errorMessage.length() > 0) {
	      System.err.println(errorMessage);
	      System.err.println();
	    }

	    System.out.println("Usage: APIKEY");
	    System.out.println();
	    System.out.println("APIKEY  Your unique application key");
	    System.out.println("        See https://docs.api.ai/docs/key-concepts for details");
	    System.out.println();
	    System.exit(exitCode);
	  }

}
