package com.zheng.upms.server.controller.manage;

import com.zheng.common.base.BaseController;
import com.zheng.upms.dao.model.UpmsRole;
import com.zheng.upms.dao.model.UpmsRoleExample;
import com.zheng.upms.rpc.api.UpmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色controller
 * Created by shuzheng on 2017/2/6.
 */
@Controller
@Api(value = "角色管理", description = "角色管理")
@RequestMapping("/manage/role")
public class UpmsRoleController extends BaseController {

    private static Logger _log = LoggerFactory.getLogger(UpmsRoleController.class);

    @Autowired
    private UpmsRoleService upmsRoleService;

    @ApiOperation(value = "角色首页")
    @RequiresPermissions("upms:role:read")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/manage/role/index";
    }

    @ApiOperation(value = "角色列表")
    @RequiresPermissions("upms:role:read")
    @RequestMapping("/list")
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order) {
        // 数据列表
        UpmsRoleExample upmsRoleExample = new UpmsRoleExample();
        upmsRoleExample.setOffset(offset);
        upmsRoleExample.setLimit(limit);
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            upmsRoleExample.setOrderByClause(sort + " " + order);
        }
        List<UpmsRole> rows = upmsRoleService.selectByExample(upmsRoleExample);
        long total = upmsRoleService.countByExample(upmsRoleExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation(value = "新增角色")
    @RequiresPermissions("upms:role:create")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "/manage/role/add";
    }

    @ApiOperation(value = "新增角色")
    @RequiresPermissions("cms:role:create")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(UpmsRole upmsRole, ModelMap modelMap) {
        long time = System.currentTimeMillis();
        upmsRole.setCtime(time);
        upmsRole.setOrders(time);
        int count = upmsRoleService.insertSelective(upmsRole);
        modelMap.put("count", count);
        _log.info("新增记录id为：{}", upmsRole.getRoleId());
        return "redirect:/manage/role/list";
    }

    @ApiOperation(value = "删除角色")
    @RequiresPermissions("cms:role:delete")
    @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
    public String delete(@PathVariable("ids") String ids, ModelMap modelMap) {
        int count = upmsRoleService.deleteByPrimaryKeys(ids);
        modelMap.put("count", count);
        return "redirect:/manage/role/list";
    }

    @ApiOperation(value = "修改角色")
    @RequiresPermissions("cms:role:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") int id, ModelMap modelMap) {
        UpmsRole role = upmsRoleService.selectByPrimaryKey(id);
        modelMap.put("role", role);
        return "/manage/role/update";
    }

    @ApiOperation(value = "修改角色")
    @RequiresPermissions("cms:role:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String update(@PathVariable("id") int id, UpmsRole upmsRole, ModelMap modelMap) {
        int count = upmsRoleService.updateByPrimaryKeySelective(upmsRole);
        modelMap.put("count", count);
        modelMap.put("id", id);
        return "redirect:/manage/role/list";
    }

}
