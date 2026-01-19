package com.example.banking.Controllers;


import com.example.banking.Services.PermissionService;
import com.example.banking.Services.dto.PermissionDTO;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService){
        this.permissionService = permissionService;
    }

    @GetMapping()
    public ResponseEntity<?> getPermissions(){
        return this.permissionService.getPermissions();
    }

    @PostMapping()
    public ResponseEntity<?> createPermission(
                                              @Valid @RequestBody PermissionDTO request
                                              ){
        return this.permissionService.createPermission(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPermission(
                                            @Valid @RequestBody PermissionDTO request,
                                            @PathVariable Long id
                                            ){
        return this.permissionService.updatePermission(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePermission(
                                              @PathVariable Long id
    ){
        return this.permissionService.deletePermission(id);
    }


}


