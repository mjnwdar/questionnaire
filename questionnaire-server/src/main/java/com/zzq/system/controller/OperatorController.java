package com.zzq.system.controller;

import com.zzq.common.annotation.OperationLog;
import com.zzq.common.annotation.RefreshFilterChain;
import com.zzq.common.util.ResultEntity;
import com.zzq.system.model.Operator;
import com.zzq.system.service.OperatorService;
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

/**
 * @author Sherlock
 */
@RestController
@RequestMapping("/operator")
public class OperatorController {

    @Resource
    private OperatorService operatorService;

    @OperationLog("查看操作日志")
    @GetMapping("/index")
    public String index() {
        return "operator/operator-list";
    }

    @GetMapping
    public String add() {
        return "operator/operator-add";
    }

    @RefreshFilterChain
    @PostMapping
    public ResultEntity add(Operator operator) {
        operatorService.add(operator);
        return ResultEntity.success();
    }

    @GetMapping("/{operatorId}")
    public String update(Model model, @PathVariable("operatorId") Integer operatorId) {
        Operator operator = operatorService.selectByPrimaryKey(operatorId);
        model.addAttribute("operator", operator);
        return "operator/operator-add";
    }

    @RefreshFilterChain
    @PutMapping
    public ResultEntity update(Operator operator) {
        operatorService.updateByPrimaryKey(operator);
        return ResultEntity.success();
    }

    @GetMapping("/list")
    public ResultEntity getList(@RequestParam(required = false) Integer menuId) {
        List<Operator> operatorList = operatorService.selectByMenuId(menuId);
        return ResultEntity.success(operatorList);
    }

    @RefreshFilterChain
    @DeleteMapping("/{operatorId}")
    public ResultEntity delete(@PathVariable("operatorId") Integer operatorId) {
        operatorService.deleteByPrimaryKey(operatorId);
        return ResultEntity.success();
    }


    @GetMapping("/tree")
    public ResultEntity tree() {
        return ResultEntity.success(operatorService.getALLMenuAndOperatorTree());
    }

}
