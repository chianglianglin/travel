package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {

    /**
     * 根據用戶名查詢用戶信息
     * @param username
     * @return
     */
    public User findByUsername(String username);

    /**
     * 用戶保存
     * @param user
     */
    public void save(User user);

    User findByCode(String code);

    void updateStatus(User user);

    User findByUsernameAndPassword(String username, String password);
}
