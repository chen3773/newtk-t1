package com.tiktok.task.service.impl;

import com.tiktok.common.core.domain.AjaxResult;
import com.tiktok.common.core.domain.model.LoginUser;
import com.tiktok.common.utils.SecurityUtils;
import com.tiktok.task.domain.*;
import com.tiktok.task.domain.ov.JuniorUserOV;
import com.tiktok.task.mapper.*;
import com.tiktok.task.service.AdminService;
import com.tiktok.task.util.OrderNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private TkUsersMapper tkUsersMapper;
    @Autowired
    private TkWallettransactionsMapper tkWallettransactionsMapper;
    @Autowired
    private TkWithdrawalsMapper tkWithdrawalsMapper;
    @Autowired
    private TkInvitationMapper tkInvitationMapper;
    @Autowired
    private TkRoyaltySettingMapper tkRoyaltySettingMapper;
    @Autowired
    private TkTaskAcceptancesMapper tkTaskAcceptancesMapper;

    @Override
    public AjaxResult HomePage() {
        Long userId = SecurityUtils.getUserId();

        //超级管理员和管理员可以查看全部
        boolean bool = false;
        for (int i = 0; i < SecurityUtils.getLoginUser().getUser().getRoles().size(); i++) {
            if( SecurityUtils.getLoginUser().getUser().getRoles().get(i).getRoleKey().contains("admin")){
                userId = null;
                bool = true;
            }
        }
        HashMap<String, Object> stringObjectHashMap = adminMapper.HomePage(userId);
        if(bool){//是管理员 去查询出代业绩数据
            List<HashMap<String, Object>> agencyPerformance = adminMapper.getAgencyPerformance();
            stringObjectHashMap.put("AgencyPerformance",agencyPerformance);
        }

        //查询出对应VIP数量
        List< HashMap<String,Object>> stringObjectHashMap1 = adminMapper.getvipNum(userId);
        stringObjectHashMap.put("vipNum",stringObjectHashMap1);
        return AjaxResult.success(stringObjectHashMap);
    }

    @Override
    @Transactional
    public AjaxResult AddAndDeduct(String amount,String withdraw,String add,String uid,String rebate,String type) {
        Assert.isTrue(!(add.equals("no")&&rebate.equals("yes")),"只有加款才能勾选返利");
        String orderNumber = OrderNumberGenerator.generateOrderNumber();

        TkUsers tkUsers = tkUsersMapper.selectTkUsersByUid(Long.valueOf(uid));
        if(type.equals("0")){
            String str = "top-up:";

            BigDecimal bigDecimal = new BigDecimal(amount);
            if(withdraw.equals("yes")){
                if(add.equals("yes")){
                    tkUsers.setBalance(new BigDecimal(tkUsers.getBalance())
                            .add(bigDecimal).toString());
                }else {
                    tkUsers.setBalance(new BigDecimal(tkUsers.getBalance())
                            .subtract(bigDecimal).toString());
                }
            }else if(withdraw.equals("no")){
                if(add.equals("yes")){
                    tkUsers.setBalance(new BigDecimal(tkUsers.getBalance())
                            .add(bigDecimal).toString());
                    tkUsers.setNonWithdrawableBalance(new BigDecimal(tkUsers.getNonWithdrawableBalance())
                            .add(bigDecimal).toString());
                }else {
                    tkUsers.setBalance(new BigDecimal(tkUsers.getBalance())
                            .subtract(bigDecimal).toString());
                    tkUsers.setNonWithdrawableBalance(new BigDecimal(tkUsers.getNonWithdrawableBalance())
                            .subtract(bigDecimal).toString());
                }

            }
            tkUsersMapper.updateTkUsers(tkUsers);

            if(add.equals("no")){
                bigDecimal = new BigDecimal(amount).negate();
                str = "Deduct money:";
            }
            //添加记录
            TkWallettransactions tkWallettransactions = new TkWallettransactions();
            tkWallettransactions.setUserid(Long.valueOf(uid));
            tkWallettransactions.setTransactionType(str);
            tkWallettransactions.setAmount(bigDecimal);
            tkWallettransactions.setTransactionDate(new Date());
            tkWallettransactions.setOrderNumber(orderNumber);
            tkWallettransactions.setWithdraw(withdraw);
            tkWallettransactions.setFundBalance(new BigDecimal(tkUsers.getBalance()));
            tkWallettransactions.setDescription(str+amount);
            tkWallettransactions.setCategory("income");
            tkWallettransactions.setTransactionStatus("已完成");
            tkWallettransactionsMapper.insertTkWallettransactions(tkWallettransactions);


            if(rebate.equals("yes")){
                //分成
                //开始分佣
                TkRoyaltySetting tkRoyaltySetting = tkRoyaltySettingMapper.selectTkRoyaltySettingById(1L);//分佣比例
                //获取出上级邀请人
                TkInvitation tkInvitation = new TkInvitation();
                tkInvitation.setInviteeId(Long.valueOf(uid));
                List<TkInvitation> tkInvitations = tkInvitationMapper.selectTkInvitationList(tkInvitation);
                for (int i = 0; i < tkInvitations.size(); i++) {
                    Long inviterId = tkInvitations.get(i).getInviterId();//上级id
                    Long id2 = tkInvitations.get(i).getId();
                    Long level = tkInvitations.get(i).getLevel();
                    BigDecimal rewardAmount = new BigDecimal(amount); // 假设这个方法返回一个 BigDecimal
                    String royalty = "";


                    // 根据 level 获取 royalty
                    if (level == 1) {
                        royalty = tkRoyaltySetting.getFirst();
                    } else if (level == 2) {
                        royalty = tkRoyaltySetting.getTwo();
                    } else if (level == 3) {
                        royalty = tkRoyaltySetting.getThree();
                    }
                    // 将 royalty 转换为 BigDecimal
                    BigDecimal royaltyAmount = new BigDecimal(royalty);
                    // 计算 rewardAmount * royaltyAmount / 100    添加佣金记录
                    BigDecimal result = rewardAmount.multiply(royaltyAmount).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);

                    //给用户添加余额
                    TkUsers tkUser = tkUsersMapper.selectTkUsersByUid(inviterId);
                    BigDecimal balance = new BigDecimal(tkUser.getBalance()).add(result);
                    tkUser.setBalance(balance.toString());
                    Assert.isTrue(tkUsersMapper.updateTkUsers(tkUser)!=0,"网络异常...");


                    //添加记录
                    TkWallettransactions tkWallettransaction = new TkWallettransactions();
                    tkWallettransaction.setUserid(inviterId);
                    tkWallettransaction.setFundBalance(balance);
                    tkWallettransaction.setCategory("income");
                    tkWallettransaction.setDescription("#"+uid+"Top-up bonus");
                    tkWallettransaction.setAmount(result);
                    tkWallettransaction.setTransactionStatus("已完成");
                    tkWallettransaction.setUpdatedAt(new Date());
                    tkWallettransaction.setTransactionType("income");
                    tkWallettransaction.setTransactionDate(new Date());
                    tkWallettransaction.setOrderNumber(orderNumber);
                    tkWallettransaction.setWithdraw("yes");
                    Assert.isTrue(tkWallettransactionsMapper.insertTkWallettransactions(tkWallettransaction)!=0,"网络异常...");
                    //更新邀请关系表金额
                    tkInvitations.get(i).setAmount(new BigDecimal(tkInvitations.get(i).getAmount()).add(result).toString());
                    tkInvitationMapper.updateTkInvitation(tkInvitations.get(i));
                }
            }
        }else if(type.equals("1")){
            String str = "top-up:";

            BigDecimal bigDecimal = new BigDecimal(amount);
            if(withdraw.equals("yes")){
                if(add.equals("yes")){
                    tkUsers.setInvestmentAmount(new BigDecimal(tkUsers.getInvestmentAmount())
                            .add(bigDecimal).toString());
                }else {
                    tkUsers.setInvestmentAmount(new BigDecimal(tkUsers.getInvestmentAmount())
                            .subtract(bigDecimal).toString());
                }
            }else if(withdraw.equals("no")){
                if(add.equals("yes")){
                    tkUsers.setInvestmentAmount(new BigDecimal(tkUsers.getInvestmentAmount())
                            .add(bigDecimal).toString());
                    tkUsers.setFrozenIvestmentAmount(new BigDecimal(tkUsers.getFrozenIvestmentAmount())
                            .add(bigDecimal).toString());
                }else {
                    tkUsers.setInvestmentAmount(new BigDecimal(tkUsers.getInvestmentAmount())
                            .subtract(bigDecimal).toString());
                    tkUsers.setFrozenIvestmentAmount(new BigDecimal(tkUsers.getFrozenIvestmentAmount())
                            .subtract(bigDecimal).toString());
                }

            }
            tkUsersMapper.updateTkUsers(tkUsers);

            if(add.equals("no")){
                bigDecimal = new BigDecimal(amount).negate();
                str = "Deduct money:";
            }
            //添加记录
            TkWallettransactions tkWallettransactions = new TkWallettransactions();
            tkWallettransactions.setUserid(Long.valueOf(uid));
            tkWallettransactions.setTransactionType(str);
            tkWallettransactions.setAmount(bigDecimal);
            tkWallettransactions.setTransactionDate(new Date());
            tkWallettransactions.setOrderNumber(orderNumber);
            tkWallettransactions.setWithdraw(withdraw);
            tkWallettransactions.setFundBalance(new BigDecimal(tkUsers.getBalance()));
            tkWallettransactions.setDescription(str+amount);
            tkWallettransactions.setCategory("income");
            tkWallettransactions.setTransactionStatus("已完成");
            tkWallettransactionsMapper.insertTkWallettransactions(tkWallettransactions);
        }

        return AjaxResult.success(tkUsers);
    }

    @Override
    @Transactional
    public AjaxResult TakeOut(Long id) {
        TkWallettransactions tkWallettransactions = tkWallettransactionsMapper.selectTkWallettransactionsById(id);
        Assert.isTrue(tkWallettransactions.getTransactionType().equals("top-up:"),"非充值订单不能扣除回滚操作");
        Assert.isTrue(tkWallettransactions.getOrderNumber()!=null,"这笔交易不能回滚");
        String orderNumber = tkWallettransactions.getOrderNumber();
        TkWallettransactions tkWallettransactionsBody = new TkWallettransactions();
        tkWallettransactionsBody.setOrderNumber(orderNumber);




        //相关订单号
        List<TkWallettransactions> tkWallettransactionsDm = tkWallettransactionsMapper.selectTkWallettransactionsList(tkWallettransactionsBody);
        for (int i = 0; i < tkWallettransactionsDm.size(); i++) {
            Assert.isTrue(!tkWallettransactionsDm.get(i).getTransactionType().equals("TakeOut"),"不能进行多次扣除");
        }
        Assert.isTrue(tkWallettransactionsDm.size()<=4,"不能进行多次扣除");


        List<Long> userIds = tkWallettransactionsDm.stream()
                .map(TkWallettransactions::getUserid)
                .collect(Collectors.toList());


        // 过滤充值的订单
        List<TkWallettransactions> filteredTransactions = tkWallettransactionsDm.stream()
                .filter(transaction -> transaction.getTransactionType().equals("top-up:"))
                .collect(Collectors.toList());

        // 获取符合条件的数据
        TkWallettransactions result = null;
        if (filteredTransactions.size() > 0) {
            result = filteredTransactions.get(0);
        }

        for (int i = 0; i < tkWallettransactionsDm.size(); i++) {
            BigDecimal amount = tkWallettransactionsDm.get(i).getAmount();//交易金额
            String withdraw = tkWallettransactionsDm.get(i).getWithdraw();//是否可提现
            Long userid = tkWallettransactionsDm.get(i).getUserid();//操作用户id

            if(withdraw.equals("yes")){
                //扣除用户余额
                TkUsers tkUser = tkUsersMapper.selectTkUsersByUid(userid);
                BigDecimal balance = new BigDecimal(tkUser.getBalance()).add(amount.negate());
                tkUser.setBalance(balance.toString());
                Assert.isTrue(tkUsersMapper.updateTkUsers(tkUser)!=0,"网络异常...");

                //添加记录
                TkWallettransactions tkWallettransaction = new TkWallettransactions();
                tkWallettransaction.setUserid(userid);
                tkWallettransaction.setFundBalance(balance);
                tkWallettransaction.setCategory("income");
                tkWallettransaction.setDescription("#"+userid+"Customer service operation");
                tkWallettransaction.setAmount(amount.negate());
                tkWallettransaction.setTransactionStatus("已完成");
                tkWallettransaction.setUpdatedAt(new Date());
                tkWallettransaction.setTransactionType("withdraw");
                tkWallettransaction.setTransactionDate(new Date());
                tkWallettransaction.setOrderNumber(orderNumber);
                tkWallettransaction.setWithdraw("yes");
                Assert.isTrue(tkWallettransactionsMapper.insertTkWallettransactions(tkWallettransaction)!=0,"网络异常...");

                if (result != null && !tkWallettransactionsDm.get(i).getId().toString().equals(result.getId().toString())) {
                    //回滚关系提成表金额
                    TkInvitation tkInvitation = new TkInvitation();
                    tkInvitation.setInviterId(tkUser.getUid());
                    tkInvitation.setInviteeId(result.getUserid());
                    List<TkInvitation> tkInvitations = tkInvitationMapper.selectTkInvitationList(tkInvitation);
                    Assert.isTrue(tkInvitations.size()!=0,"禁止多次回滚操作");
                    TkInvitation tkInvitationdm = tkInvitations.get(0);
                    tkInvitationdm.setAmount(new BigDecimal(tkInvitationdm.getAmount()).subtract(amount).toString());
                    tkInvitationMapper.updateTkInvitation(tkInvitationdm);
                }

            }else if(withdraw.equals("no")){
                //扣除用户余额
                TkUsers tkUser = tkUsersMapper.selectTkUsersByUid(userid);
                BigDecimal balance = new BigDecimal(tkUser.getBalance()).add(amount.negate());
                tkUser.setBalance(balance.toString());
                tkUser.setNonWithdrawableBalance(new BigDecimal(tkUser.getNonWithdrawableBalance()).subtract(amount).toString());
                Assert.isTrue(tkUsersMapper.updateTkUsers(tkUser)!=0,"网络异常...");


                //添加记录
                TkWallettransactions tkWallettransaction = new TkWallettransactions();
                tkWallettransaction.setUserid(userid);
                tkWallettransaction.setFundBalance(balance);
                tkWallettransaction.setCategory("TakeOut");
                tkWallettransaction.setDescription("#"+userid+"Customer service operation");
                tkWallettransaction.setAmount(amount.negate());
                tkWallettransaction.setTransactionStatus("已完成");
                tkWallettransaction.setUpdatedAt(new Date());
                tkWallettransaction.setTransactionType("TakeOut");
                tkWallettransaction.setTransactionDate(new Date());
                tkWallettransaction.setOrderNumber(orderNumber);
                tkWallettransaction.setWithdraw("no");
                Assert.isTrue(tkWallettransactionsMapper.insertTkWallettransactions(tkWallettransaction)!=0,"网络异常...");

                if (result != null && !tkWallettransactionsDm.get(i).getId().toString().equals(result.getId().toString())) {
                    //回滚关系提成表金额
                    TkInvitation tkInvitation = new TkInvitation();
                    tkInvitation.setInviterId(tkUser.getUid());
                    tkInvitation.setInviteeId(result.getUserid());
                    TkInvitation tkInvitationdm = tkInvitationMapper.selectTkInvitationList(tkInvitation).get(0);
                    tkInvitationdm.setAmount(new BigDecimal(tkInvitationdm.getAmount()).subtract(amount).toString());
                    tkInvitationMapper.updateTkInvitation(tkInvitationdm);
                }

            }
        }
        return AjaxResult.success("用户"+userIds+"余额扣除成功");
    }

    @Override
    public List<JuniorUserOV> ViewSubordinates(Long id) {
        Long userId = SecurityUtils.getUserId();

        //超级管理员和管理员可以查看全部
        for (int i = 0; i < SecurityUtils.getLoginUser().getUser().getRoles().size(); i++) {
            if (SecurityUtils.getLoginUser().getUser().getRoles().get(i).getRoleKey().contains("admin")) {
                userId = null;
            }
        }

        List<JuniorUserOV> juniorUserOVS = adminMapper.ViewSubordinates(id, userId);
        for (JuniorUserOV juniorUserOV : juniorUserOVS) {
            Long uid = juniorUserOV.getTkUsers().getUid(); // 用户id
            int num = getSubordinateCount(uid); // 获取下级数量
            juniorUserOV.getTkUsers().setTeamsize(num); // 假设JuniorUserOV有一个字段来存储下级数量
        }
        return juniorUserOVS;
    }

    @Override
    public AjaxResult pendingTask() {
        TkTaskAcceptances tkTaskAcceptances = new TkTaskAcceptances();
        tkTaskAcceptances.setTips("0");
        List<TkTaskAcceptances> tkTaskAcceptances1 = tkTaskAcceptancesMapper.selectTkTaskAcceptancesList(tkTaskAcceptances);
        HashMap<String, Integer> objectObjectHashMap = new HashMap<>();


        TkWithdrawals tkWithdrawals = new TkWithdrawals();
        tkWithdrawals.setTips("0");
        List<TkWithdrawals> tkWithdrawals1 = tkWithdrawalsMapper.selectTkWithdrawalsList(tkWithdrawals);
        objectObjectHashMap.put("Tasks",tkTaskAcceptances1.size());
        objectObjectHashMap.put("Withdrawal",tkWithdrawals1.size());
        return AjaxResult.success(objectObjectHashMap);
    }

    private int getSubordinateCount(Long userId) {
        List<JuniorUserOV> subordinates = adminMapper.ViewSubordinates(userId, null);
        int count = subordinates.size();
        for (JuniorUserOV subordinate : subordinates) {
            count += getSubordinateCount(subordinate.getTkUsers().getUid());
        }
        return count;
    }

}
