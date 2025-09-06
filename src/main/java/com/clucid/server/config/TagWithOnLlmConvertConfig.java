package com.clucid.server.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TagWithOnLlmConvertConfig {
	/** llm word to db tag mapping */
	@Bean
	public Map<String,String> tagWithOnLlmConvertMap(){
		Map<String,String> map = new HashMap<>();
		// ('af0d808d-5781-4647-8a6c-eded147dd557','프론트엔드','frontend')
       // ,('7d5d1402-8993-4dfa-81c8-06370f674ea3','백엔드','backend')
       // ,('849beebc-37a2-4e6e-b052-a0c0438d5dd6','UX/UI','uxui')
       // ,('1cd87c78-c3ef-4aa3-9175-a7910a133826','디자인','design')
       // ,('abcbcb4a-e4a6-4b45-a081-c1759a585a61','마케팅','marketing')
       // ,('cfe02543-7451-4274-8e94-aa8ce7ac7851','데이터','datascience')
       // ,('b7cc27a8-15c6-4e84-986c-7f40bf44c4e3','AI','artificialintelligence')
       // ,('e8819645-23a6-47b7-bc49-485a4d245649','비즈니스','business')
       // ,('3fd2f031-6141-4a22-af05-027caaeca596','커뮤니케이션','communication')
		// ;
		map.put("frontend","프론트엔드");
		map.put("backend","백엔드");
		map.put("uxui","UX/UI");
		map.put("design","디자인");
		map.put("marketing","마케팅");
		map.put("datascience","데이터");
		map.put("artificialintelligence","AI");
		map.put("business","비즈니스");
		map.put("communication","커뮤니케이션");
		return map;
	}
}
