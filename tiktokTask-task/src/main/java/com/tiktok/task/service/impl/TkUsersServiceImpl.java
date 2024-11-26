package com.tiktok.task.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.tiktok.common.core.domain.AjaxResult;
import com.tiktok.common.core.domain.entity.SysUser;
import com.tiktok.common.core.redis.RedisCache;
import com.tiktok.common.utils.DateUtils;
import com.tiktok.common.utils.SecurityUtils;
import com.tiktok.common.utils.StringUtils;
import com.tiktok.framework.web.exception.CustomException;
import com.tiktok.system.service.ISysDeptService;
import com.tiktok.system.service.ISysRoleService;
import com.tiktok.system.service.ISysUserService;
import com.tiktok.task.domain.*;
import com.tiktok.task.mapper.*;
import com.tiktok.task.util.AssertionUtils;
import com.tiktok.task.util.InviteCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tiktok.task.service.ITkUsersService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static com.tiktok.common.core.domain.AjaxResult.error;
import static com.tiktok.common.utils.SecurityUtils.getUsername;
import static com.tiktok.task.util.SVIPNotificationGenerator.generateRandomMessages;

/**
 * 用户信息Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-20
 */
@Service
public class TkUsersServiceImpl implements ITkUsersService
{
    @Autowired
    private TkUsersMapper tkUsersMapper;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private TkUserDefaultMapper tkUserDefaultMapper;

    @Autowired
    private TkInvitationMapper tkInvitationMapper;
    @Autowired
    private TkWallettransactionsMapper tkWallettransactionsMapper;

    @Autowired
    private TkWithdrawalsMapper tkWithdrawalsMapper;

    @Autowired
    private TkTasknumMapper tkTasknumMapper;
    @Autowired
    private TkSpecialTaskMapper tkSpecialTaskMapper;
    @Autowired
    private TkTasksMapper tkTasksMapper;

    @Autowired
    private TkTaskAcceptancesMapper tkTaskAcceptancesMapper;
    @Autowired
    private LcSubscriptionMapper lcSubscriptionMapper;

    @Autowired
    private  LcRecordsMapper lcRecordsMapper;
    @Autowired
    private TkSvipSettingMapper tkSvipSettingMapper;
    @Autowired
    private RedisCache redisCache;




    /**
     * 查询用户信息
     *
     * @param uid 用户信息主键
     * @return 用户信息
     */
    @Override
    public TkUsers selectTkUsersByUid(Long uid)
    {
        return tkUsersMapper.selectTkUsersByUid(uid);
    }

    /**
     * 查询用户信息列表
     *
     * @param tkUsers 用户信息
     * @return 用户信息
     */
    @Override
    public List<TkUsers> selectTkUsersList(TkUsers tkUsers) {

        Long userId = SecurityUtils.getUserId();
        // 只能查看自己创建的
        tkUsers.setCreateBy(userId.toString());

        // 超级管理员和管理员可以查看全部
        for (int i = 0; i < SecurityUtils.getLoginUser().getUser().getRoles().size(); i++) {
            if (SecurityUtils.getLoginUser().getUser().getRoles().get(i).getRoleKey().contains("admin")) {
                tkUsers.setCreateBy(null);
            }
        }

        List<TkUsers> tkUsersList = tkUsersMapper.selectTkUsersList(tkUsers);
        for (TkUsers user : tkUsersList) {
            int num = getSubordinateCount(user.getUid()); // 获取下级数量
            user.setTeamsize(num); // 设置下级数量
        }

        if(tkUsersList.size()==0){//尝试模糊查询
            tkUsersList =  tkUsersMapper.selectTkUsersBlurList(tkUsers);
        }

        //获取出用户获利多少
        for (int i1 = 0; i1 < tkUsersList.size(); i1++) {
            Long uid = tkUsersList.get(i1).getUid();

            //获取出来分红记录
            LcRecords lcRecordsBody = new LcRecords();
            lcRecordsBody.setUserId(uid);
            lcRecordsBody.setTitle("Revenue");
            List<LcRecords> lcRecords = lcRecordsMapper.selectLcRecordsList(lcRecordsBody);
            // 使用Java 8 Stream API将amount转换为数字并求和
            double totalAmount = lcRecords.stream()
                    .mapToDouble(record -> {
                        // 使用Optional处理可能为null的情况
                        return Optional.ofNullable(record.getAmount())
                                .map(Double::parseDouble)
                                .orElse(0.0);
                    })
                    .sum();
            //获取项目数量
            LcSubscription lcSubscription = new LcSubscription();
            lcSubscription.setUserId(uid);
            List<LcSubscription> lcSubscriptions = lcSubscriptionMapper.selectLcSubscriptionList(lcSubscription);
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("myProject",lcSubscriptions.size());
            objectObjectHashMap.put("projectIncome",totalAmount);


            //获取出用户一共质押了多少钱
            //获取出来未结束的订单
            lcSubscription.setStatus("0");
            List<LcSubscription> lcSubscriptions2 = lcSubscriptionMapper.selectLcSubscriptionList(lcSubscription);
            BigDecimal bigDecimal = new BigDecimal(0);
            for (int i = 0; i < lcSubscriptions2.size(); i++) {
                BigDecimal purchasePrice = new BigDecimal(lcSubscriptions2.get(i).getPurchasePrice());//单价
                BigDecimal subscriptionCount = new BigDecimal(lcSubscriptions2.get(i).getSubscriptionCount());//数量
                bigDecimal = bigDecimal.add(purchasePrice.multiply(subscriptionCount));
            }
            tkUsersList.get(i1).setInvest(objectObjectHashMap);
            objectObjectHashMap.put("frozenAmount",bigDecimal);
        }



        return tkUsersList;
    }

    private int getSubordinateCount(Long userId) {
        List<TkUsers> subordinates = tkUsersMapper.selectSubordinates(userId);
        int count = subordinates.size();
        for (TkUsers subordinate : subordinates) {
            count += getSubordinateCount(subordinate.getUid());
        }
        return count;
    }


    /**
     * 新增用户信息
     *
     * @param tkUsers 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTkUsers(TkUsers tkUsers)
    {
        Assert.isTrue(tkUsers.getNickname()!=null&&!tkUsers.getNickname().trim().equals(""),"昵称不能为空");
        //判断用户名是否存在
        TkUsers tkUsers3 = new TkUsers();
        tkUsers3.setUsername(tkUsers.getUsername());
        Assert.isTrue(tkUsersMapper.selectTkUsersList(tkUsers3).size()==0,"用户名已存在");


        //判断用户昵称是否存在
        //判断用户名是否存在
        TkUsers tkUsers5 = new TkUsers();
        tkUsers5.setNickname(tkUsers.getNickname());
        Assert.isTrue(tkUsersMapper.selectTkUsersList(tkUsers5).size()==0,"昵称已存在");

        String InviteCode = InviteCodeGenerator.generateInviteCode();//邀请码


        //todo 判断邀请码是否存在
        TkUsers tkUsers1 = new TkUsers();
        tkUsers1.setInvitationCode(tkUsers.getInvitationCode());
        List<TkUsers> tkUsers4 = tkUsersMapper.selectTkUsersList(tkUsers1);
        Assert.isTrue(tkUsers4.size()!=0,"邀请码不存在");
        tkUsers.setReferrerId(tkUsers4.get(0).getUid());

        Long userId = SecurityUtils.getUserId();

        tkUsers.setCreateTime(DateUtils.getNowDate());
        //设置创建人
        tkUsers.setCreateBy(userId.toString());
        tkUsers.setAvatar("/profile/upload/avatar/2024/10/19/lgo_20241019212730A001.jpg");
        tkUsers.setTotareward("0");
        tkUsers.setRegistrationTime(new Date());//注册时间
        tkUsers.setWithdraw("0");
        tkUsers.setUserStatus("1");
        tkUsers.setBalance("0");
        tkUsers.setSvipLevel(0L);
        tkUsers.setNonWithdrawableBalance("0");
        tkUsers.setInvitationCode(InviteCode);

        SysUser user = new SysUser();
        user.setDeptId(110L);
        user.setRoleId(104L);
        user.setPassword(tkUsers.getPassword());
        user.setUserName(tkUsers.getUsername());
        user.setNickName(tkUsers.getNickname());
        deptService.checkDeptDataScope(110L);//分配岗位
        roleService.checkRoleDataScope(104L);//分配角色

        if (!userService.checkUserNameUnique(user))
        {
            System.out.println(error("新增用户'" + user.getUserName() + "'失败，登录账号已存在"));
        }
        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
        {
            System.out.println(error("新增用户'" + user.getUserName() + "'失败，手机号码已存在"));
        }
        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            System.out.println(error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在"));
        }
        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        Assert.isTrue( userService.insertUser(user)>0,"新增用户失败");
        int i = tkUsersMapper.insertTkUsers(tkUsers);
        Long uid = tkUsers.getUid();
        //添加一级
        TkInvitation tkInvitation = new TkInvitation();
        tkInvitation.setInviteeId(uid);
        tkInvitation.setInviterId(tkUsers.getReferrerId());
        tkInvitation.setAmount("0.00");
        tkInvitation.setLevel(1L);
        Assert.isTrue(tkInvitationMapper.insertTkInvitation(tkInvitation)>0,"新增用户失败");

        //添加二级
        if(tkUsers4.get(0).getReferrerId()!=null){
            tkInvitation.setInviterId(tkUsers4.get(0).getReferrerId());
            tkInvitation.setLevel(2L);
            Assert.isTrue( tkInvitationMapper.insertTkInvitation(tkInvitation)>0,"新增用户失败");

            TkUsers tkUsers2 = tkUsersMapper.selectTkUsersByUid(tkUsers4.get(0).getReferrerId());
            if(tkUsers2.getReferrerId()!=null){//添加三级
                tkInvitation.setInviterId(tkUsers2.getReferrerId());
                tkInvitation.setLevel(3L);
                Assert.isTrue( tkInvitationMapper.insertTkInvitation(tkInvitation)>0,"新增用户失败");
            }
        }

        //导入用户默认配置
        TkUserDefault tkUserDefault = tkUserDefaultMapper.selectTkUserDefaultById(1L);
        TkSvipSetting tkSvipSetting = tkSvipSettingMapper.selectTkSvipSettingById(1L);
        TkTasknum tkTasknum = new TkTasknum();
        tkTasknum.setUserId(uid);
        tkTasknum.setExperienceTaskCount(tkSvipSetting.getDailyTaskCount());
        tkTasknum.setNormalTaskCount(tkUserDefault.getRegularTaskCount());
        tkTasknum.setHiddenTaskCount(tkUserDefault.getRegularTaskCount());
        tkTasknum.setTotalCompletedTasks(0L);
        //创建用户限制
        tkTasknumMapper.insertTkTasknum(tkTasknum);
        //去创建系统用户
        return 1;
    }

    /**
     * 修改用户信息
     *
     * @param tkUsers 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTkUsers(TkUsers tkUsers)
    {
        Assert.isTrue(tkUsers.getNickname()!=null&&!tkUsers.getNickname().trim().equals(""),"昵称不能为空");

        //判断用户昵称是否存在
        //判断用户名是否存在
        TkUsers tkUsers5 = new TkUsers();
        tkUsers5.setNickname(tkUsers.getNickname());
        List<TkUsers> tkUsers2 = tkUsersMapper.selectTkUsersList(tkUsers5);
        if(tkUsers2.size()!=0&&!tkUsers2.get(0).getUid().toString().equals(tkUsers.getUid().toString())){
            Assert.isTrue(false,"昵称已存在");
        }
        TkUsers tkUsers1 = tkUsersMapper.selectTkUsersByUid(tkUsers.getUid());

        String username = tkUsers.getUsername();
        Assert.isTrue(username!=null,"登录名不能为空");
        Assert.isTrue(!username.trim().equals(""),"登录名不能为空");
        tkUsers.setUpdateTime(DateUtils.getNowDate());

        //修改用户名和用户密码同步系统表
        String newPassword = SecurityUtils.encryptPassword(tkUsers.getPassword());

       Assert.isTrue(userService.resetUserPwd(tkUsers1.getUsername(), newPassword) > 0,"网络异常");

        SysUser sysUser = userService.selectUserByUserName(tkUsers1.getUsername());
        sysUser.setUserName(username);
        sysUser.setPassword(newPassword);
        Assert.isTrue( userService.updateUser(sysUser) > 0,"网络异常");
        return tkUsersMapper.updateTkUsers(tkUsers);
    }

    /**
     * 批量删除用户信息
     *
     * @param uids 需要删除的用户信息主键
     * @return 结果
     */
    @Override
    public int deleteTkUsersByUids(Long[] uids)
    {
        return tkUsersMapper.deleteTkUsersByUids(uids);
    }

    /**
     * 删除用户信息信息
     *
     * @param uid 用户信息主键
     * @return 结果
     */
    @Override
    public int deleteTkUsersByUid(Long uid)
    {
        return tkUsersMapper.deleteTkUsersByUid(uid);
    }

    @Override
    public List<TkWallettransactions> getWalletRecords(String category) {
        TkWallettransactions tkWallettransactions = new TkWallettransactions();
        tkWallettransactions.setUserid(SecurityUtils.getLoginUser().getUser().getUid());
        tkWallettransactions.setCategory(category);
        return tkWallettransactionsMapper.selectTkWallettransactionsList(tkWallettransactions);
    }

    @Override
    @Transactional
    public AjaxResult updateUser(TkUsers tkUsers) throws CustomException {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        TkUsers tkUser = tkUsersMapper.selectTkUsersByUid(uid);

        if(tkUser.getPaymentPassword()!=null && tkUsers.getUsdtAddress()==null){
            AssertionUtils.isTrue(tkUser.getPaymentPassword().equals(tkUsers.getOldPasswords()),"The old password doesn't match");

        }
        TkUsers tkUsers1 = new TkUsers();
        tkUsers1.setUid(uid);
        tkUsers1.setNickname(tkUsers.getNickname());
        tkUsers1.setPaymentPassword(tkUsers.getPaymentPassword());
        tkUsers1.setAvatar(tkUsers.getAvatar());
        tkUsers1.setUsdtAddress(tkUsers.getUsdtAddress());
        tkUsers1.setBlockchainName(tkUsers.getBlockchainName());
        int i = tkUsersMapper.updateTkUsers(tkUsers1);
        Assert.isTrue(i!=0,"error");
        return AjaxResult.success("Successful");
    }


    @Override
    @Transactional
    public AjaxResult withdraw(String amount,String paymentPassword,String type) {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        TkUsers tkUsers = tkUsersMapper.selectTkUsersByUid(uid);


        Assert.isTrue(tkUsers.getPaymentPassword()!=null,"Set a payment password.");
        Assert.isTrue(tkUsers.getPaymentPassword().trim().equals(paymentPassword.trim()),"Payment password error");

        // 计算提现后的余额
        BigDecimal newBalance = new BigDecimal(0);
        if(type.equals("0")){

            //判断是否有未完成任务
            TkTaskAcceptances tkTaskAcceptances = new TkTaskAcceptances();
            tkTaskAcceptances.setUid(uid);
            List<TkTaskAcceptances> tkTaskAcceptances1 = tkTaskAcceptancesMapper.selectTkTaskAcceptancesList(tkTaskAcceptances);
            for (int i = 0; i < tkTaskAcceptances1.size(); i++) {
                Assert.isTrue(!tkTaskAcceptances1.get(i).getStatus().equals("0"),"Mission not completed");
                Assert.isTrue(!tkTaskAcceptances1.get(i).getStatus().equals("1"),"Mission not completed");
                Assert.isTrue(!tkTaskAcceptances1.get(i).getStatus().equals("4"),"Mission not completed");
            }

            //获取出额度
            TkTasknum tkTasknum = new TkTasknum();
            tkTasknum.setUserId(uid);
            List<TkTasknum> tkTasknums = tkTasknumMapper.selectTkTasknumList(tkTasknum);
            AssertionUtils.isTrue(tkTasknums.size()!=0,"Contact Customer Service");
            Assert.isTrue(tkTasknums.get(0).getExperienceTaskCount()==0,"Mission not completed");
            Assert.isTrue(tkTasknums.get(0).getNormalTaskCount()==0,"Mission not completed");
            // 将字符串转换为 double
            double value = Double.parseDouble(amount);
            Assert.isTrue(value>0,"error");

            TkUserDefault tkUserDefault = tkUserDefaultMapper.selectTkUserDefaultById(1L);
            Assert.isTrue(Double.parseDouble(tkUserDefault.getMinimumWithdrawalAmount())<=value,"Minimum withdrawal ："+ tkUserDefault.getMinimumWithdrawalAmount());

            Assert.isTrue(tkUsers.getUsdtAddress()!=null,"Set the withdrawal address.");

            Assert.isTrue(tkUsers.getWithdraw().equals("0"),"Withdrawal exception, contact customer service");

            // 获取余额和不可提现金额
            String balanceStr = tkUsers.getBalance(); // 余额
            String nonWithdrawableBalanceStr = tkUsers.getNonWithdrawableBalance(); // 不可提现金额
            // 将字符串转换为 BigDecimal
            BigDecimal balance = new BigDecimal(balanceStr);
            BigDecimal nonWithdrawableBalance = new BigDecimal(nonWithdrawableBalanceStr);
            // 计算可提现余额
            BigDecimal withdrawableBalance = balance.subtract(nonWithdrawableBalance);
            // 将 amount 转换为 BigDecimal
            BigDecimal amountToWithdraw = new BigDecimal(amount);
            // 检查余额是否足够
            Assert.isTrue(withdrawableBalance.compareTo(amountToWithdraw) >= 0,"Insufficient funds");

            // 计算提现后的余额
             newBalance = balance.subtract(amountToWithdraw);

            // 更新用户余额
            tkUsers.setBalance(newBalance.toString());
        }else if(type.equals("1")){
            // 获取余额和不可提现金额
            String balanceStr = tkUsers.getInvestmentAmount(); // 余额
            String nonWithdrawableBalanceStr = tkUsers.getFrozenIvestmentAmount(); // 不可提现金额
            // 将字符串转换为 BigDecimal
            BigDecimal balance = new BigDecimal(balanceStr);
            BigDecimal nonWithdrawableBalance = new BigDecimal(nonWithdrawableBalanceStr);
            // 计算可提现余额
            BigDecimal withdrawableBalance = balance.subtract(nonWithdrawableBalance);
            // 将 amount 转换为 BigDecimal
            BigDecimal amountToWithdraw = new BigDecimal(amount);
            // 检查余额是否足够
            Assert.isTrue(withdrawableBalance.compareTo(amountToWithdraw) >= 0,"Insufficient funds");

            // 计算提现后的余额
            newBalance = balance.subtract(amountToWithdraw);

            // 更新用户余额
            tkUsers.setInvestmentAmount(newBalance.toString());

        }


        tkUsersMapper.updateTkUsers(tkUsers);

        TkWithdrawals tkWithdrawals = new TkWithdrawals();
        tkWithdrawals.setAmount(new BigDecimal(amount));
        tkWithdrawals.setUsername(tkUsers.getUsername());
        tkWithdrawals.setUid(uid);
        tkWithdrawals.setTips("0");
        tkWithdrawals.setBlockchainName(tkUsers.getBlockchainName());
        tkWithdrawals.setAddress(tkUsers.getUsdtAddress());
        tkWithdrawals.setWithdrawalTime(new Date());
        tkWithdrawals.setStatus(0L);
        tkWithdrawalsMapper.insertTkWithdrawals(tkWithdrawals);
        //钱包记录
        TkWallettransactions tkWallettransactions = new TkWallettransactions();
        tkWallettransactions.setAmount(new BigDecimal(amount).negate());
        tkWallettransactions.setUserid(uid);
        tkWallettransactions.setTransactionType("withdraw ");
        tkWallettransactions.setTransactionDate(new Date());
        tkWallettransactions.setFundBalance(newBalance);
        tkWallettransactions.setCategory("withdraw");
        tkWallettransactions.setTransactionStatus("待处理");
        tkWallettransactions.setDescription("withdraw："+new BigDecimal(amount));
        tkWallettransactionsMapper.insertTkWallettransactions(tkWallettransactions);

        // 返回新的余额
        return AjaxResult.success("Successful");
    }

    @Override
    public AjaxResult getUserUpgradeVIPMessage() {
        List<String> notifications = generateRandomMessages(5); // 生成 5 条消息
        return AjaxResult.success(notifications);
    }

    @Override
    public AjaxResult GetFundComposition() {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        // 赚取的资金
        TkUsers tkUsers = tkUsersMapper.selectTkUsersByUid(uid);

// 获取出来用户的 amount
        Double amount = tkWallettransactionsMapper.getTotalAmountForTransactionTypeAndUser(uid);

        HashMap<String, String> teamData = tkInvitationMapper.getTeamData(uid);


        //获取出来分红记录
        LcRecords lcRecordsBody = new LcRecords();
        lcRecordsBody.setUserId(uid);
        lcRecordsBody.setTitle("Revenue");
        List<LcRecords> lcRecords = lcRecordsMapper.selectLcRecordsList(lcRecordsBody);
        // 使用Java 8 Stream API将amount转换为数字并求和
        double totalAmount = lcRecords.stream()
                .mapToDouble(record -> {
                    // 使用Optional处理可能为null的情况
                    return Optional.ofNullable(record.getAmount())
                            .map(Double::parseDouble)
                            .orElse(0.0);
                })
                .sum();

        //获取出来分红赚的钱

        HashMap<String, Object> res = new HashMap<>();

        res.put("JobCommission", tkUsers.getTotareward() != null ? tkUsers.getTotareward() : 0);
        res.put("customerRebate", teamData != null && teamData.get("totareward") != null ? teamData.get("totareward") : 0);
        res.put("commissionRebate", amount != null ? amount : 0);
        res.put("ShareholderDividends", totalAmount);

        return AjaxResult.success(res);


    }

    @Override
    @Transactional
    public AjaxResult addSpecialTask(TaskData taskData) {

        for (int i = 0; i < taskData.getUids().size(); i++) {


            //过滤
            TkSpecialTask tkSpecialTask1 = new TkSpecialTask();
            tkSpecialTask1.setUserId(Long.valueOf(taskData.getUids().get(i)));

            List<TkSpecialTask> tkSpecialTasks = tkSpecialTaskMapper.selectTkSpecialTaskList(tkSpecialTask1);
            Long[] idArray = tkSpecialTasks.stream()
                    .map(TkSpecialTask::getId)
                    .toArray(Long[]::new);
            if(idArray.length!=0){
                int i2 = tkSpecialTaskMapper.deleteTkSpecialTaskByIds(idArray);

            }
            for (int i1 = 0; i1 < taskData.getTaskList().size(); i1++) {

                List<TaskData.Task> taskList = taskData.getTaskList();
                Map<String, Long> taskIdCountMap = taskList.stream()
                        .collect(Collectors.groupingBy(TaskData.Task::getTaskId, Collectors.counting())); // 统计每个taskId出现的次数

                // 判断是否有重复的taskId
                boolean hasDuplicate = taskIdCountMap.values().stream().anyMatch(count -> count > 1);

                // 获取重复的taskId
                List<String> duplicateTaskIds = taskIdCountMap.entrySet().stream()
                        .filter(entry -> entry.getValue() > 1)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                Assert.isTrue(!hasDuplicate,"特殊任务不能进行两次添加，重复任务："+duplicateTaskIds+"请移除");

                // 获取任务ID
                String taskId = taskData.getTaskList().get(i1).getTaskId();

                Assert.isTrue( tkTasksMapper.selectTkTasksById(Long.valueOf(taskId))!=null,
                        "这个任务id不存在-->"+taskId);

                TkSpecialTask tkSpecialTask = new TkSpecialTask();
                tkSpecialTask.setUserId(Long.valueOf(taskData.getUids().get(i)));
                tkSpecialTask.setTaskId(Long.valueOf(taskId));
                tkSpecialTask.setStatus("0");
                if(taskData.getTaskList().get(i1).getCount().equals("0")){
                    tkSpecialTask.setStatus("1");
                }

                tkSpecialTask.setTriggerCount(Long.valueOf(taskData.getTaskList().get(i1).getCount()));


                //二次兜底  不能重复下特殊任务
                String uid = taskData.getUids().get(i);//用户id
                TkTaskAcceptances tkTaskAcceptances = new TkTaskAcceptances();
                tkTaskAcceptances.setUid(Long.valueOf(uid));
                tkTaskAcceptances.setTaskId(Long.valueOf(taskId));
                List<TkTaskAcceptances> tkTaskAcceptances1 = tkTaskAcceptancesMapper.selectTkTaskAcceptancesList(tkTaskAcceptances);
                Assert.isTrue(tkTaskAcceptances1.size()==0||tkSpecialTask.getTriggerCount()==0,taskId+"这个特殊任务客户已经做过了");



                tkSpecialTaskMapper.insertTkSpecialTask(tkSpecialTask);
            }
        }
        return AjaxResult.success("添加成功");
    }

    @Override
    public int SignIn(TkUsers tkUsers) {
        Assert.isTrue(tkUsers.getInvitationCode()!=null,"Fill in the invitation code");
        Assert.isTrue(tkUsers.getInvitationCode()!="","Fill in the invitation code");
        Assert.isTrue(tkUsers.getNickname()!=null&&!tkUsers.getNickname().trim().equals(""),"Nicknames cannot be empty");
        //判断用户名是否存在
        TkUsers tkUsers3 = new TkUsers();
        tkUsers3.setUsername(tkUsers.getUsername());
        Assert.isTrue(tkUsersMapper.selectTkUsersList(tkUsers3).size()==0,"The user name already exists");


        //判断用户昵称是否存在
        //判断用户名是否存在
        TkUsers tkUsers5 = new TkUsers();
        tkUsers5.setNickname(tkUsers.getNickname());
        Assert.isTrue(tkUsersMapper.selectTkUsersList(tkUsers5).size()==0,"Nickname already exists");

        String InviteCode = InviteCodeGenerator.generateInviteCode();//邀请码


        //todo 判断邀请码是否存在
        TkUsers tkUsers1 = new TkUsers();
        tkUsers1.setInvitationCode(tkUsers.getInvitationCode());
        List<TkUsers> tkUsers4 = tkUsersMapper.selectTkUsersList(tkUsers1);
        Assert.isTrue(tkUsers4.size()!=0,"The invitation code does not exist");
        tkUsers.setReferrerId(tkUsers4.get(0).getUid());

       // Long userId = SecurityUtils.getUserId();

        tkUsers.setCreateTime(DateUtils.getNowDate());
        //设置创建人
        tkUsers.setCreateBy("1");
        tkUsers.setAvatar("/profile/upload/avatar/2024/10/19/lgo_20241019212730A001.jpg");
        tkUsers.setTotareward("0");
        tkUsers.setRegistrationTime(new Date());//注册时间
        tkUsers.setWithdraw("0");
        tkUsers.setUserStatus("1");
        tkUsers.setCreateBy(tkUsers4.get(0).getCreateBy());
        tkUsers.setBalance("0");
        tkUsers.setSvipLevel(0L);
        tkUsers.setNonWithdrawableBalance("0");
        tkUsers.setInvitationCode(InviteCode);


        SysUser user = new SysUser();
        user.setDeptId(110L);
        user.setRoleId(104L);
        user.setPassword(tkUsers.getPassword());
        user.setUserName(tkUsers.getUsername());
        user.setNickName(tkUsers.getNickname());
//        deptService.checkDeptDataScope2(110L);//分配岗位
//        roleService.checkRoleDataScope2(104L);//分配角色

        if (!userService.checkUserNameUnique(user))
        {
            System.out.println(error("新增用户'" + user.getUserName() + "'失败，登录账号已存在"));
        }
        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
        {
            System.out.println(error("新增用户'" + user.getUserName() + "'失败，手机号码已存在"));
        }
        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            System.out.println(error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在"));
        }
//        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        Assert.isTrue( userService.insertUser(user)>0,"Network exception");
        int i = tkUsersMapper.insertTkUsers(tkUsers);
        Long uid = tkUsers.getUid();
        //添加一级
        TkInvitation tkInvitation = new TkInvitation();
        tkInvitation.setInviteeId(uid);
        tkInvitation.setInviterId(tkUsers.getReferrerId());
        tkInvitation.setAmount("0.00");
        tkInvitation.setLevel(1L);
        Assert.isTrue(tkInvitationMapper.insertTkInvitation(tkInvitation)>0,"Network exception");

        //添加二级
        if(tkUsers4.get(0).getReferrerId()!=null){
            tkInvitation.setInviterId(tkUsers4.get(0).getReferrerId());
            tkInvitation.setLevel(2L);
            Assert.isTrue( tkInvitationMapper.insertTkInvitation(tkInvitation)>0,"Network exception");

            TkUsers tkUsers2 = tkUsersMapper.selectTkUsersByUid(tkUsers4.get(0).getReferrerId());
            if(tkUsers2.getReferrerId()!=null){//添加三级
                tkInvitation.setInviterId(tkUsers2.getReferrerId());
                tkInvitation.setLevel(3L);
                Assert.isTrue( tkInvitationMapper.insertTkInvitation(tkInvitation)>0,"Network exception");
            }
        }

        //导入用户默认配置
        TkUserDefault tkUserDefault = tkUserDefaultMapper.selectTkUserDefaultById(1L);

        TkSvipSetting tkSvipSetting = tkSvipSettingMapper.selectTkSvipSettingById(1L);

        TkTasknum tkTasknum = new TkTasknum();
        tkTasknum.setUserId(uid);
        tkTasknum.setExperienceTaskCount(tkSvipSetting.getDailyTaskCount());
        tkTasknum.setNormalTaskCount(tkUserDefault.getRegularTaskCount());
        tkTasknum.setHiddenTaskCount(tkUserDefault.getRegularTaskCount());
        tkTasknum.setTotalCompletedTasks(0L);
        //创建用户限制
        tkTasknumMapper.insertTkTasknum(tkTasknum);
        //去创建系统用户
        return 1;
    }

    @Override
    public int LanguageSetting(String language) {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        if (uid != null && StringUtils.isNotEmpty(language)) {
            // 将语言设置存储到Redis中，并设置有效期为一个月
            redisCache.setCacheObject("user:language:" + uid, language, 30, TimeUnit.DAYS);
        }
        return 1;
    }

    @Override
    public AjaxResult getLanguage() {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        if (uid != null) {
            // 从Redis中获取用户的语言设置
            return AjaxResult.success(redisCache.getCacheObject("user:language:" + uid));
        }
        return null;
    }
}
