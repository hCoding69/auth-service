package com.example.banking.Controllers;


import com.example.banking.Services.RolePermissionService;
import com.example.banking.Services.RoleService;
import com.example.banking.Services.dto.PermissionDTO;
import com.example.banking.Services.dto.RoleDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    private final RolePermissionService rolePermissionService;

    public RoleController(RoleService roleService, RolePermissionService rolePermissionService){
        this.roleService = roleService;
        this.rolePermissionService = rolePermissionService;
    }

    @GetMapping
    public ResponseEntity<?> getRoles(
    ){
        return roleService.getRoles();
    }

    @GetMapping("/with-permissions")
    public ResponseEntity<?> getAllRolesWithPermissions(){
        return roleService.getRolesWithPermissions(null);
    }

    @GetMapping("/with-permissions/{id}")
    public ResponseEntity<?> getRoleWithPermissions(@PathVariable Long id){
        return roleService.getRolesWithPermissions(id);
    }


    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleDTO req){
        return roleService.createRole( req);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(
                                        @PathVariable Long id, @Valid @RequestBody RoleDTO req
                                        ){
        return roleService.updateRole(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(
                                        @PathVariable Long id){
        return roleService.deleteRole(id);
    }


    @PostMapping("/{roleId}/permissions")
    public ResponseEntity<?> addRoleToPermission(@PathVariable Long roleId, @Valid @RequestBody List<PermissionDTO> permissions){
        return rolePermissionService.addPermissionsToRole(permissions, roleId);
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<?> removePermissionFromRole(@PathVariable Long roleId, @PathVariable Long permissionId){
        return rolePermissionService.removePermission(roleId, permissionId);
    }
}
