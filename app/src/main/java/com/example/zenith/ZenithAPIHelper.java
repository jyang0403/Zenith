package com.example.zenith;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.kwabenaberko.newsapilib.models.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ZenithAPIHelper {


    // swap between these API keys if one runs out of calls
    //private String NewsAPIKey = "1aa97f4444fb408d84c534d24cb5b086";
    //private String NewsAPIKey = "26bdc819fdde470aa1c4dcbd0ece8a95";
    private String NewsAPIKey = "63c89ad27a8242d6bc0f7ea00e4fef98";

    private String countryAPIKey = "2Zd5CSw4HLTq08UJSi0BLw==hMsRF2HJbxUdIP5d";

    private final String[] countryCodes = {"ae", "at", "be", "bg", "br",
            "ca", "cn", "cu", "cz", "de",
            "fr", "ie", "hu", "il", "in",
            "jp", "lv", "ma", "no", "ph",
            "rs", "se", "za", "tr", "us"};

    private static final String[] countryFullNames = {"United Arab Emirates", "Austria", "Belgium", "Bulgaria", "Brazil",
            "Canada", "China", "Cuba", "Czechia", "Germany",
            "France", "Ireland", "Hungary", "Israel", "India",
            "Japan", "Latvia", "Morocco", "Norway", "Philippines",
            "Serbia", "Sweden", "South Africa", "Turkey", "United States"};

    private static final String[] countryContinents = {"Asia", "Europe", "Europe", "Europe", "South America",
            "North America", "Asia", "North America", "Europe", "Europe",
            "Europe", "Europe", "Europe", "Asia", "Asia",
            "Asia", "Europe", "Africa", "Europe", "Asia",
            "Europe", "Europe", "Africa", "Asia", "North America"};

    private static final String[] continentList = {"North America", "South America", "Europe", "Asia", "Africa", "Oceania"};

    public NewsApiClient newsApiClient = new NewsApiClient(NewsAPIKey);

    public static String[] getCountryFullNames() {
        return countryFullNames;
    }

    public static String[] getCountryContinents() { return countryContinents; }

    public static String[] getContinentList() { return continentList; }

    public String getCountryCode(String countryFullName) {
        int index = 0;
        for (String name : countryFullNames) {
            if (name.equals(countryFullName)) {
                return countryCodes[index];
            }
            index++;
        }
        return null;
    }

    public void getArticles(String country, String topic, String category, int pageSize, int pageNumber, NewsApiCallback callback) {
        final List<Article>[] articles = new List[1];
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .q(topic)
                        .pageSize(pageSize)
                        .page(pageNumber)
                        .country(country)
                        .category(category)
//                        .language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        articles[0] = response.getArticles();
                        ArrayList<FeedArticle> arts = new ArrayList();
                        for (Article a : articles[0]) {
                            FeedArticle fa = new FeedArticle(category, removeSourceFromTitle(a.getTitle()), country, a.getDescription(), a.getUrlToImage(), a.getUrl(), a.getSource().getName());
                            arts.add(fa);
                        }
                        callback.getCountryNews(arts);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        callback.getCountryNews(null);
                    }
                }
        );
    }

    private String removeSourceFromTitle(String description) {
        int endIndex = description.lastIndexOf("-");
        if (endIndex != -1) {
            description = description.substring(0, endIndex);
        }
        return description;
    }


    public void getDefaultArticles(String topic, int pageSize, int pageNumber) {
        final List<Article>[] articles = new List[1];
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .pageSize(pageSize)
                        .page(pageNumber)
                        .q(topic)
                        .language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        System.out.println(response.getArticles().get(0).getTitle());
                        articles[0] = response.getArticles();
                        for (Article a : articles[0]) {
                            System.out.println(a.getTitle());
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );
    }

    public void getCountryBackground(String topic, Context context, String category, final CountryApiCallback callback) {
        try {
            String URL = "https://en.wikipedia.org/w/api.php";

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, URL, null,
                    new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            JSONObject res = (JSONObject) response;
                            try {
                                JSONObject query = (JSONObject) res.get("query");
                                JSONObject pages = (JSONObject) query.get("pages");
                                String pageID = "0";
                                for (Iterator<String> it = pages.keys(); it.hasNext(); ) {
                                    pageID = it.next();
                                }
                                JSONObject article = (JSONObject) pages.get(pageID);
                                String text = (String) article.get("extract");
                                callback.getDetails(parseWikipediaCategory(text, category));
                            } catch (Exception ex) {
                                callback.getDetails(null);
                                System.out.println("ERROR");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                    callback.getDetails(null);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                    return headers;
                }

                @Override
                public byte[] getBody() {
                    String str = "action=query&titles=" + topic + "&format=json&prop=extracts&explaintext=";
                    return str.getBytes();
                }
            };

            String str = jsonObject.toString();

            queue.add(jsonObject);
        } catch (Exception ex) {
            System.out.println("ERROR");
            callback.getDetails(null);
        }
    }

    public void getCountryDetails(String countryName, Context context, CountryApiCallback callback) {
        String URL = "https://api.api-ninjas.com/v1/country?name=" + countryName;

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest  jsonObject = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            JSONObject jsonobject = jsonArray.getJSONObject(0);
                            Iterator<String> keysIterator = jsonobject.keys();
                            StringBuilder str = new StringBuilder();
                            LinkedHashMap<String, String> details = new LinkedHashMap<>();
                            while (keysIterator.hasNext()) {
                                String key = keysIterator.next();
                                String value = jsonobject.get(key).toString();
                                details.put(key, value);
                            }

                            callback.getDetails(details);

                        } catch (JSONException e) {
                            callback.getDetails(null);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("this is an error");
                System.out.println(error.getMessage());
                callback.getDetails(null);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Api-Key", countryAPIKey);
                return headers;
            }
        };

        queue.add(jsonObject);
    }

    public void getThisDay(int day, int month, String countryName, Context context) {
        String URL = "https://api.api-ninjas.com/v1/historicalevents?day=" + day + "&month=" + month;

        if (!countryName.isEmpty()) {
            URL += "&text=" + countryName;
        }

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, URL, null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Api-Key", countryAPIKey);
                return headers;
            }
        };

        queue.add(jsonObject);
    }


    public LinkedHashMap<String,String> parseWikipediaCategory(String article, String category) {
        LinkedHashMap<String, String> sections = new LinkedHashMap<>();
        String[] articleArr = article.split("\n== ");

        for (String s: articleArr) {
            String[] Arr = s.split(" ", 2);
            if (Arr[0].equals(category)) {
                String str = Arr[1];
                String[] sectionArr = str.split("\n=== ");
                for (int i=1; i < sectionArr.length; i++) {
                    String[] section = sectionArr[i].split(" ===\n", 2);
                    if(section[1].equals("\n")) continue;
                    sections.put(section[0], section[1].trim());
                }
                break;
            }
        }

        return sections;
    }

    public interface CountryApiCallback {
        void getDetails(LinkedHashMap<String, String> sections);
    }

    public interface NewsApiCallback {
        void getCountryNews(ArrayList<FeedArticle> articles);
    }
}
