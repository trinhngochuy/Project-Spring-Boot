package com.example.project_sem_4.database.repository;

import com.example.project_sem_4.database.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    public List<Voucher> findAllByOrderByVoucherCodeIntDesc();
    public Voucher findByVoucherCode(String voucherCode);
}
