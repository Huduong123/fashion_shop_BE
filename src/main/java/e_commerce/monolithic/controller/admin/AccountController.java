package e_commerce.monolithic.controller.admin;

import e_commerce.monolithic.dto.admin.AccountAdminDTO;
import e_commerce.monolithic.dto.admin.AccountCreateAdminDTO;
import e_commerce.monolithic.dto.admin.AccountUpdateAdminDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.service.admin.AccountService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AccountAdminDTO>> getAllAccounts() {
        List<AccountAdminDTO> accounts = accountService.getAllAccount();
        return ResponseEntity.ok(accounts);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        try {
            AccountAdminDTO account = accountService.getAccountById(id);
            return ResponseEntity.ok(account);
        }catch (NotFoundException e){
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occcurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountCreateAdminDTO  accountCreateAdminDTO) {
        try {
            AccountAdminDTO createdAccount = accountService.createAccount(accountCreateAdminDTO);
            return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return  new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAccount(@RequestBody @Valid AccountUpdateAdminDTO accountUpdateAdminDTO) {
        try {
            AccountAdminDTO updatedAccount = accountService.updateAccount(accountUpdateAdminDTO);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        }catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessageDTO> deleteAccount (@PathVariable Long id) {
        try {
            accountService.deleteAccount(id);
            return  new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.OK, "Account with id "+id+" delete successfully"), HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AccountAdminDTO>>  searchAccounts(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String fullname,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy")LocalDate birthday,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)LocalDateTime createAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)LocalDateTime createAtEnd,
            @RequestParam(required = false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime updateAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updateAtEnd
            ){
        List<AccountAdminDTO> accounts = accountService.searchAccounts(username,email,fullname,phone, gender, birthday, enabled,createAtStart,createAtEnd,updateAtStart,updateAtEnd);
        return ResponseEntity.ok(accounts);
    }

}
