package org.climbing.web;

import java.util.List;

import org.climbing.domain.User;
import org.climbing.repo.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/test")
public class TestController {

	@Autowired
	UserDAO userDao;
	
	@RequestMapping(method=RequestMethod.GET)
    public String test(Model model)
    {
		List<User> users = userDao.findAll("username", "desc");
		model.addAttribute("users", users);
        return "index";
    }
	
}
