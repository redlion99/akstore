package me.smartco.akstore.user.service;

import me.smartco.akstore.common.util.MD5Util;
import me.smartco.akstore.exception.CodeValidateFailedException;
import me.smartco.akstore.user.model.Resource;
import me.smartco.akstore.user.model.Role;
import me.smartco.akstore.user.model.User;
import me.smartco.akstore.user.repository.RoleRepository;
import me.smartco.akstore.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by libin on 15-2-9.
 */
@Component
public class UserService {
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RoleRepository roleRepository;

    public User findById(String userId){
        return userRepository.findOne(userId);
    }

    public User findByUsernameAndPassword(String username,String password){
        return userRepository.findByUsernameAndPasswordAndActive(username, MD5Util.MD5(password), true);
    }

    public boolean checkPermission(String username,String name,String path){
        User user=userRepository.findByUsername(username);
        for(Role role:user.roles()){
            for(Resource resource:role.resources()){
                if(resource.getName().equals(name)){
                    return true;
                }else if(resource.getPath().equals(path)){
                    return true;
                }
            }
        }
        return false;
    }

    public User register(String username,String password,String role,String validateCode) throws CodeValidateFailedException {
        if(getValidateCode(username).equals(validateCode)) {
            User user = userRepository.findByUsername(username);
            if (null == user) {
                user = new User(username, MD5Util.MD5(password));
                user.roles().add(getOrCreateRole(role));
                user = userRepository.save(user);
                return user;
            } else {
                return userRepository.findByUsernameAndPasswordAndActive(username, MD5Util.MD5(password), true);
            }
        }else {
            return null;
        }
    }

    public String getValidateCode(String username){
        return MD5Util.getValidateCode(username+new Date().getMonth()+"bigwin");
    }
    public Role getOrCreateRole(String roleName){
        Role role=roleRepository.findOneByName(roleName);
        if(null==role){
            role=new Role(roleName);
            roleRepository.save(role);
        }
        return role;
    }

    public String getAvailableToken(User user,boolean force){
        if(!force&&null!=user.token()&&null!=user.tokenExpiredAt()&&System.currentTimeMillis()<user.tokenExpiredAt().getTime()){
            return user.token();
        }else {
            user.generateNewToken();
            userRepository.save(user);
            return user.token();
        }
    }
    public String getAvailableToken(User user){
        return getAvailableToken(user,false);
    }

    public User findByUnionId(String unionId){
        return userRepository.findByUnionIdAndActive(unionId,true);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }
}
