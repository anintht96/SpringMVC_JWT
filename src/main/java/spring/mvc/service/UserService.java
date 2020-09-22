package spring.mvc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import spring.mvc.entities.User;

@Service
public class UserService {

	public static List<User> listUsers = new ArrayList<User>();

	static {
		String[] roleUser = { "ROLE_USER" };
		String[] roleAdmin = { "ROLE_ADMIN" };
		User user1 = new User(0, "user", "123456", roleUser);
		User user2 = new User(1, "admin", "123456", roleAdmin);

		listUsers.add(user1);
		listUsers.add(user2);
	}

	public List<User> findAll() {
		return listUsers;
	}

	public User findById(int id) {
		for (User user : listUsers) {
			if (user.getId() == id) {
				return user;
			}
		}
		return null;
	}

	public boolean add(User user) {
		for (User userExist : listUsers) {
			if (user.getId() == userExist.getId() || user.getUsername().equalsIgnoreCase((userExist.getUsername()))) {
				return false;
			}
		}
		listUsers.add(user);
		return true;
	}

	public void delete(int id) {
		listUsers.removeIf(User -> User.getId() == id);
	}

	public User loadUserByUsername(String username) {
		for (User user : listUsers) {
			if (user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}

	public boolean checkLogin(User user) {
		for (User userExist : listUsers) {
			if (userExist.getUsername().equals(user.getUsername())
					&& userExist.getPassword().equals(user.getPassword())) {
				return true;
			}
		}
		return false;
	}
}
