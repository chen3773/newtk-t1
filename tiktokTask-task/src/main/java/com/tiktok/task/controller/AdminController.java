package com.tiktok.task.controller;

import com.tiktok.common.core.controller.BaseController;
import com.tiktok.common.core.domain.AjaxResult;
import com.tiktok.common.core.page.TableDataInfo;
import com.tiktok.task.domain.ov.JuniorUserOV;
import com.tiktok.task.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController {
    @Autowired
    private AdminService adminService;
    @GetMapping("/getHomePage")
    public AjaxResult HomePage(){
        return  adminService.HomePage();
    }

    /**
     * 给用户加款 或者
     * Add and deduct
     */

    @GetMapping("/AddAndDeduct")
    public AjaxResult AddAndDeduct(String amount,String withdraw,String add,String uid,String rebate,String type){
        return  adminService.AddAndDeduct(amount,withdraw,add,uid,rebate,type);
    }

    /**
     * 获取有多少个未审核任务
     */
    @GetMapping("/pendingTask")
    public AjaxResult pendingTask(){
        return adminService.pendingTask();
    }

    /**
     * 充值错误回滚
     *
     */

    @GetMapping("/TakeOut")
    public AjaxResult TakeOut(Long id){

        return  adminService.TakeOut(id);
    }

    /**
     * 查看用户下级
     */
    @GetMapping("/ViewSubordinates")
    public TableDataInfo ViewSubordinates(Long id){
        startPage();
        List<JuniorUserOV> juniorUserOVS = adminService.ViewSubordinates(id);
        return getDataTable(juniorUserOVS);
    }
    /**
     * 获取对应VIP数量
     */

}
