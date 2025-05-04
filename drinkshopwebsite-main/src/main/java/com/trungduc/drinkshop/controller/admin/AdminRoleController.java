package com.trungduc.drinkshop.controller.admin;

import com.trungduc.drinkshop.controller.common.BaseController;
import com.trungduc.drinkshop.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/admin/roles_management")
public class AdminRoleController extends BaseController {
    private final RoleService roleService;
}

