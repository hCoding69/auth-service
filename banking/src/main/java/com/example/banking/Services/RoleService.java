package com.example.banking.Services;

import com.example.banking.Models.Role;
import com.example.banking.Models.User;
import com.example.banking.Repositories.RoleRepository;
import com.example.banking.Repositories.UserRepository;
import com.example.banking.Services.dto.AssignRoleRequest;
import com.example.banking.Services.dto.CreateRoleRequest;
import com.example.banking.Services.dto.RoleDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository, JwtService jwtService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }



    public ResponseEntity<?> getRoles(){

        List<Role> roles = roleRepository.findAll();

        if (roles.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No roles found");
        }

        List<RoleDTO> rolesDto = roleRepository.findAll()
                .stream()
                .map(role -> new RoleDTO(
                        role.getId(),
                        role.getName(),
                        role.getDescription()
                ))
                .toList();

        return ResponseEntity.ok(rolesDto);

    }

    public ResponseEntity<?> getRolesWithPermissions(Long id){



        if(id != null){
            Role role = roleRepository.findById(id).orElse(null);
            if(role == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role Not Found");
            } else{
                return ResponseEntity.ok(role);
            }
        }
        List<Role> roles = roleRepository.findAll();

        if(roles.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Roles is Empty");
        }

        return ResponseEntity.ok(roles);
    }

    public ResponseEntity<?> createRole(RoleDTO req){


        String normalizedName = req.getName().trim().toUpperCase();
        if(roleRepository.existsByName(normalizedName)){
            return ResponseEntity.badRequest().body("Role already exists");
        } else{
            Role roleToCreate = new Role();

            roleToCreate.setName(req.getName());
            roleToCreate.setDescription(req.getDescription());
            roleToCreate.setUpdatedAt(LocalDateTime.now());
            Role rc = roleRepository.save(roleToCreate);
            RoleDTO response = new RoleDTO(rc.getId(), rc.getName(), rc.getDescription());
            return ResponseEntity.ok(response);
        }
    }

    public ResponseEntity<?> updateRole(Long id, RoleDTO req) {


        Role roleToUpdate = roleRepository.findById(id).orElse(null);
        if (roleToUpdate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role Not Found");
        }

        boolean updated = false;

        if (req.getName() != null
                && !req.getName().isBlank()
                && !req.getName().equalsIgnoreCase(roleToUpdate.getName())) {

            roleToUpdate.setName(req.getName().trim());
            updated = true;
        }

        if (req.getDescription() != null
                && !req.getDescription().isBlank()
                && !req.getDescription().equals(roleToUpdate.getDescription())) {

            roleToUpdate.setDescription(req.getDescription().trim());
            updated = true;
        }

        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .body("Nothing to update");
        }

        roleToUpdate.setUpdatedAt(LocalDateTime.now());

        Role updatedRole = roleRepository.save(roleToUpdate);

        RoleDTO response = new RoleDTO(
                updatedRole.getId(),
                updatedRole.getName(),
                updatedRole.getDescription()
        );

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> deleteRole(Long id ){



        Role role = roleRepository.findById(id).orElse(null);

        if (role == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Role Not Found");
        }

        roleRepository.delete(role);

        return ResponseEntity.ok("Role '" + role.getName() + "' deleted successfully");
    }
}
