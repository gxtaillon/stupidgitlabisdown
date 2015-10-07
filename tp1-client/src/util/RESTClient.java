package util;
import gxt.common.Either;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class RESTClient {
	public static <R> Either<Object, R> get
			( String uri // where to request
			, Class<R> rclass // class of the object to get
			) {
		return readwrite(uri, "GET", rclass, null);
	}
	
	private static <R, W> Either<Object, R> readwrite
			( String uri // where to request
			, String method //GET,PUT,etc.
			, Class<R> rclass // class of the object to read
			, W write // object to write
			) {
		HttpURLConnection connection = null;
		Scanner sc = null;
		try {
	        URL url = new URL(uri);
	        connection = (HttpURLConnection) url.openConnection(); 
	        connection.setDoInput(rclass != null); 
	        connection.setDoOutput(write != null); 
	        connection.setInstanceFollowRedirects(false); 
	        connection.setRequestMethod(method); 
	        connection.setRequestProperty("Content-Type", "application/json"); 

	        Gson gson = new Gson();
	        if (write != null) {
		        OutputStream os = connection.getOutputStream();
		        os.write(gson.toJson(write).getBytes(Charset.forName("UTF-8")));
		        os.flush();
	        }
	        
	        int rc = connection.getResponseCode();
	        if (rc != 200) {
	        	return Either.<Object, R>Left(
	        			rc, "HTTP response code is not 200.");
	        }
	        InputStream is = connection.getInputStream();
	        sc = new Scanner(is).useDelimiter("\\A");
	        if (!sc.hasNext()) {
	        	return Either.<Object, R>Left(
	        			null, "HTTP response is empty.");
	        }
	        String json = sc.next();
	        System.out.println(json);
			R ms = gson.fromJson(json, rclass);
        	return Either.<Object, R>Right(
	    			ms, "Successful query.");
		} catch (JsonSyntaxException e) {
        	return Either.<Object, R>Left(
        			e, "Thrown JsonSyntaxException.");
		} catch (MalformedURLException e) {
        	return Either.<Object, R>Left(
        			e, "Thrown MalformedURLException.");
		} catch (IOException e) {
        	return Either.<Object, R>Left(
        			e, "Thrown IOException.");
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (sc != null) {
				sc.close();
			}
		}
	}
}