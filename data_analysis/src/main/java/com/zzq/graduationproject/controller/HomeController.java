package com.zzq.graduationproject.controller;

import com.zzq.graduationproject.model.DataSource;
import com.zzq.graduationproject.model.HostHolder;
import com.zzq.graduationproject.model.Page;
import com.zzq.graduationproject.model.User;
import com.zzq.graduationproject.service.DataGroupService;
import com.zzq.graduationproject.service.DataSourceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zzq
 */
@Controller
public class HomeController implements ErrorController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private DataGroupService dataGroupService;

    /**
     * 首页
     *
     * @param model
     * @return
     */

    @GetMapping("/")
    public String manager(Model model,
        @RequestParam(value = "p", defaultValue = "1") int pageNum) {

        User user = hostHolder.getUser();

      if (user == null) {
        return "login";
      }

        model.addAttribute("user", user);

        model.addAttribute("page", "index");

        int pageSize = 5;
        Page page = new Page(pageNum, pageSize, dataSourceService.getDataSourceCount(user.getId()));
        List<DataSource> list = dataSourceService.getDataSource(hostHolder.getUser().getId(),
            page.getOffset(), page.getPageSize());

        model.addAttribute("dataSources", list);
        model.addAttribute("pages", page);
        model.addAttribute("dataGroups",
            dataGroupService.getDataGroup(user.getId(), 0, Integer.MAX_VALUE));

        return "index";
    }

//  @GetMapping("/dataSourceGroup")
//  public String dataSourceGroup(Model model) {
//
//    model.addAttribute("page", "dataSourceGroup");
//
//    User user = hostHolder.getUser();
//
//    if(user != null) {
//      model.addAttribute("user", user);
//    }
//
//    return "dataSourceGroup";
//  }
//
//  @GetMapping("/dataFlowManager")
//  public String dataFlowManager(Model model) {
//
//    model.addAttribute("page", "dataFlowManager");
//
//    User user = hostHolder.getUser();
//
//    if(user != null) {
//      model.addAttribute("user", user);
//    }
//
//    return "dataFlowManager";
//  }
//
//  @GetMapping("/dataShow")
//  public String dataShow(Model model) {
//
//    model.addAttribute("page", "dataShow");
//
//    User user = hostHolder.getUser();
//
//    if(user != null) {
//      model.addAttribute("user", user);
//    }
//
//    return "dataShow";
//  }
//
//  @GetMapping("/messageManager")
//  public String messageManager(Model model) {
//
//    model.addAttribute("page", "messageManager");
//
//    User user = hostHolder.getUser();
//
//    if(user != null) {
//      model.addAttribute("user", user);
//    }
//
//    return "messageManager";
//  }

    @GetMapping("/error")
    @Override
    public String getErrorPath() {
        return "error";
    }
}
