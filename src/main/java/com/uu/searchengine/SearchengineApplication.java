package com.uu.searchengine;

import com.uu.searchengine.dto.ImageDto;
import com.uu.searchengine.dto.WebPageDto;
import com.uu.searchengine.handlers.Handler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.ArrayList;

@SpringBootApplication
@EnableCaching
public class SearchengineApplication {


//	DocumentTermService documentTermService = BeanUtil.getBean(DocumentTermService.class);


	public static void main(String[] args) {
		SpringApplication.run(SearchengineApplication.class, args);

	}
}
