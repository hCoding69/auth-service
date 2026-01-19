package com.example.banking.Services;


import com.example.banking.Models.Permission;
import com.example.banking.Models.Role;
import com.example.banking.Repositories.PermissionRepository;
import com.example.banking.Repositories.RoleRepository;
import com.example.banking.Services.dto.PermissionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class RolePermissionService {
    private RoleRepository rRepo;
    private PermissionRepository pRepo;

    public RolePermissionService(RoleRepository rRepo, PermissionRepository pRepo){
        this.rRepo = rRepo;
        this.pRepo = pRepo;
    }

    //    POST	/roles/{roleId}/permissions	Ajouter permissions
    public ResponseEntity<?> addPermissionsToRole(List<PermissionDTO> permissions, Long id) {
        if (permissions == null || permissions.isEmpty()) {
            return ResponseEntity.badRequest().body("Nothing To Add");
        }

        Role role = rRepo.findById(id).orElse(null);
        if (role == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role Does Not Exist");
        }

        if (role.getPermissions() == null) {
            role.setPermissions(new HashSet<>());
        }

        for (PermissionDTO dto : permissions) {
            Permission permission = pRepo.findById(dto.getId()).orElse(null);
            if (permission == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Permission " + dto.getId() + " Does Not Exist");
            }

            if (role.getPermissions().stream().anyMatch(p -> p.getId().equals(dto.getId()))) {
                return ResponseEntity.badRequest()
                        .body("Permission " + dto.getId() + " Already assigned");
            }

            role.getPermissions().add(permission);
        }

        rRepo.save(role);
        return ResponseEntity.ok("Permissions Added To Role Successfully");
    }


    public ResponseEntity<?> removePermission(Long roleId, Long permissionId){
        Role role = rRepo.findById(roleId).orElse(null);
        if(role == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role Not Found");
        }
        Permission pTR = pRepo.findById(permissionId).orElse(null);

        if(pTR == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Permisisons Not Found");
        }
        boolean hasPermission = role.getPermissions().stream()
                .anyMatch(p -> p.getId().equals(permissionId));

        if (!hasPermission) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The Role Does Not Have This Permission");
        }
        role.getPermissions().remove(pTR);

        rRepo.save(role);
        return ResponseEntity.ok("Permission Removed Successfully");
    }
}



