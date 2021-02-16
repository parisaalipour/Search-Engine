package com.uu.searchengine;

import com.uu.searchengine.dto.WebPageDto;
import com.uu.searchengine.handlers.Handler;
import com.uu.searchengine.handlers.Provider;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Spider {


    private static final int MAX_PAGES_TO_SEARCH = 10000;
    private static Set<String> pagesVisited = new HashSet<String>();
    private static ArrayList<String> pagesToVisit = new ArrayList<>();
    public static String currentUrl;

//    public void crawl(String nextURL) // Give it a URL and it makes an HTTP request for a web page
//    public boolean searchForWord(String word) // Tries to find a word on the page
//    public List<String> getLinks() // Returns a list of all the URLs on the page



    /**
     * Returns the next URL to visit (in the order that they were found). We also do a check to make
     * sure this method doesn't return a URL that has already been visited.
     *
     * @return
     */
    private static String nextUrl()
    {
        String nextUrl = null;
        do
        {
            if(!pagesToVisit.isEmpty()) {
                nextUrl = pagesToVisit.remove(0);
                if (nextUrl == null)
                    System.out.println("next url : " + pagesToVisit.size());
            }
        } while(pagesVisited.contains(nextUrl));
        pagesVisited.add(nextUrl);
        return nextUrl;
    }



    /**
     * Our main launching point for the Spider's functionality. Internally it creates spider legs
     * that make an HTTP request and parse the response (the web page).
     *
     * @param url
     *            - The starting point of the spider
     */

    private static Thread databaseThread;
    private static Thread crawlerThread;

    public static void search(String url) throws IOException, JSONException {


        databaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    if (!queue.isEmpty()) {
                        try {
                            Handler.INSTANCE.addWebPageDto(queue.poll());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        databaseThread.setPriority(Thread.MAX_PRIORITY);
        databaseThread.start();

        crawlerThread = new Thread(new Runnable() {
            @Override
            public void run() {

                var i = 0;

                while (true)
                {

//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    SpiderLeg leg = new SpiderLeg();
                    if(pagesToVisit.isEmpty())
                    {
                        currentUrl = url;
                        pagesVisited.add(url);
                    }
                    else
                    {
                        currentUrl = nextUrl();
                    }
                    boolean success = leg.crawl(currentUrl); // Lots of stuff happening here. Look at the crawl method in
                    // SpiderLeg

                    if (success) {
                        System.out.println("coooouuuunnnntttttt " + ++i);
                        System.out.println("pages visited " + pagesVisited.size());
                        System.out.println("pages to visit " + pagesToVisit.size());
                        if (leg.getWebPageDto() != null)
                            queue.add(leg.getWebPageDto());
                    }
                    pagesToVisit.addAll(leg.getLinks());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        crawlerThread.start();

//
//        while(this.pagesVisited.size() < MAX_PAGES_TO_SEARCH)
//        {
////            String currentUrl;
//            SpiderLeg leg = new SpiderLeg();
//            if(this.pagesToVisit.isEmpty())
//            {
//                currentUrl = url;
//                this.pagesVisited.add(url);
//            }
//            else
//            {
//                currentUrl = this.nextUrl();
//            }
//            boolean success = leg.crawl(currentUrl); // Lots of stuff happening here. Look at the crawl method in
//            // SpiderLeg
//
//            System.out.println(++i);
//            if (success) {
//                leg.writeDocument();
////                System.out.println(String.format("**Success** URL found at %s", currentUrl));
////                break;
//            }
//            this.pagesToVisit.addAll(leg.getLinks());
//        }
//        System.out.println(String.format("**Done** Visited %s web page(s)", this.pagesVisited.size()));
    }


    private static BlockingQueue<WebPageDto> queue = new ArrayBlockingQueue<WebPageDto>(100000);


    public static void stopSearch()
    {
        try { databaseThread.stop();
            crawlerThread.stop();
        } catch (Exception e) { e.printStackTrace();}

    }

}
