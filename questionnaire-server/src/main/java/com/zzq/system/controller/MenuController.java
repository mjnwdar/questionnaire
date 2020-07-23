package com.zzq.system.controller;

import com.zzq.common.annotation.OperationLog;
import com.zzq.common.annotation.RefreshFilterChain;
import com.zzq.common.util.ResultEntity;
import com.zzq.system.model.Menu;
import com.zzq.system.service.MenuService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @GetMapping("/index")
    public String index() {
        return "menu/menu-list";
    }

    @OperationLog("获取菜单列表")
    @GetMapping("/list")
    public ResultEntity getList(@RequestParam(required = false) Integer parentId) {
        List<Menu> menuList = menuService.selectByParentId(parentId);
        return ResultEntity.success(menuList);
    }

    @GetMapping
    public String add(Model model) {
        return "menu/menu-add";
    }

    @GetMapping("/tree")
    public ResultEntity tree() {
        return ResultEntity.success(menuService.getALLTree());
    }

    @GetMapping("/tree/root")
    public ResultEntity treeAndRoot() {
        return ResultEntity.success(menuService.getALLMenuTreeAndRoot());
    }

    @GetMapping("/tree/root/operator")
    public ResultEntity menuAndCountOperatorTreeAndRoot() {
        return ResultEntity.success(menuService.getALLMenuAndCountOperatorTreeAndRoot());
    }

    @OperationLog("新增菜单")
    @RefreshFilterChain
    @PostMapping
    public ResultEntity add(Menu menu) {
        menuService.insert(menu);
        return ResultEntity.success();
    }

    @OperationLog("删除菜单")
    @RefreshFilterChain
    @DeleteMapping("/{menuId}")
    public ResultEntity delete(@PathVariable("menuId") Integer menuId) {
        menuService.deleteByIDAndChildren(menuId);
        return ResultEntity.success();
    }

    @GetMapping("/{menuId}")
    public String updateMenu(@PathVariable("menuId") Integer menuId, Model model) {
        Menu menu = menuService.selectByPrimaryKey(menuId);
        model.addAttribute("menu", menu);
        return "menu/menu-add";
    }

    @OperationLog("修改菜单")
    @RefreshFilterChain
    @PutMapping
    public ResultEntity update(Menu menu) {
        menuService.updateByPrimaryKey(menu);
        return ResultEntity.success();
    }

    @OperationLog("调整部门排序")
    @PostMapping("/swap")
    public ResultEntity swapSort(Integer currentId, Integer swapId) {
        menuService.swapSort(currentId, swapId);
        return ResultEntity.success();
    }
}