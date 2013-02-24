package org.thelq.stackarchive.bot.se;

import java.io.IOException;
import java.util.Properties;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Leon
 */
public class SEAPI {
	protected static final Logger log = LoggerFactory.getLogger(SEAPI.class);
	protected final String seApiKey;
	protected static SEAPI instance;
	protected HttpClient httpclient = new DecompressingHttpClient(new DefaultHttpClient());

	static {
		try {
			instance = new SEAPI();
		} catch (IOException ex) {
			log.error("FATAL EXCEPTION: Can't load config.properties", ex);
			System.exit(2);
		}
	}

	public static SEAPI get() {
		return instance;
	}

	public SEAPI() throws IOException {
		Properties config = new Properties();
		config.load(getClass().getResourceAsStream("/config.properties"));
		seApiKey = config.getProperty("se_api_key");
	}

	public void getRecent() throws Exception {
		querySE("posts", "stackoverflow");
	}

	protected JSONObject querySE(String method, String site, String... options) throws Exception {
		HttpGet httpGet = null;
		try {
			String url = "https://api.stackexchange.com/2.1/" + method + "?key=" + seApiKey + "&site=" + site;
			for (String curOption : options)
				url += "&" + curOption;
			log.debug("Querying API with URL: " + url);

			//Do the request
			//TODO: Figure out how to handle errors with different status codes (causes Exception)
			httpGet = new HttpGet(url);
			HttpResponse responseHttp = httpclient.execute(httpGet);
			String responseRaw = EntityUtils.toString(responseHttp.getEntity());
			JSONObject responseJSON = (JSONObject) JSONSerializer.toJSON(responseRaw);

			//Handle errors
			if (responseJSON.containsKey("error_id"))
				//Have an error, throw an exception
				throw new SEException(responseJSON.getInt("error_id"), responseJSON.getString("error_name"),
						responseJSON.getString("error_message"));

			//No errors, were done
			return responseJSON;
		} catch (Exception e) {
			throw new Exception("Can't query StackExchange", e);
		} finally {
			if (httpGet != null)
				httpGet.releaseConnection();
		}
	}
}
