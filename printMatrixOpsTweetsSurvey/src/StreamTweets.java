import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class MapUtil
{
	public static <K, V extends Comparable<? super V>> Map<String, Integer> 
	sortByValue( Map<String, Integer> map )
	{
		List<Map.Entry<String, Integer>> list =
			new LinkedList<Map.Entry<String, Integer>>( map.entrySet() );
		Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
				{
			public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
			{
				return o2.getValue() > o1.getValue()? 1:-1 ;
			}
				} );

		Map<String, Integer> result = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : list)
		{
			result.put( entry.getKey(), entry.getValue() );
		}
		return result;
	}
}


public class StreamTweets {

	public static Map<String, Integer> wordTweet = new Hashtable<String, Integer>(1000);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://search.twitter.com/search.json?q=sonarme";
		JSONObject myjson;
		JSONArray jsonarray;
		JSONObject jsontext;
		String tweets;
		MapUtil mputils = new MapUtil();
		int noIterations=10;
		for (int j = 0; j < noIterations; j++) {
			try {
				myjson = readJsonFromUrl(url);
				jsonarray = myjson.getJSONArray("results");
				for (int i = 0; i < jsonarray.length(); i++) {
					jsontext = jsonarray.getJSONObject(i);
					tweets = (String) jsontext.get("text");
					parseTweets(tweets);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//printMap(wordTweet);
		}

		Map<String, Integer> result = mputils.sortByValue(wordTweet);
		printMap(result);

	}

	private static void printMap(Map<String, Integer> words) {
		// TODO Auto-generated method stub
		Iterator iterator = words.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			String value = words.get(key).toString();
			System.out.println(key + " " + value);
		}

	}

	private static void parseTweets(String tweets) {
		String[] words = tweets.split("\\W+");
		for (String word : words) {

			if(word.trim()!=null && word.trim()!=""){

				word=word.toLowerCase();
				if(wordTweet.get(word)==null){
					wordTweet.put(word,1);
				}else{
					int value = wordTweet.get(word)+1;
					wordTweet.put(word,value);
				}
			}


		}
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

}
