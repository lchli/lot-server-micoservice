package com.lch.lottery.user.service;

import com.lch.lottery.common.model.LotUser;
import com.lch.lottery.user.dao.UserRepository;
import com.lch.lottery.user.dao.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Administrator
 * @version 1.0
 **/
@Service
public class UserService {

    @Autowired
    UserRepository xcUserRepository;
    @Autowired
    RestTemplate restTemplate;


    //根据账号查询xcUser信息
    public UserEntity findXcUserByUsername(String username) {
        return xcUserRepository.findByUsername(username);
    }

    //根据账号查询用户信息
    public LotUser getUserExt(String username) {
        //根据账号查询xcUser信息
        UserEntity xcUser = this.findXcUserByUsername(username);
        if (xcUser == null) {
            return null;
        }
        //用户id
//        String userId = xcUser.getId();
//        //查询用户所有权限
//        List<XcMenu> xcMenus = xcMenuMapper.selectPermissionByUserId(userId);
//
//        //根据用户id查询用户所属公司id
//        XcCompanyUser xcCompanyUser = xcCompanyUserRepository.findByUserId(userId);
//        //取到用户的公司id
//        String companyId = null;
//        if(xcCompanyUser!=null){
//            companyId = xcCompanyUser.getCompanyId();
//        }
//        XcUserExt xcUserExt = new XcUserExt();
//        BeanUtils.copyProperties(xcUser,xcUserExt);
//        xcUserExt.setCompanyId(companyId);
//        //设置权限
//        xcUserExt.setPermissions(xcMenus);
//        return xcUserExt;

        //String response = restTemplate.getForObject("http://lot-file-service/api/file/1", String.class);
        LotUser lotUser=new LotUser();
        lotUser.setUserId(xcUser.uid);
        lotUser.setUsername(xcUser.username);
        lotUser.setPassword(xcUser.pwd);
        lotUser.setPermissions(null);
        return lotUser;

    }


    //根据账号查询用户信息
    public LotUser getUserById(String userId) {
        //根据账号查询xcUser信息
        UserEntity xcUser = xcUserRepository.findByUid(userId);
        if (xcUser == null) {
            return null;
        }
        //用户id
//        String userId = xcUser.getId();
//        //查询用户所有权限
//        List<XcMenu> xcMenus = xcMenuMapper.selectPermissionByUserId(userId);
//
//        //根据用户id查询用户所属公司id
//        XcCompanyUser xcCompanyUser = xcCompanyUserRepository.findByUserId(userId);
//        //取到用户的公司id
//        String companyId = null;
//        if(xcCompanyUser!=null){
//            companyId = xcCompanyUser.getCompanyId();
//        }
//        XcUserExt xcUserExt = new XcUserExt();
//        BeanUtils.copyProperties(xcUser,xcUserExt);
//        xcUserExt.setCompanyId(companyId);
//        //设置权限
//        xcUserExt.setPermissions(xcMenus);
//        return xcUserExt;

        //String response = restTemplate.getForObject("http://lot-file-service/api/file/1", String.class);
        LotUser lotUser=new LotUser();
        lotUser.setUserId(xcUser.uid);
        lotUser.setUsername(xcUser.username);
        lotUser.setPassword(xcUser.pwd);
        lotUser.setPermissions(null);
        return lotUser;

    }

    public UserEntity save(UserEntity userEntity) {
        return xcUserRepository.save(userEntity);
    }

}
