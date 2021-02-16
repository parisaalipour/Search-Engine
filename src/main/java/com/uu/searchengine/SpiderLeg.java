package com.uu.searchengine;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.uu.searchengine.dto.ImageDto;
import com.uu.searchengine.dto.WebPageDto;
import org.apache.commons.validator.routines.UrlValidator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SpiderLeg {

    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<String>(); // Just a list of URLs
    private Document htmlDocument; // This is our web page, or in other words, our document
    static FileWriter myWriter2;
    static FileWriter myWriter1;


    public SpiderLeg(){
    }

    /**
     * This performs all the work. It makes an HTTP request, checks the response, and then gathers
     * up all the links on the page. Perform a searchForWord after the successful crawl
     *
     * @param url
     *            - The URL to visit
     * @return whether or not the crawl was successful
     */
    public boolean crawl(String url)
    {
        try {
            System.out.println("Current URL : " + url);

            String[] schemes = {"http", "https"}; // DEFAULT schemes = "http", "https", "ftp"
            UrlValidator urlValidator = new UrlValidator(schemes);
            char[] s;
            if (urlValidator.isValid(url)) {
                Connection connection = Jsoup.connect(url).userAgent(USER_AGENT).timeout(3_000);
                Document htmlDocument = connection.get();
                this.htmlDocument = htmlDocument;
                if (connection.response().statusCode() == 200) // 200 is the HTTP OK status code
                // indicating that everything is great.
                {
//                 System.out.println("\n**Visiting** Received web page at " + url);
                }
                if (!connection.response().contentType().contains("text/html")) {
                    System.out.println("**Failure** Retrieved something other than HTML");
                    return false;
                }
                Elements linksOnPage = htmlDocument.select("a[href]");
                System.out.println("Found (" + linksOnPage.size() + ") links");

                for (Element link : linksOnPage) {
                    /** delete "/" from end of urls */
                    s = link.absUrl("href").toLowerCase().toCharArray();
//                 System.out.println("print url after delete : " + String.valueOf(s));
//                 System.out.println("s length : " + s.length);
//                 System.out.println(s[s.length-1]);
                    if(s.length != 0 && s[s.length-1] == '/') {
                        s = Arrays.copyOfRange(s, 0, s.length - 1);
                        this.links.add(String.valueOf(s));
                    }
                    else{
                        this.links.add(link.absUrl("href").toLowerCase());
                    }
                }
            }
            return true;
        }
        catch(IOException ioe)
        {
            // We were not successful in our HTTP request
            return false;
        }
    }


    /**
     * Performs a search on the body of on the HTML document that is retrieved. This method should
     * only be called after a successful crawl.
     *
     * @return whether or not the word was found
     */
    public WebPageDto getWebPageDto() {
        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null)
        {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
//            myWriter2.write(Spider.currentUrl);
//            myWriter2.write("\n");
            return null;
        }

//        SpiderLeg parser = new SpiderLeg();
//        Element bodyText = this.htmlDocument.body();
//        myWriter.write(Spider.currentUrl);

//        myWriter1.write(Spider.currentUrl);
//        myWriter1.write("\n");

//        myWriter.write(String.valueOf(bodyText));
//        myWriter.write("************************************");

//        myWriter.write(this.htmlDocument.title());
//        myWriter.write("##################################");
////        myWriter.write(bodyText.getAllElements().text());
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.put(parser.parse(this.htmlDocument));
//        System.out.println(jsonArray.toString());

        /* Add images */
        Elements elements = this.htmlDocument.select("img");
        List<ImageDto> imageDtos2  = new ArrayList<ImageDto>();

        for (Element e : elements) {
            imageDtos2.add(new ImageDto(e.absUrl("src") , e.absUrl("alt")));
        }


        WebPageDto webPageDto1 = new WebPageDto(Spider.currentUrl , this.htmlDocument.title() , this.htmlDocument.body().text() , imageDtos2);


        return webPageDto1;
    }


    public List<String> getLinks()
    {
        return this.links;
    }

    /* Parse JSONArray */
    public JSONObject parse(Document doc) throws JSONException {
        String url = Spider.currentUrl;
        String title = doc.title();
        String body = doc.body().text();
        Elements elements = doc.select("img");
        ArrayList<String> imagesSrc = new ArrayList<>();
        ArrayList<String> imagesAlt = new ArrayList<>();
        for (Element e : elements) {
            imagesSrc.add(e.absUrl("src"));
            imagesAlt.add(e.absUrl("alt"));
        }

        JSONArray imgs = new JSONArray();
        JSONObject imgObject = new JSONObject();
        for (int i = 0; i < elements.size(); i++) {
            imgObject.put("src", imagesSrc.get(i));
            imgObject.put("alt", imagesAlt.get(i));
            imgs.put(imgObject);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", url);
        jsonObject.put("title", title);
        jsonObject.put("body", body);
        jsonObject.put("images", imgs);

        return jsonObject;
    }
}
