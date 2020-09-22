package spring.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import spring.mvc.entities.User;
import spring.mvc.service.JwtService;
import spring.mvc.service.UserService;

@RestController
@RequestMapping(value = "/rest")
public class UserRestController {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUser() {
		return new ResponseEntity<List<User>>(userService.findAll(), HttpStatus.OK);
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getUserById(@PathVariable(name = "id") int id) {
		User user = userService.findById(id);
		if (user != null) {
			return new ResponseEntity<Object>(user, HttpStatus.OK);
		}
		return new ResponseEntity<Object>("Not Found User", HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ResponseEntity<String> createUser(@RequestBody User user) {
		if (userService.add(user)) {
			return new ResponseEntity<String>("Create!", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<String>("User Existed!", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deteleUserById(@PathVariable(name = "id") int id) {
		userService.delete(id);
		return new ResponseEntity<String>("Delete!", HttpStatus.OK);
	}

	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public ResponseEntity<String> login(HttpServletRequest request, @RequestBody User user) {
		String result = "";
		HttpStatus httpStatus = null;
		try {
			if (userService.checkLogin(user)) {
				result = jwtService.generateTokenLogin(user.getUsername());
				httpStatus = HttpStatus.OK;
			} else {
				result = "Wrong UserId and password";
				httpStatus = HttpStatus.BAD_REQUEST;
			}
		} catch (Exception e) {
			result = "Server Error";
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<String>(result, httpStatus);
	}
}
