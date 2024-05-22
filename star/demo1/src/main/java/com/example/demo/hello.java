//package com.example.demo;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.env.Environment;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class hello {
//    @Autowired
//    private Environment env;
//    @Autowired
//    private person person;
//    @RequestMapping("/hello3")//请求页面
//    public String hello3()
//    {
//        System.out.println(person.getName()+" "+person.getAge());
//        return "hello2";
//    }
//
//
//    @RequestMapping("/hello")
//    public String hello()
//    {
//        return "hello";
//    }
//}
