package org.climbing.web;

import org.climbing.repo.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/")
public class HomeController {

	@Autowired
	UserDAO userDao;
	
	@RequestMapping(method=RequestMethod.GET)
    public String home(Model model)
    {
        return "redirect:/persons";
    }
	
	@RequestMapping(method=RequestMethod.GET, value = "/login")
    public String login(Model model)
    {
        return "login";
    }
	
}
