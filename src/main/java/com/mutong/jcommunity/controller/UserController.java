package com.mutong.jcommunity.controller;

import com.mutong.jcommunity.annotation.LoginRequired;
import com.mutong.jcommunity.model.User;
import com.mutong.jcommunity.service.FollowService;
import com.mutong.jcommunity.service.LikeService;
import com.mutong.jcommunity.service.UserService;
import com.mutong.jcommunity.util.CommunityConstant;
import com.mutong.jcommunity.util.CommunityUtil;
import com.mutong.jcommunity.util.HostHolder;
import org.apache.maven.surefire.shade.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-02 17:41
 * @time_complexity: O()
 */
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    //上传位置
    @Value("${jcommunity.path.upload}")
    private String uploadPath;
    //域名
    @Value("${jcommunity.path.domain}")
    private String domian;
    //项目名字
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;
    @Autowired
    private FollowService followService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;

    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage(){
        return "/site/setting";
    }

    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model){
        if (headerImage == null){
            model.addAttribute("error","您还没有选择图片");
            return "/site/setting";
        }
        //得到用户上传原始的文件名
        String fileName = headerImage.getOriginalFilename();
        //获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件的格式不正确");
        }
        //生成随机的文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        //确定文件的存放路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            //存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败",e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常", e);
        }
        //更新用户头像路径(web)访问路径
        User user = hostHolder.getUser();
        //http://localhost:8080/jcommunity/user/header
        String headUrl = domian + contextPath + "/user/header/"+fileName;
        userService.updateHeader(user.getId(),headUrl);
        return "redirect:/index";
    }


    @GetMapping("/header/{fileName}")
    public void getHeader(@PathVariable("fileName")String fileName, HttpServletResponse response){
        //服务器存放的路径
        fileName = uploadPath + "/" + fileName;
        //文件的后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //响应图片
        response.setContentType("/image/" + suffix);

        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ){
            byte[] buffer = new byte[5120];
            int b = 0;
            while ((b = fis.read(buffer)) != -1){
                os.write(buffer, 0 , b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败 : "+ e.getMessage());
        }
    }

    /**
     * 个人主页
     * @param userId
     * @param model
     * @return
     */
    @RequestMapping(value = "/profile/{userId}",method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId")int userId, Model model){
        User user = userService.findUserById(userId);
        if (user == null){
            throw new RuntimeException("该用户不存在");
        }
        //用户
        model.addAttribute("user",user);
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount",likeCount);

        //关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount",followeeCount);
        //粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount",followerCount);
        // 是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }


}
