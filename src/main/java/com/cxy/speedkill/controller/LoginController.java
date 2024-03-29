package com.cxy.speedkill.controller;

import com.cxy.speedkill.common.Result;
import com.cxy.speedkill.common.ResultGeekQ;
import com.cxy.speedkill.common.ResultStatus;
import com.cxy.speedkill.service.SpeedKillUserService;
import com.cxy.speedkill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

/**
 * @Auther: cxy
 * @Date: 2019/7/19
 * @Description: 登陆
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    SpeedKillUserService speedKillUserService;

    /**
     * 跳转至登录界面
     * @param model
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin(Model model){
        return "login/index";
    }

    /**
     * 登陆
     * @param response
     * @param loginVo
     * @return
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public ResultGeekQ<Boolean> doLogin(HttpServletResponse response, LoginVo loginVo){
        logger.info(loginVo.toString());

        Boolean result = speedKillUserService.login(response,loginVo);
        return ResultGeekQ.build("登陆成功");
    }

    /**
     * 跳转至注册界面
     * @param model
     * @return
     */
    @RequestMapping("/register")
    public String toRegister(Model model){
        return "login/register";
    }


    /**
     * 生成验证码
     * @param response
     * @return
     */
    @RequestMapping(value = "/verifyCodeRegister", method = RequestMethod.GET)
    @ResponseBody
    public ResultGeekQ<String> getMiaoshaVerifyCod(HttpServletResponse response
    ) {
        ResultGeekQ<String> result = ResultGeekQ.build();
        try {
            BufferedImage image = speedKillUserService.createVerifyCodeRegister();
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return result;
        } catch (Exception e) {
            logger.error("生成验证码错误-----注册:{}", e);
            result.withError(ResultStatus.MIAOSHA_FAIL.getCode(), ResultStatus.MIAOSHA_FAIL.getMessage());
            return result;
        }
    }

    /**
     * 注册
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("doRegister")
    @ResponseBody
    public ResultGeekQ<String> register(HttpServletRequest request, HttpServletResponse response ){
        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");
        String verifyCode = request.getParameter("verifyCode");
        String salt = request.getParameter("salt");

        ResultGeekQ<String> result = ResultGeekQ.build();
        /**
         * 校验验证码
         */
        boolean check = speedKillUserService.checkVerifyCodeRegister(Integer.valueOf(verifyCode));
        if(!check){
            result.withError(ResultStatus.CODE_FAIL.getCode(),ResultStatus.CODE_FAIL.getMessage());
            return result;

        }
        boolean registerInfo  = speedKillUserService.register(response, mobile, password, salt);
        if(!registerInfo){
            result.withError(ResultStatus.RESIGETER_FAIL.getCode(),ResultStatus.RESIGETER_FAIL.getMessage());
            return result;
        }
        return result;
    }

}