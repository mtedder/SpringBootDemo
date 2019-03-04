package com.gc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.gc.dao.Dao;
import com.gc.dao.HibernateDao;
import com.gc.dao.JDBCDao;
import com.gc.dto.CustomUserDetails;
import com.gc.dto.GroupDto;
import com.gc.dto.StudentDto;
import com.gc.factory.DaoFactory;
import com.gc.repository.GroupRepository;
import com.gc.repository.StudentRepository;
import com.gc.util.AppUtil;

/**
 * Created by maurice on 6/27/17.
 * Modified June 2018 for Spring Boot and JpaRepository
 */

@Controller
@SessionAttributes("groupslist")//session variable
public class HomeController {
	
	//@Autowired
	//private JDBCDao dao;//Use this one for JDBC template
	//@Autowired
	//private HibernateDao dao;//Use this one of JPA/Hibernate version
	
	//JPA Repository version
	@Autowired
	private StudentRepository studentRepository;
	
	//JPA Repository version
	@Autowired	
	private GroupRepository groupRepository;
	
	//session variable
    private List<GroupDto> groupslist;

    /**
     * Home page request mapping
     * @param model
     * @return ModelAndView
     */
    @RequestMapping("/")
    public ModelAndView helloWorld(Model model) {
        //initialize groups dropdown
        //Dao dao = DaoFactory.getInstance(DaoFactory.JDBC);
        //List<GroupDto> groups = dao.getGroups();//get groups list
        List<GroupDto> groups = groupRepository.findAll();//get groups list
        groupslist = groups;
        model.addAttribute("groupslist", groups);//save session scope variable to persist across pages

        return new ModelAndView("index", "message", "Hello World!");
    }


    /**
     * Return admin page view. This is a secured resource
     * @param model
     * @return ModelAndView
     */
    @RequestMapping("admin")
    public ModelAndView admin(Model model) {
        /*Dao dao = DaoFactory.getInstance(DaoFactory.JDBC);
        List<GroupDto> groups = dao.getGroups();//get groups list
        model.addAttribute("groupslist", groups);//save session scope variable to persist across pages*/
        return new ModelAndView("admin", "message", "Welcome");
    }

    /**
    * Accepts comma separated list of student names and adds them to the data base
     */
    @RequestMapping(value = "addstudents", method = RequestMethod.POST)
    public ModelAndView addStudents(Authentication authentication, @RequestParam("namecsv") String studentList, @RequestParam("group") String group){

        User user =  (User) authentication.getPrincipal();        
        //Dao dao = DaoFactory.getInstance(DaoFactory.JDBC);

        if(AppUtil.isAuthorized(user, group)){//check if user is authorized to access this group
            List<StudentDto> students = AppUtil.parseStudentList(studentList.trim(), group);
            //dao.addStudents(students);
            studentRepository.saveAll(students);
            return new ModelAndView("admin", "message", "Students Added to DB");
        }
        return new ModelAndView("admin", "message", "Access denied for " + user.getUsername() + " to perform this operation!");
    }

    /*
    * Get a random list of students who have not already been selected (status = "X"). The number of students
    * selected is based on the listsize parameter submitted in the form
    * Ref: http://www.baeldung.com/get-user-in-spring-security
     */
    @RequestMapping(value = "getrandomstudents", method = RequestMethod.GET)
    public ModelAndView getRandomStudents(Authentication authentication, @RequestParam("numberofstudents") int listSize, @RequestParam("group") String group){

    	//System.out.println("authentication:" + authentication);//Debug
    	User user =  (User) authentication.getPrincipal();     
        //Dao dao = DaoFactory.getInstance(DaoFactory.JDBC);

        if(AppUtil.isAuthorized(user, group)){
            //List<StudentDto> students  = dao.getRandomStudents(listSize, group);
        	
        	studentRepository.updatePickRandom(listSize, group);//1.Pick random students
        	List<StudentDto> students = studentRepository.pickRandomStudents(group);//2. Select students with random selected status
        	studentRepository.updateDisableRandowStudents(group);//3. Set Randomly selected students to not available
        	
            return new ModelAndView("admin", "studentlist", students);
        }
        return new ModelAndView("admin", "message", "Access denied for " + user.getUsername() + " to perform this operation!");
    }

    /*
    * Resets all students in the database to available status A (=available)
     */
    @RequestMapping(value = "resetstudentlist", method = RequestMethod.GET)
    public ModelAndView resetStudentList(Authentication authentication, @RequestParam("group") String group){

    	User user =  (User) authentication.getPrincipal();    
        //Dao dao = DaoFactory.getInstance(DaoFactory.JDBC);
    
        if(AppUtil.isAuthorized(user, group)){
            //dao.resetStudentListStatus("A", group);
            studentRepository.resetStudentListStatus("A", group);
            return new ModelAndView("admin", "message", "StudentDto List has been reset");
        }
        return new ModelAndView("admin", "message", "Access denied for " + user.getUsername() + " to perform this operation!");
    }

    /*
    * Get list of all students from DB.
     */
    @RequestMapping(value = "getstudentlist", method = RequestMethod.GET)
    public ModelAndView getStudentList(@RequestParam("group") String group){
    	
        //List<StudentDto> students = dao.getStudents(group);
        List<StudentDto> students = studentRepository.findByGroup(group);

        return new ModelAndView("list", "results", students);
    }
    
    
    /**
     * Updates the status of a student
     * @param status - String Desired student status setting
     * @return
     */
    @RequestMapping(value = "updatestatus", method = RequestMethod.GET)
    public ModelAndView updateStudentStatus(@RequestParam("status") String status, @RequestParam("studentid") String studentId){
    	        
    	int id = Integer.parseInt(studentId);
    	StudentDto student = studentRepository.getOne(id);
    	student.setStatus(status);
    	studentRepository.save(student);

        return new ModelAndView("redirect:/");
    }
    
    /*
     * Login Spring security controller Ref:
     * http://www.beingjavaguys.com/2014/05/spring-security-authentication-and.
     * html
     * http://docs.spring.io/spring-security/site/docs/4.1.0.RELEASE/reference/
     * htmlsingle/#what-is-acegi-security
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLoginForm(Authentication authentication,@RequestParam(required = false) String authfailed, String logout, String denied, HttpServletRequest request) {
        
    	String message = "Please login";

        if (authfailed != null) {
            message = "Invalid username or password, try again!";
        } else if (logout != null) {
            message = "Logged Out successfully, login again to continue!";
        } else if (denied != null) {
            message = "Access denied for this user!";
        }
        //return new ModelAndView("login_page", "message", message);
        return new ModelAndView("login_page", "message", message);
    }

    /*
     * Login failed
     */
    @RequestMapping("403page")
    public String ge403denied() {
        return "redirect:/login?denied";
    }

}
