package com.uu.searchengine.controller;

//import com.uu.searchengine.redis.CacheImpl;
import com.uu.searchengine.crawler.SingleWebPageCrawler;
import com.uu.searchengine.entity.Document;
import com.uu.searchengine.entity.Image;
import com.uu.searchengine.handlers.Handler;
import com.uu.searchengine.handlers.Provider;
import com.uu.searchengine.spellcorrection.KgramIndexing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
class SearchController {

    public List<Document> webpages = new ArrayList<>();
    public ArrayList<Image> images = new ArrayList<>();
    public List<String> correctedWords = new ArrayList<>();
    public KgramIndexing k = new KgramIndexing();

    @GetMapping("/")
    public String showMainPage(Model model) {
        return "main";
    }

    @PostMapping("/results")
//    @Cacheable(value = "webpages", key = "#query")
    public String processResultsPage(HttpServletRequest request, Model model) {
        webpages.clear();
        images.clear();
        correctedWords.clear();

        System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPP");
        String query = request.getParameter("query");
        webpages = Provider.INSTANCE.search(query);

        Set<Image> imgs = new HashSet<>();
        for (Document webpage : webpages) {
            imgs.addAll(webpage.getImages());
        }

        images.addAll(imgs);

        correctedWords = k.correctAndReturnSuggestions(query, 3);
        model.addAttribute("webpages", webpages);
        model.addAttribute("images", images);
        model.addAttribute("correctedWords", correctedWords);
        System.out.println("this is query from results page: " + query);
        System.out.println(webpages.toString());

        return "results";
    }

    // Single item retrieving
    /** RequestMapping.GET
     * EX: curl -v http://localhost:8093/result/friend+ir  */

    @RequestMapping(value = "/result/{query}", method = RequestMethod.GET)
    @Cacheable(value = "webpages", key = "#query")
    public @ResponseBody
    String one(@PathVariable String query) throws JSONException {

        System.out.println("/result/{query}");
        webpages.clear();
        images.clear();
        correctedWords.clear();

        webpages = Provider.INSTANCE.search(query);

        Set<Image> imgs = new HashSet<>();
        for (Document webpage : webpages) {
            imgs.addAll(webpage.getImages());
        }

        images.addAll(imgs);

        correctedWords = k.correctAndReturnSuggestions(query, 3);

        JSONArray websites = new JSONArray();

        for (Document d: webpages) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", d.getTitle());
            jsonObject.put("url", d.getUrl());
            websites.put(jsonObject);
        }

        ResponseEntity.ok(websites.toString());
//        ResponseEntity.ok(jsonObject);
        return websites.toString();
    }


    @GetMapping("/addUrl")
    public String showAddUrlPage() {
        return "addUrl";
    }

    @PostMapping(value = "/addUrl", params = "add_button")
    @CacheEvict(value = "webpages", allEntries = true)
    public String addUrl(HttpServletRequest request) {
        String url = request.getParameter("url");
        new SingleWebPageCrawler().crawl(url);
        System.out.println("added " + url);
        return "redirect:/addUrl?addUrlsuccess";
    }

    @RequestMapping(value = "/addUrl", params = "delete_button", method = RequestMethod.POST)
    @CacheEvict(value = "webpages", allEntries = true)
    public String DeleteUrl(HttpServletRequest request) {
        String url = request.getParameter("url");
        if (Provider.INSTANCE.getDocumentByUrl(url) != null) {
            Handler.INSTANCE.deleteWebPageDto(url);
            System.out.println("deleted " + url);
            return "redirect:/addUrl?deleteUrlsuccess";
        }
        return "redirect:/addUrl?error";
    }

    @PostMapping(value = "/addUrl", params = "update_button")
    @CacheEvict(value = "webpages", allEntries = true)
    public String UpdateUrl(HttpServletRequest request) {
        String url = request.getParameter("url");
        if (Provider.INSTANCE.getDocumentByUrl(url) != null) {
            Handler.INSTANCE.deleteWebPageDto(url);
            new SingleWebPageCrawler().crawl(url);
            System.out.println("updated " + url);
            return "redirect:/addUrl?updateUrlsuccess";
        }
        return "redirect:/addUrl?error";
    }

    /**
     * curl -i -X DELETE localhost:8093/Url?url=http://google.com
     * */
    @RequestMapping(value = "/Url", method = RequestMethod.DELETE)
    @CacheEvict(value = "webpages", allEntries = true)
    public @ResponseBody
    ResponseEntity<String> delete(@RequestParam String url) {

        Document document = Provider.INSTANCE.getDocumentByUrl(url);
        if(document != null) {
            webpages.remove(document);
            Handler.INSTANCE.deleteWebPageDto(url);
            System.out.println("delete " + url);
            return ResponseEntity.ok(url + " DELETED SUCCESSFULLY");
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );

    }

    /**ADD URL
     * EX : curl -X POST -H "Content-type: application/json" -d "{\"url\" : \"http://google.com\"}" http://localhost:8093/Url
     */
    @RequestMapping(value = "/Url", method = RequestMethod.POST)
    @CacheEvict(value = "webpages", allEntries = true)
    public @ResponseBody
    ResponseEntity<String> post(@Valid @RequestBody Document doc) {
        SingleWebPageCrawler singleWebPageCrawler = new SingleWebPageCrawler();
        System.out.println("post " + doc.getUrl());
        singleWebPageCrawler.crawl(doc.getUrl());
        return ResponseEntity.ok("ADDED SUCCESSFULLY");
    }

    /** UPDATE URL
     * EX : curl -X PUT -H "Content-type: application/json" -d "{\"url\" : \"http://google.com\"}" http://localhost:8093/Url*/
    @RequestMapping(value = "/Url", method = RequestMethod.PUT)
    @CacheEvict(value = "webpages", allEntries = true)
    public @ResponseBody
    ResponseEntity<String> update(@Valid @RequestBody Document doc) {

        Document doc1 = Provider.INSTANCE.getDocumentByUrl(doc.getUrl());
        if (doc1 != null) {
            Handler.INSTANCE.deleteWebPageDto(doc.getUrl());
            SingleWebPageCrawler singleWebPageCrawler = new SingleWebPageCrawler();
            singleWebPageCrawler.crawl(doc.getUrl());
            System.out.println("updated " + doc.getUrl());
            return ResponseEntity.ok("UPDATED SUCCESSFULLY");
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
    }
}