package org.thelq.stackarchive.bot.se;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.Module.SetupContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.deser.Deserializers.Base;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
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
		ResponseEntry<PostEntry> entry = querySE(PostEntry.class, "posts", "stackoverflow");
		log.debug("Items class: " + entry.getItems());
		for (PostEntry curEntry : entry.getItems())
			log.debug(" - " + curEntry.toString());
	}

	protected <E> ResponseEntry<E> querySE(Class<E> itemClass, String method, String site, String... options) throws Exception {
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

			//Handle errors
			/*
			 if (responseJSON.containsKey("error_id"))
			 //Have an error, throw an exception
			 throw new SEException(responseJSON.getInt("error_id"), responseJSON.getString("error_name"),
			 responseJSON.getString("error_message"));
			 */

			//No errors, convert what we can to ResponseEntry automatically
			ObjectMapper mapper = new ObjectMapper();
			mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
			//DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING 
			mapper.registerModule(new EnumCaseInsensitiveModule());
			ResponseEntry responseEntry = mapper.readValue(responseRaw, new TypeReference<ResponseEntry<PostEntry>>() {
			});

			return responseEntry;
		} catch (Exception e) {
			throw new Exception("Can't query StackExchange", e);
		} finally {
			if (httpGet != null)
				httpGet.releaseConnection();
		}
	}

	public static class LowerEnumDeserializer extends StdScalarDeserializer<Enum<?>> {
		protected LowerEnumDeserializer(Class<Enum<?>> clazz) {
			super(clazz);
		}

		@Override
		public Enum<?> deserialize(JsonParser jp, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			String text = jp.getText().toUpperCase();
			try {
				Method valueOfMethod = getValueClass().getDeclaredMethod("valueOf", String.class);
				return (Enum<?>) valueOfMethod.invoke(null, text);
			} catch (Exception e) {
				throw new RuntimeException("Cannot deserialize enum " + getValueClass().getName() + " from " + text, e);
			}
		}
	}

	public static class EnumCaseInsensitiveModule extends SimpleModule {
		public EnumCaseInsensitiveModule() {
			super("stackarchive-se", new Version(1, 0, 0, "", "org.thelq", "stackarchive-se"));
		}

		@Override
		public void setupModule(SetupContext context) {
			super.setupModule(context);
			Base deser = new Deserializers.Base() {
				@SuppressWarnings("unchecked")
				@Override
				public JsonDeserializer<?> findEnumDeserializer(Class<?> type,
						DeserializationConfig config, BeanDescription beanDesc)
						throws JsonMappingException {
					return new LowerEnumDeserializer((Class<Enum<?>>) type);
				}
			};
			context.addDeserializers(deser);
		}
	;
}
}
