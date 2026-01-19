package com.example.banking.Services;


import com.example.banking.Models.Permission;
import com.example.banking.Repositories.PermissionRepository;
import com.example.banking.Services.dto.PermissionDTO;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final JwtService jwtService;

    public PermissionService(PermissionRepository permissionRepository, JwtService jwtService){
        this.permissionRepository = permissionRepository;
        this.jwtService = jwtService;
    }

    public ResponseEntity<?> getPermissions(){


        List<PermissionDTO> permissions = permissionRepository.findAllOrderByName();
        if(permissions.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "The Table Permission is Empty")
            );
        } else{
            return ResponseEntity.ok(permissions);
        }
    }

    public ResponseEntity<?> createPermission( PermissionDTO request){




        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        Permission savedPermission = permissionRepository.save(permission);

        PermissionDTO response = new PermissionDTO(
                savedPermission.getId(),
                savedPermission.getName(),
                savedPermission.getDescription()
        );

        return ResponseEntity.ok(response);

    }

    public ResponseEntity<?> updatePermission(Long id, PermissionDTO request){

        boolean updated = false;
        Permission existedPermission = permissionRepository.findById(id).orElse(null);

        if(existedPermission == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "No Permission Found")
            );
        }

        if(request.getName()!= null && !request.getName().isBlank() && !request.getName().equals(existedPermission.getName())){
            existedPermission.setName(request.getName());
            updated = true;
        }

        if(request.getDescription()!= null && !request.getDescription().isBlank() && !request.getDescription().equals(existedPermission.getDescription())){
            existedPermission.setDescription(request.getDescription());
            updated = true;
        }

        if(updated) {
            existedPermission.setUpdatedAt(LocalDateTime.now());
        }

        Permission updatedPermission = permissionRepository.save(existedPermission);

        PermissionDTO response = new PermissionDTO(
                updatedPermission.getId(),
                updatedPermission.getName(),
                updatedPermission.getDescription()
        );
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> deletePermission(Long id) {

        if (!permissionRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Permission not found"));
        }

        permissionRepository.deleteById(id);

        return ResponseEntity.ok(
                Map.of("message", "Permission with id = " + id + " is successfully deleted")
        );
    }
}
