package com.tiktok.task.domain;

import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tiktok.common.annotation.Excel;
import com.tiktok.common.core.domain.BaseEntity;

/**
 * 用户信息对象 tk_users
 * 
 * @author ruoyi
 * @date 2024-09-20
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TkUsers extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long uid;

    /** 账户名 */
    @Excel(name = "账户名")
    private String username;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 昵称 */
    @Excel(name = "昵称")
    private String nickname;

    /** svip等级 */
    @Excel(name = "svip等级")
    private Long svipLevel;

    /** 余额 */
    @Excel(name = "余额")
    private String balance;

    /** 不可提现余额 */
    @Excel(name = "不可提现余额")
    private String nonWithdrawableBalance;
    /**
     * 总奖励金额
     */
    private String totareward;


    /** 提现USDT地址 */
    @Excel(name = "提现USDT地址")
    private String usdtAddress;

    /** 注册时间 */
    @JsonFormat(pattern = "MM/dd/yyyy")
    @Excel(name = "注册时间", width = 30, dateFormat = "MM/dd/yyyy hh:mm:ss a")
    private Date registrationTime;


    private String avatar;
    /**
     * 邀请码
     */
    private String invitationCode;
    /**
     * 提现开关
     */
    private String withdraw;
    private String paymentPassword;
    /**
     * 区块链名称
     */
    private String blockchainName;


    /** 推荐人ID */
    @Excel(name = "推荐人ID")
    private Long referrerId;

    /** 用户状态 */
    @Excel(name = "用户状态")
    private String userStatus;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date logindate;
    private String deleted;

    private int teamsize;
    private String oldPasswords;

    private String frozenIvestmentAmount; //冻结的投资金额
    private String investmentAmount;//可提现投资金额
    private HashMap<String, Object> invest;

    public TkUsers() {
    }

    public void setUid(Long uid) 
    {
        this.uid = uid;
    }

    public Long getUid() 
    {
        return uid;
    }
    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setNickname(String nickname) 
    {
        this.nickname = nickname;
    }

    public String getNickname() 
    {
        return nickname;
    }
    public void setSvipLevel(Long svipLevel) 
    {
        this.svipLevel = svipLevel;
    }

    public Long getSvipLevel() 
    {
        return svipLevel;
    }
    public void setBalance(String balance) 
    {
        this.balance = balance;
    }

    public String getBalance() 
    {
        return balance;
    }
    public void setNonWithdrawableBalance(String nonWithdrawableBalance) 
    {
        this.nonWithdrawableBalance = nonWithdrawableBalance;
    }

    public String getNonWithdrawableBalance() 
    {
        return nonWithdrawableBalance;
    }
    public void setUsdtAddress(String usdtAddress) 
    {
        this.usdtAddress = usdtAddress;
    }

    public String getUsdtAddress() 
    {
        return usdtAddress;
    }
    public void setRegistrationTime(Date registrationTime) 
    {
        this.registrationTime = registrationTime;
    }

    public Date getRegistrationTime() 
    {
        return registrationTime;
    }
    public void setReferrerId(Long referrerId) 
    {
        this.referrerId = referrerId;
    }

    public Long getReferrerId() 
    {
        return referrerId;
    }
    public void setUserStatus(String userStatus) 
    {
        this.userStatus = userStatus;
    }

    public String getUserStatus() 
    {
        return userStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("uid", getUid())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("nickname", getNickname())
            .append("svipLevel", getSvipLevel())
            .append("balance", getBalance())
            .append("nonWithdrawableBalance", getNonWithdrawableBalance())
            .append("usdtAddress", getUsdtAddress())
            .append("registrationTime", getRegistrationTime())
            .append("referrerId", getReferrerId())
            .append("userStatus", getUserStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }

    public String getTotareward() {
        return totareward;
    }

    public void setTotareward(String totareward) {
        this.totareward = totareward;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(String withdraw) {
        this.withdraw = withdraw;
    }

    public String getBlockchainName() {
        return blockchainName;
    }

    public void setBlockchainName(String blockchainName) {
        this.blockchainName = blockchainName;
    }

    public Date getLogindate() {
        return logindate;
    }

    public void setLogindate(Date logindate) {
        this.logindate = logindate;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public int getTeamsize() {
        return teamsize;
    }

    public void setTeamsize(int teamsize) {
        this.teamsize = teamsize;
    }

    public String getPaymentPassword() {
        return paymentPassword;
    }

    public void setPaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
    }

    public String getOldPasswords() {
        return oldPasswords;
    }

    public void setOldPasswords(String oldPasswords) {
        this.oldPasswords = oldPasswords;
    }

    public String getFrozenIvestmentAmount() {
        return frozenIvestmentAmount;
    }

    public void setFrozenIvestmentAmount(String frozenIvestmentAmount) {
        this.frozenIvestmentAmount = frozenIvestmentAmount;
    }

    public String getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(String investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public HashMap<String, Object> getInvest() {
        return invest;
    }

    public void setInvest(HashMap<String, Object> invest) {
        this.invest = invest;
    }
}
