package com.ankit.poc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

  @GetMapping("/hello/{name}")
  public String hello(@PathVariable String name) {
    return "Hello, " + name + "!";
  }
}
