package com.kkpa.jbh.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class TestController {
  @GetMapping("/test")
  fun test(model:Model): String {
    model.addAttribute("message", "Hello, Thymeleaf!")
    return "test"
  }
}
