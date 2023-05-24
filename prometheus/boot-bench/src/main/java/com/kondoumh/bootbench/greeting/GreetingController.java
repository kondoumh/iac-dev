package com.kondoumh.bootbench.greeting;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greeting")
public class GreetingController {

  @GetMapping(path = "/hello/{name}")
  public String index(@PathVariable String name) throws InterruptedException {
    Thread.sleep(1000L);
    return "Hello " + name;
  }
}
