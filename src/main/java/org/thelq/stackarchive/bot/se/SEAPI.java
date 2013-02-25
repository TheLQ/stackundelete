package org.thelq.stackarchive.bot.se;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.JavaIdentifierTransformer;
import net.sf.json.util.PropertySetStrategy;
import org.apache.commons.beanutils.DynaProperty;
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
		querySE(PostEntry.class, "posts", "stackoverflow");
	}

	protected <E> ResponseEntry querySE(Class<E> itemClass, String method, String site, String... options) throws Exception {
		HttpGet httpGet = null;
		try {
			String url = "https://api.stackexchange.com/2.1/" + method + "?key=" + seApiKey + "&site=" + site;
			for (String curOption : options)
				url += "&" + curOption;

			//Do the request
			//TODO: Figure out how to handle errors with different status codes (causes Exception)
			//TODO: Handle backoff times
			log.debug("Querying API with URL: " + url);
			httpGet = new HttpGet(url);
			HttpResponse responseHttp = httpclient.execute(httpGet);
			String responseRaw = EntityUtils.toString(responseHttp.getEntity());
			JSONObject responseJSON = (JSONObject) JSONSerializer.toJSON(responseRaw);

			//Handle errors
			if (responseJSON.containsKey("error_id"))
				//Have an error, throw an exception
				throw new SEException(responseJSON.getInt("error_id"), responseJSON.getString("error_name"),
						responseJSON.getString("error_message"));

			//No errors, convert what we can to ResponseEntry automatically
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(ResponseEntry.class);
			jsonConfig.setJavaIdentifierTransformer(new UnderscoreCamelCaseTransformer());
			jsonConfig.setPropertySetStrategy(new TreePropertySetStrategy());
			ResponseEntry responseEntry = (ResponseEntry) JSONSerializer.toJava(responseJSON, jsonConfig);

			//Covert items to their respective classes
			JSONArray itemsArray = responseJSON.getJSONArray("items");

			return responseEntry;
		} catch (Exception e) {
			throw new Exception("Can't query StackExchange", e);
		} finally {
			if (httpGet != null)
				httpGet.releaseConnection();
		}
	}

	protected static class UnderscoreCamelCaseTransformer extends JavaIdentifierTransformer {
		public String transformToJavaIdentifier(String str) {
			//Slightly modified version of CamelCaseJavaIdentifierTransformer
			if (str == null)
				return null;

			String str2 = shaveOffNonJavaIdentifierStartChars(str);

			char[] chars = str2.toCharArray();
			int pos = 0;
			StringBuffer buf = new StringBuffer();
			boolean toUpperCaseNextChar = false;
			while (pos < chars.length) {
				if (!Character.isJavaIdentifierPart(chars[pos])
						|| chars[pos] == '_')
					toUpperCaseNextChar = true;
				else if (toUpperCaseNextChar) {
					buf.append(Character.toUpperCase(chars[pos]));
					toUpperCaseNextChar = false;
				} else
					buf.append(chars[pos]);
				pos++;
			}
			return buf.toString();
		}
	}

	protected static class TreePropertySetStrategy extends PropertySetStrategy {
		@Override
		public void setProperty(Object bean, String key, Object value) throws JSONException {
			log.debug("Key: " + key + " | Value class: " + value.getClass());
			if (value instanceof ArrayList)
				for (MorphDynaBean curValueEntry : (ArrayList<MorphDynaBean>) value) {
					log.debug(" - Subentry:");
					for(DynaProperty curProperty : curValueEntry.getDynaClass().getDynaProperties())
					log.debug("  -: Key: " + curProperty.getName() + " | Value: " + curValueEntry.get(curProperty.getName()));
				}

			PropertySetStrategy.DEFAULT.setProperty(bean, key, value);
		}
	}
}
