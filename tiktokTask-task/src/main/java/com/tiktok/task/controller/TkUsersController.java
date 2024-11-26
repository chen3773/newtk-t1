package com.tiktok.task.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tiktok.common.constant.Constants;
import com.tiktok.common.core.domain.entity.SysUser;
import com.tiktok.common.core.domain.model.LoginBody;
import com.tiktok.common.core.domain.model.LoginUser;
import com.tiktok.common.utils.SecurityUtils;
import com.tiktok.framework.web.exception.CustomException;
import com.tiktok.framework.web.service.SysLoginService;
import com.tiktok.framework.web.service.TokenService;
import com.tiktok.system.service.ISysUserService;
import com.tiktok.task.domain.*;
import com.tiktok.task.mapper.TkUserDefaultMapper;
import com.tiktok.task.mapper.TkUsersMapper;
import com.tiktok.task.service.ITkSvipSettingService;
import com.tiktok.task.service.impl.TkTasknumServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tiktok.common.annotation.Log;
import com.tiktok.common.core.controller.BaseController;
import com.tiktok.common.core.domain.AjaxResult;
import com.tiktok.common.enums.BusinessType;
import com.tiktok.task.service.ITkUsersService;
import com.tiktok.common.utils.poi.ExcelUtil;
import com.tiktok.common.core.page.TableDataInfo;



/**
 * 用户信息Controller
 *
 * @author ruoyi
 * @date 2024-09-20
 */
@RestController
@RequestMapping("/task/users")
public class TkUsersController extends BaseController
{
    @Autowired
    private ITkUsersService tkUsersService;
    @Autowired
    private TkUsersMapper tkUsersMapper;
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private TkTasknumServiceImpl tkTasknumService;

    @Autowired
    private ITkSvipSettingService tkSvipSettingService;

    @Autowired
    private TkUserDefaultMapper tkUserDefaultMapper;


    /**
     * 查询用户信息列表
     */
    @PreAuthorize("@ss.hasPermi('task:users:list')")
    @GetMapping("/list")
    public TableDataInfo list(TkUsers tkUsers)
    {
        startPage();

        List<TkUsers> list = tkUsersService.selectTkUsersList(tkUsers);





        return getDataTable(list);
    }

    /**
     * 导出用户信息列表
     */
    @PreAuthorize("@ss.hasPermi('task:users:export')")
    @Log(title = "用户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TkUsers tkUsers)
    {
        List<TkUsers> list = tkUsersService.selectTkUsersList(tkUsers);
        ExcelUtil<TkUsers> util = new ExcelUtil<TkUsers>(TkUsers.class);
        util.exportExcel(response, list, "用户信息数据");
    }

    /**
     * 获取用户信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('task:users:query')")
    @GetMapping(value = "/{uid}")
    public AjaxResult getInfo(@PathVariable("uid") Long uid)
    {
        return success(tkUsersService.selectTkUsersByUid(uid));
    }

    /**
     * 新增用户信息
     */
    @PreAuthorize("@ss.hasPermi('task:users:add')")
    @Log(title = "用户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TkUsers tkUsers)
    {
        return toAjax(tkUsersService.insertTkUsers(tkUsers));
    }

    /**
     * 修改用户信息
     */
    @PreAuthorize("@ss.hasPermi('task:users:edit')")
    @Log(title = "用户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TkUsers tkUsers)
    {
        return toAjax(tkUsersService.updateTkUsers(tkUsers));
    }

    /**
     * 删除用户信息
     */
    @PreAuthorize("@ss.hasPermi('task:users:remove')")
    @Log(title = "用户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{uids}")
    public AjaxResult remove(@PathVariable Long[] uids)
    {
        return toAjax(tkUsersService.deleteTkUsersByUids(uids));
    }


    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        List<TkUsers> tkUsers = new ArrayList<>();
        if(loginBody.getLoginChannel().equals("tk")){
            //通过账号获取出平台授权账号
            TkUsers tkUser = new TkUsers();
            tkUser.setUsername(loginBody.getUsername());
            tkUsers = tkUsersMapper.selectTkUsersList(tkUser);
            Assert.isTrue(tkUsers.size()!=0,"Password error");
            Assert.isTrue(tkUsers.get(0).getUserStatus().equals("0"),"Account exception");
            Assert.isTrue(tkUsers.get(0).getDeleted().equals("0"),"Account exception");
            Assert.isTrue(tkUsers.get(0).getPassword().equals(loginBody.getPassword()),"Password error");
        }
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(tkUsers.get(0).getUsername(), tkUsers.get(0).getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     */

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getUserInfo")
    public AjaxResult getInfo()
    {
        UserInfo userInfo = new UserInfo();
        String username = SecurityUtils.getUsername();
        TkUsers newtkUsers = new TkUsers();
        newtkUsers.setUsername(username);
        TkUsers tkUsers = tkUsersMapper.selectTkUsersList(newtkUsers).get(0);
        if (tkUsers != null){

            newtkUsers.setNickname(tkUsers.getNickname());
            newtkUsers.setSvipLevel(tkUsers.getSvipLevel());
            newtkUsers.setBalance(tkUsers.getBalance());
            newtkUsers.setNonWithdrawableBalance(tkUsers.getNonWithdrawableBalance());
            newtkUsers.setUsdtAddress(tkUsers.getUsdtAddress());
            newtkUsers.setReferrerId(tkUsers.getReferrerId());
            newtkUsers.setUid(SecurityUtils.getLoginUser().getUser().getUid());
            newtkUsers.setTotareward(tkUsers.getTotareward());
            newtkUsers.setAvatar(tkUsers.getAvatar());
            newtkUsers.setInvitationCode(tkUsers.getInvitationCode());
            newtkUsers.setPaymentPassword("1");
            newtkUsers.setFrozenIvestmentAmount(tkUsers.getFrozenIvestmentAmount());
            newtkUsers.setInvestmentAmount(tkUsers.getInvestmentAmount());
            userInfo.setTkUsers(newtkUsers);
            if(tkUsers.getPaymentPassword()==null || tkUsers.getPaymentPassword().trim().equals("")){
                newtkUsers.setPaymentPassword("0");
            }
        }
        TkTasknum tkTasknum = new TkTasknum();
        tkTasknum.setUserId(tkUsers.getUid());
        TkTasknum tkTasknums = tkTasknumService.selectTkTasknumList(tkTasknum).get(0);


        TkUserDefault tkUserDefault = tkUserDefaultMapper.selectTkUserDefaultById(1L);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("userInfo", userInfo);
        ajax.put("AvailableTasks",tkTasknums.getNormalTaskCount()+tkTasknums.getExperienceTaskCount());
        ajax.put("WorkIncome",tkUsers.getTotareward());
        ajax.put("ChainName",tkUsers.getBlockchainName());
        return ajax;
    }


    /**
     * 修改用户信息
     * @return
     */
    @PostMapping("/updateUser")
    public AjaxResult updateUser(@RequestBody TkUsers tkUsers) throws CustomException {

        return tkUsersService.updateUser(tkUsers);
    }


    @GetMapping("getWalletRecords")
    public TableDataInfo getWalletRecords(String category)
    {
        startPage();
        return getDataTable(tkUsersService.getWalletRecords(category));
    }

    /**
     * 用户提现
     */
    @PostMapping("/withdraw")
    public AjaxResult withdraw(String amount,String paymentPassword,String type){
        Assert.isTrue(amount!=null,"error");
        Assert.isTrue(paymentPassword!=null,"The security password cannot be empty!");
        return tkUsersService.withdraw(amount,paymentPassword,type);
    }

    @GetMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword)
    {
        String p = newPassword;//后台显示不加密
        LoginUser loginUser = getLoginUser();
        Long uid = loginUser.getUser().getUid();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password))
        {
            return error("Password change failed, incorrect old password.");
        }
        if (SecurityUtils.matchesPassword(newPassword, password))
        {
            return error("The new password cannot be the same as the old password.");
        }
        newPassword = SecurityUtils.encryptPassword(newPassword);
        if (userService.resetUserPwd(userName, newPassword) > 0)
        {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(newPassword);
            tokenService.setLoginUser(loginUser);
            //更新tk用户信息
            TkUsers tkUsers = tkUsersService.selectTkUsersByUid(uid);
            tkUsers.setPassword(p);
            tkUsersService.updateTkUsers(tkUsers);
            return success();
        }


        return error("Network error");
    }

    /**
     * 公告获取
     * @return
     */
    @GetMapping("/getUserUpgradeVIPMessage")
    public AjaxResult getUserUpgradeVIPMessage(){
        return tkUsersService.getUserUpgradeVIPMessage();
    }

    /**
     * 获取出用户剩余任务
     */

    /**
     * 获取资金构成部分
     *
     * 基金构成：
     * 工作佣金（USDT）：为吸引新客户而提供的返现（USDT）：
     * 下属佣金返现（USDT）：股东分红（USDT）：
     */
    @GetMapping("/GetFundComposition")
    public AjaxResult GetFundComposition(){
        return tkUsersService.GetFundComposition();
    }




    /**
     * 查询svip默认配置列表
     */
    @GetMapping("/SvipSetting")
    public TableDataInfo SvipSetting(TkSvipSetting tkSvipSetting)
    {
        List<TkSvipSetting> list = tkSvipSettingService.selectTkSvipSettingList(tkSvipSetting);
        return getDataTable(list);
    }

    /**
     * 给用户批量添加特殊任务
     */
    @PostMapping("/addSpecialTask")
    public AjaxResult addSpecialTask(@RequestBody TaskData taskData)
    {

        return tkUsersService.addSpecialTask(taskData);
    }

    /**
     * 注册用户
     * Sign in
     */
    @PostMapping("/SignIn")
    public AjaxResult SignIn(@RequestBody TkUsers tkUsers)
    {

        return toAjax(tkUsersService.SignIn(tkUsers));
    }

    /**
     * 锁定用户系统语言
     */
    @GetMapping("/LanguageSetting")
    public AjaxResult LanguageSetting(String Language)
    {
        return toAjax(tkUsersService.LanguageSetting(Language));
    }

    /**
     * 锁定用户系统语言
     */
    @GetMapping("/getLanguage")
    public AjaxResult getLanguage()
    {
        return tkUsersService.getLanguage();
    }



}
