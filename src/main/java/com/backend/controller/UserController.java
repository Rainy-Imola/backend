package com.backend.controller;

import com.backend.entity.User;
import com.backend.service.UserService;
import com.backend.utils.msgUtils.Msg;
import com.backend.utils.msgUtils.MsgUtils;
import com.backend.utils.sessionUtils.SessionUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Msg login(@RequestBody JSONObject object) {
        String username = object.getString("username");
        String password = object.getString("password");

        User auth = userService.checkUser(username, password);

        Logger logger = Logger.getLogger(UserController.class);

        if (auth != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", auth.getId());
            jsonObject.put("username", auth.getUsername());
            SessionUtils.setSession(jsonObject);

            JSONObject data = JSONObject.fromObject(auth);
            logger.info("Path: /login, status: success, username: " + username);
            return MsgUtils.makeMsg(MsgUtils.SUCCESS, MsgUtils.LOGIN_SUCCESS_MSG, data);
        } else {
            logger.error("Path: /login, status: fail, username: " + username + " password: " + password);
            return MsgUtils.makeMsg(MsgUtils.LOGIN_USER_ERROR, MsgUtils.LOGIN_USER_ERROR_MSG);
        }
    }

    @GetMapping("/getUsers")
    public List<User> getUsers() { return userService.getUsers(); }

    @PostMapping("/register")
    public Msg register(@RequestBody JSONObject jsonObject) {
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        User auth = userService.findByName(username);

        Logger logger = Logger.getLogger(UserController.class);

        if (auth == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            userService.addUser(user);

            logger.info("Path: /register, status: success, username: " + username);
            return MsgUtils.makeMsg(MsgUtils.SUCCESS, MsgUtils.REGISTER_SUCCESS_MSG);
        }  else {
            logger.error("Path: /register, status: fail, username: " + username);
            return MsgUtils.makeMsg(MsgUtils.REGISTER_ERROR, MsgUtils.REGISTER_ERROR_MSG);
        }
    }

    /*
     * Todo List:
     * Update password
     */
}
