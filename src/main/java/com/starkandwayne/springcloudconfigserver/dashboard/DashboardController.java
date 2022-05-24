package com.starkandwayne.springcloudconfigserver.dashboard;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class DashboardController {

   
   @RequestMapping(path = "/dashboard")
   public String index() {

       return "index";
   }
}