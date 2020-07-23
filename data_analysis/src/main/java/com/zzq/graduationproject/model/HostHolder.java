package com.zzq.graduationproject.model;

import org.springframework.stereotype.Component;

/**
 * @author zzq
 */
@Component
public class HostHolder {

    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUsers(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
