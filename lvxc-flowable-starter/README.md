
# 集成flowable-modeler方式

1. 在application上排除springsecurity的配置  @SpringBootApplication(exclude = {SecurityAutoConfiguration.class})

2. 再ShiroConfig上开启一些配置的白名单：
   map.put("/idm/**", "anon");
   map.put("/app/**", "anon");
   map.put("/modeler/**", "anon");
   map.put("/rest/models/*/thumbnail", "anon");
   map.put("/rest/models/**/model-json", "anon");

3. 参考demo: git@git.aihuoshi.net:industry-brain/standard-product/be-base-power-center.git   main
   启动后：http://localhost:8889/modeler/  ，需要把token参数带上。
   例： http://localhost:8889/modeler/#/processes?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwbGF0Zm9ybUlkIjoiMmFkNWZiYTRhNmJhNmMzNmY3YTAyYjVkMTNlNjU4ZjMiLCJleHAiOjE3MDE0MTc3MDUsInVzZXJuYW1lIjoibHZ4YyJ9.tKUVVQNXSRXQZrARfnljKWekDGcpcOgNXQ7lZvRNjLE



