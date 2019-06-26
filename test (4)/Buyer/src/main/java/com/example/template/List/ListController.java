package com.example.template.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins="*")
@RestController
@RequestMapping("/List")
public class ListController {

    @RequestMapping(value = "/prodbuy", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
   public List prodbuy (HttpServletRequest request, HttpServletResponse response) 
         throws Exception {
          return null;
           }
   

}