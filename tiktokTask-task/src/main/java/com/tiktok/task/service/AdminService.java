package com.tiktok.task.service;

import com.tiktok.common.core.domain.AjaxResult;
import com.tiktok.task.domain.ov.JuniorUserOV;

import java.util.List;

public interface AdminService {
    AjaxResult HomePage();

    AjaxResult AddAndDeduct(String amount,String withdraw,String add,String uid,String rebate,String type);

    AjaxResult TakeOut(Long id);

    List<JuniorUserOV> ViewSubordinates(Long id);

    AjaxResult pendingTask();
}
