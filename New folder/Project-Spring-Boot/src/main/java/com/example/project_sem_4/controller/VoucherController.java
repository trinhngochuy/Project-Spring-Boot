package com.example.project_sem_4.controller;

import com.example.project_sem_4.database.dto.AccountDTO;
import com.example.project_sem_4.database.dto.BranchDTO;
import com.example.project_sem_4.database.dto.RegisterDTO;
import com.example.project_sem_4.database.dto.VoucherDTO;
import com.example.project_sem_4.database.search_body.BranchSearchBody;
import com.example.project_sem_4.database.search_body.VoucherSearchBody;
import com.example.project_sem_4.service.authen.AuthenticationService;
import com.example.project_sem_4.service.voucher.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(value = "/voucher")
public class VoucherController {
    private final VoucherService voucherService;
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Object> saveVoucher(@RequestBody @Valid VoucherDTO model) {
        boolean result = voucherService.saveVoucher(model);
        if (result){
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.internalServerError().body(null);
    }

    @GetMapping("/{voucherCode}")
    public ResponseEntity findByVoucher_code(@PathVariable String voucherCode) {
        return new ResponseEntity(voucherService.getVoucherByVoucher_code(voucherCode), HttpStatus.OK);
    }

    @GetMapping("delete/{voucherCode}")
    public ResponseEntity deleteByVoucher_code(@PathVariable String voucherCode) {
        return new ResponseEntity(voucherService.deleteVoucherByVoucher_code(voucherCode), HttpStatus.OK);
    }

    @RequestMapping(value = "/use-disable/{voucherCode}", method = RequestMethod.GET)
    public ResponseEntity disableVoucherUsed(@PathVariable String voucherCode) {
        return new ResponseEntity(voucherService.disableVoucher(voucherCode), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity search(@RequestBody VoucherSearchBody voucherSearchBody) {
        return new ResponseEntity(voucherService.findAll(voucherSearchBody), HttpStatus.OK);
    }
}
