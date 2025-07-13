package e_commerce.monolithic.controller.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.monolithic.dto.admin.AccountAdminDTO;
import e_commerce.monolithic.dto.admin.AccountCreateAdminDTO;
import e_commerce.monolithic.dto.admin.AccountUpdateAdminDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.service.admin.AccountService;
import jakarta.validation.Valid;

// NOTE: Chỉ tài khoản có ROLE_SYSTEM mới truy cập được các API này (xem SecurityConfig)
@RestController
@RequestMapping("/api/admin/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity<List<AccountAdminDTO>> getAllAccounts() {
        List<AccountAdminDTO> accounts = accountService.getAllAccount();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        try {
            AccountAdminDTO account = accountService.getAccountById(id);
            return ResponseEntity.ok(account);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.NOT_FOUND, e.getMessage()),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occcurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountCreateAdminDTO accountCreateAdminDTO) {
        try {
            AccountAdminDTO createdAccount = accountService.createAccount(accountCreateAdminDTO);
            return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred."),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity<?> updateAccount(@RequestBody @Valid AccountUpdateAdminDTO accountUpdateAdminDTO) {
        try {
            AccountAdminDTO updatedAccount = accountService.updateAccount(accountUpdateAdminDTO);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.NOT_FOUND, e.getMessage()),
                    HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred."),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity<ResponseMessageDTO> deleteAccount(@PathVariable Long id) {
        try {
            accountService.deleteAccount(id);
            return new ResponseEntity<>(
                    new ResponseMessageDTO(HttpStatus.OK, "Account with id " + id + " delete successfully"),
                    HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.NOT_FOUND, e.getMessage()),
                    HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred."),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity<?> toggleAccountStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        try {
            Boolean enabled = request.get("enabled");
            if (enabled == null) {
                return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.BAD_REQUEST, "Missing 'enabled' field"),
                        HttpStatus.BAD_REQUEST);
            }

            AccountAdminDTO updatedAccount = accountService.toggleAccountStatus(id, enabled);
            return ResponseEntity.ok(updatedAccount);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.NOT_FOUND, e.getMessage()),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity<List<AccountAdminDTO>> searchAccounts(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String fullname,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate birthday,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createAtEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updateAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updateAtEnd) {
        List<AccountAdminDTO> accounts = accountService.searchAccounts(username, email, fullname, phone, gender,
                birthday, enabled, role, createAtStart, createAtEnd, updateAtStart, updateAtEnd);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/{id}/roles")
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity<?> addRoleToUser(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String roleName = request.get("roleName");
            if (roleName == null || roleName.trim().isEmpty()) {
                return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.BAD_REQUEST, "Missing 'roleName' field"),
                        HttpStatus.BAD_REQUEST);
            }

            AccountAdminDTO updatedAccount = accountService.addRoleToUser(id, roleName.trim());
            return ResponseEntity.ok(updatedAccount);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.NOT_FOUND, e.getMessage()),
                    HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/roles/{roleName}")
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable Long id, @PathVariable String roleName) {
        try {
            if (roleName == null || roleName.trim().isEmpty()) {
                return new ResponseEntity<>(
                        new ResponseMessageDTO(HttpStatus.BAD_REQUEST, "Missing 'roleName' parameter"),
                        HttpStatus.BAD_REQUEST);
            }

            AccountAdminDTO updatedAccount = accountService.removeRoleFromUser(id, roleName.trim());
            return ResponseEntity.ok(updatedAccount);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.NOT_FOUND, e.getMessage()),
                    HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
