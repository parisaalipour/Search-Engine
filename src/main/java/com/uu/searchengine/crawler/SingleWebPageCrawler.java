package com.uu.searchengine.crawler;

import com.uu.searchengine.dto.ImageDto;
import com.uu.searchengine.dto.WebPageDto;
import com.uu.searchengine.handlers.Handler;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SingleWebPageCrawler {

    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private Document htmlDocument; // This is our web page, or in other words, our document

    /**
     * This performs all the work. It makes an HTTP request, checks the response, and then gathers
     * up all the links on the page. Perform a searchForWord after the successful crawl
     *
     * @param url - The URL to visit
     */

    String url;

    public void crawl(String url) {
        this.url = url;
//        String urlDecoder= URLEncoder.encode(url,"UTF-8");

        Connection connection = Jsoup.connect(url).userAgent(USER_AGENT).timeout(3000);
        try {
            String[] schemes = {"http", "https"}; // DEFAULT schemes = "http", "https", "ftp"
            UrlValidator urlValidator = new UrlValidator(schemes);
            System.out.println(urlValidator.isValid(url));
            if (urlValidator.isValid(url)) {
                Document htmlDocument = connection.get();
                this.htmlDocument = htmlDocument;
                System.out.println(connection.response().statusCode());
                if (connection.response().statusCode() == 200) // 200 is the HTTP OK status code
                // indicating that everything is great.
                {
//                    System.out.println("\n*Visiting* Received web page at " + url);
                    writeDocument();
                    System.out.println("url added successfully ...!");
                }
            }

        } catch (IOException ioe) {
            // We were not successful in our HTTP request
            System.out.println("We were not successful in our HTTP request");
            System.out.println("Please Try Again ...");
            // todo: handle "Please Try Again"
//            crawl(url);
        }
    }


    /**
     * Performs a search on the body of on the HTML document that is retrieved. This method should
     * only be called after a successful crawl.
     *
     */
    private void writeDocument(){
        // Defensive coding. This method should only be used after a successful crawl.
        if (this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
        }


        Elements elements = this.htmlDocument.select("img");
        List<ImageDto> imageDtos  = new ArrayList<ImageDto>();

        for (Element e : elements) {
            imageDtos.add(new ImageDto(e.absUrl("src") , e.absUrl("alt")));
        }

        char[] s;
        /** delete "/" from end of urls */
        s = url.toLowerCase().toCharArray();
        if(s.length != 0 && s[s.length-1] == '/') {
            s = Arrays.copyOfRange(s, 0, s.length - 1);
            url = String.valueOf(s);
        }
        else{
            url = url.toLowerCase();
        }

        System.out.println(this.htmlDocument.title());
        System.out.println(this.htmlDocument.body().text());

        WebPageDto webPageDto = new WebPageDto(url ,htmlDocument.title() ,htmlDocument.body().text() , imageDtos);
        Handler.INSTANCE.addWebPageDto(webPageDto);
    }


    public static void main(String[] args){
        SingleWebPageCrawler SingleWebPageCrawler = new SingleWebPageCrawler();
//        crawler.crawl("http://dolat.ir/detail/356125");
//        crawler.crawl("https://virgool.io/@simeakhar659/%D8%A8%D9%87%D8%AA%D8%B1%DB%8C%D9%86-%D8%B1%D8%B4%D8%AA%D9%87-%D9%87%D8%A7-%D8%A8%D8%B1%D8%A7%DB%8C-%D9%85%D9%87%D8%A7%D8%AC%D8%B1%D8%AA-%D8%A8%D9%87-%DA%A9%D8%A7%D9%86%D8%A7%D8%AF%D8%A7-zbya30i9xe4t");
        SingleWebPageCrawler.crawl("http://www.urmia.ac.ir/nezarat");
//        crawler.crawl("https://www.hamshahrionline.ir/news/44748/");
//        crawler.crawl("https://www.hamshahrionline.ir/archive?tp=643#header-search");
//        crawler.crawl("https://virgool.io/@arnikadolatyari/%d8%af%d9%86%db%8c%d8%a7%db%8c-%d8%a7%db%8c%d9%86-%d8%b1%d9%88%d8%b2%d8%a7%db%8c-%d9%85%d9%86-%d9%88-%d8%b4%da%af%d9%81%d8%aa%db%8c-%d9%87%d8%a7%db%8c-%d8%a7%d9%88%d9%86-gmc1alqcgsbj");
//        String urlDecoder= URLEncoder.encode("https://www.hamshahrionline.ir/news/580336/ویدئو-ادعای-رئیس-کمیسیون-اصل-۹۰-مجلس-درباره-عاملان-اصلی-اوضاع","UTF-8");
//        String urlencoder= URLDecoder.decode(urlDecoder,"UTF-8");
//        System.out.println(urlencoder);
    }

}