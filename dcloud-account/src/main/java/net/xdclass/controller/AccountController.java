package net.xdclass.controller;

import net.xdclass.controller.request.AccountLoginRequest;
import net.xdclass.controller.request.AccountRegisterRequest;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.service.AccountService;
import net.xdclass.service.FileService;
import net.xdclass.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author 刘森飚
 * @since 2023-01-09
 */
@RestController
@RequestMapping("/api/account/v1")
public class AccountController {

    @Autowired
    private FileService fileService;

    @Autowired
    private AccountService accountService;

    /**
     * 文件上传 最大默认1M
     * @param file
     * @return
     */
    @PostMapping("upload")
    public JsonData uploadUserImg(@RequestPart("file")MultipartFile file) {
        String result = fileService.uploadUserImg(file);
        return result != null ? JsonData.buildSuccess(result) :
                JsonData.buildResult(BizCodeEnum.FILE_UPLOAD_USER_IMG_FAIL);
    }


    /**
     * 用户注册
     * @param registerRequest
     * @return
     */
    @PostMapping("register")
    public JsonData register(@RequestBody AccountRegisterRequest registerRequest) {
        JsonData jsonData = accountService.register(registerRequest);
        return jsonData;
    }


    /**
     * 用户登录
     * @param request
     * @return
     */
    @PostMapping("login")
    public JsonData login(@RequestBody AccountLoginRequest request) {
        JsonData jsonData = accountService.login(request);
        return jsonData;
    }
}