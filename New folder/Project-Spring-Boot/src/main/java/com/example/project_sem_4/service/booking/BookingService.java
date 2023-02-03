package com.example.project_sem_4.service.booking;

import com.example.project_sem_4.database.dto.booking.BookingDTO;
import com.example.project_sem_4.database.dto.booking.BookingSearchDTO;
import com.example.project_sem_4.database.entities.*;
import com.example.project_sem_4.database.jdbc_query.QueryBookingByJDBC;
import com.example.project_sem_4.database.repository.AccountRepository;
import com.example.project_sem_4.database.repository.BookingRepository;
import com.example.project_sem_4.database.repository.BranchRepository;
import com.example.project_sem_4.database.search_body.BookingSearchBody;
import com.example.project_sem_4.enum_project.RoleEnum;
import com.example.project_sem_4.enum_project.StatusEnum;
import com.example.project_sem_4.enum_project.TimeBookingEnum;
import com.example.project_sem_4.util.HelpConvertBookingDate;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionBadRequest;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionCustomBadRequest;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionNotFound;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class BookingService {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    QueryBookingByJDBC queryBookingByJDBC;
    public Booking createBooking(BookingDTO bookingDTO){
        Account checkEmployee = accountRepository.findById(bookingDTO.getEmployee_id()).orElse(null);
        if (checkEmployee == null){
            throw new ApiExceptionNotFound("bookings","employee_id",bookingDTO.getEmployee_id());
        }
        boolean checkRoleSTAFF = false;
        // Kiểm tra đây có phải là nhân viên cắt tóc không
        for (Role role: checkEmployee.getRoles()) {
            if (role.getName().equals(RoleEnum.STAFF.role)){
                checkRoleSTAFF = true;
            }
        }
        if (!checkRoleSTAFF){
            throw new ApiExceptionBadRequest("bookings","employee_id","Yêu cầu chọn tài khoản có role là STAFF");
        }

//        //Tạo thời gian đặt
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
//        String dateString = "21-12-2022 " + TimeBookingEnum.TIME_8H.timeValue;
//        try {
//            Date date = sdf.parse(dateString);
//            log.info("dateString là " + dateString);
//            log.info("Thời gian parse thường là " + date);
//            log.info("Thời gian parse time là " + date.getTime());
//            log.info("Thời gian parse day là " + date.getDay());
//            log.info("Thời gian parse date là " + date.getDate());
//            log.info("Thời gian parse date là " + date.getHours());
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(date);
//            log.info("Thời gian calendar là " + new SimpleDateFormat("EEEE").format(cal.getTime()));
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }

        boolean checkTimeBooking = false;
        String timeBookingValue = "";
        for (TimeBookingEnum booking: TimeBookingEnum.values()) {
            if (booking.timeName.equals(bookingDTO.getTime_booking())){
                timeBookingValue = booking.timeValue;
                checkTimeBooking = true;
            }
        }

        if (!checkTimeBooking){
            throw new ApiExceptionBadRequest("bookings","time_booking","Chọn sai thời gian lịch " + bookingDTO.getDate_booking());
        }

        Date dateBooking = HelpConvertBookingDate.convertStringToDate(bookingDTO.getDate_booking() + " " + timeBookingValue);
        //Kiểm tra thời gian đặt
        Date dateNow = new Date();
        if (dateBooking.getTime() - dateNow.getTime() < 0){
            throw new ApiExceptionCustomBadRequest("Thời gian tạo lịch làm việc không được trước thời gian hiện tại");
        }

        // Kiểm tra trùng lịch
        Booking checkBookingDuplicate =  bookingRepository.findByDateAndAndEmployee_id(String.valueOf(dateBooking.getTime()),bookingDTO.getEmployee_id());
        if (checkBookingDuplicate != null){
            throw new ApiExceptionBadRequest("bookings","date","Trùng lịch");
        }
        // Kiểm tra chi nhánh
        Branch checkBranch = branchRepository.findById(bookingDTO.getBranch_id()).orElse(null);
        if (checkBranch == null){
            throw new ApiExceptionNotFound("branchs","id",bookingDTO.getBranch_id());
        }

//        log.info("Thời gian parse getDay là " + dateBooking.getDay());
//        log.info("Thời gian parse getDate là " + dateBooking.getDate());
//        log.info("Thời gian parse getMonth là " + dateBooking.getMonth());
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(dateBooking);
//        log.info("Thời gian parse cal.get(Calendar.YEAR) là " + cal.get(Calendar.YEAR));
//        log.info("Thời gian parse getTime là " + dateBooking.getTime());
//        log.info("Thời gian parse getHours là " + dateBooking.getHours());
//        log.info("Thời gian parse getMinutes là " + dateBooking.getMinutes());
//        log.info("Thời gian parse getSeconds là " + dateBooking.getSeconds());
//        log.info("Thời gian parse getTimezoneOffset là " + dateBooking.getTimezoneOffset());
//        log.info("Thời gian parse dateBooking là " + dateBooking);


        String bookingDate = String.valueOf(dateBooking.getDate());
        if (dateBooking.getDate() < 10){
            bookingDate = 0+String.valueOf(dateBooking.getDate());
        }

        int validateMonth = dateBooking.getMonth() + 1;
        String bookingMonth = String.valueOf(validateMonth);
        if (validateMonth < 10){
            bookingMonth = 0+String.valueOf(validateMonth);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBooking);
        String bookingYear = String.valueOf(cal.get(Calendar.YEAR));

        Booking bookingSave = Booking.builder()
                .branch(checkBranch)
                .employee(checkEmployee)
                .date(String.valueOf(dateBooking.getTime()))
                .date_booking(bookingDate + "-" + bookingMonth + "-" + bookingYear)
                .time_booking(bookingDTO.getTime_booking())
                .build();

        bookingSave.setStatus(StatusEnum.UN_ACTIVE.status);
        bookingSave.setCreated_at(new Date());

        return bookingRepository.save(bookingSave);
    }

    public Booking updateBooking(BookingDTO bookingDTO, String id){
        Booking updateBooking = bookingRepository.findById(id).orElse(null);
        if (updateBooking == null){
            throw new ApiExceptionNotFound("bookings","id",id);
        }

        // Kiểm tra tài khoản nhân viên
        Account checkEmployee = accountRepository.findById(bookingDTO.getEmployee_id()).orElse(null);
        if (checkEmployee == null){
            throw new ApiExceptionNotFound("bookings","employee_id",bookingDTO.getEmployee_id());
        }
        boolean checkRoleADMIN_STAFF = false;
        // Kiểm tra đây có phải là nhân viên cắt tóc không
        for (Role role: checkEmployee.getRoles()) {
            if (role.getName().equals(RoleEnum.ADMIN.role) || role.getName().equals(RoleEnum.STAFF.role)){
                checkRoleADMIN_STAFF = true;
            }
        }
        if (!checkRoleADMIN_STAFF){
            throw new ApiExceptionBadRequest("bookings","employee_id","Yêu cầu chọn tài khoản có role là ADMIN hoặc STAFF");
        } else {
            updateBooking.setEmployee(checkEmployee);
        }

        boolean checkTimeBooking = false;
        String timeBookingValue = "";
        for (TimeBookingEnum booking: TimeBookingEnum.values()) {
            if (booking.timeName.equals(bookingDTO.getTime_booking())){
                timeBookingValue = booking.timeValue;
                checkTimeBooking = true;
            }
        }

        if (!checkTimeBooking){
            throw new ApiExceptionBadRequest("bookings","time_booking","Chọn sai thời gian lịch " + bookingDTO.getTime_booking());
        }

        Date dateBooking = HelpConvertBookingDate.convertStringToDate(bookingDTO.getDate_booking() + " " + timeBookingValue);

        // Kiểm tra trùng lịch
        Booking checkBookingDuplicate =  bookingRepository.findByDateAndAndEmployee_id(String.valueOf(dateBooking.getTime()),bookingDTO.getEmployee_id());
        if (checkBookingDuplicate != null){
            throw new ApiExceptionBadRequest("bookings","date","Trùng lịch");
        }
        updateBooking.setDate(String.valueOf(dateBooking.getTime()));

        Branch checkBranch = branchRepository.findById(bookingDTO.getBranch_id()).orElse(null);
        if (checkBranch == null){
            throw new ApiExceptionNotFound("branchs","id",bookingDTO.getBranch_id());
        }
        updateBooking.setBranch(checkBranch);

        String bookingDate = String.valueOf(dateBooking.getDate());
        if (dateBooking.getDate() < 10){
            bookingDate = 0+String.valueOf(dateBooking.getDate());
        }

        int validateMonth = dateBooking.getMonth() + 1;
        String bookingMonth = String.valueOf(validateMonth);
        if (validateMonth < 10){
            bookingMonth = 0+String.valueOf(validateMonth);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBooking);
        String bookingYear = String.valueOf(cal.get(Calendar.YEAR));

        updateBooking.setDate_booking(bookingDate + "-" + bookingMonth + "-" + bookingYear);
        updateBooking.setTime_booking(bookingDTO.getTime_booking());

//        Booking bookingSave = Booking.builder()
//                .branch(checkBranch)
//                .employee(checkEmployee)
//                .date(String.valueOf(dateBooking.getTime()))
//                .time_booking(bookingDTO.getTime_booking())
//                .build();
//        bookingSave.setCreated_at(new Date());

        updateBooking.setUpdated_at(new Date());
        updateBooking.setStatus(bookingDTO.getStatus());
        return bookingRepository.save(updateBooking);
    }

    public boolean deleteBooking(String id){
        Booking deleteBooking = bookingRepository.findById(id).orElse(null);
        if (deleteBooking == null){
            throw new ApiExceptionNotFound("bookings","id",id);
        }
        if (deleteBooking.getStatus() == StatusEnum.ACTIVE.status){
            throw new ApiExceptionCustomBadRequest("Lịch đã được đặt, không được xóa");
        }
        bookingRepository.delete(deleteBooking);
        return true;
    }

    public List<Booking> findAllByEmployee_idAndDate_booking(int employee_id, String date_booking){
        return bookingRepository.findByEmployee_idAndDate_booking(employee_id,date_booking);
    }
    public Map<String, Object> findAll(BookingSearchBody bookingSearchBody) {
        List<BookingSearchDTO> listContentPage = queryBookingByJDBC.filterWithPaging(bookingSearchBody);
//        List<BookingSearchDTO> listContentNoPage = queryBookingByJDBC.filterWithNoPaging(bookingSearchBody);

        Map<String, Object> responses = new HashMap<>();
        responses.put("content", listContentPage);
        responses.put("currentPage", bookingSearchBody.getPage());
        responses.put("totalItems", listContentPage.size());
        responses.put("totalPage", (int) Math.ceil((double) listContentPage.size() / bookingSearchBody.getLimit()));
        return responses;
    }

    public Booking findById(String id){
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null){
            throw new ApiExceptionNotFound("bookings","id",id);
        }
        return booking;
    }

    public   List<Object> findChartPie(){
        List<Object> responses = queryBookingByJDBC.filterBookingForChartPei();
        return  responses;
    }

    public   List<Object> findChartRange(){
        List<Object> responses = queryBookingByJDBC.filterBookingForChartRange();
        return  responses;
    }

    public   List<Object> findChartColumn(){
        List<Object> responses = queryBookingByJDBC.filterBookingForChartColumn();
        return  responses;
    }

    public   List<Object> findChartHeat(){
        List<Object> responses = queryBookingByJDBC.filterBookingForChartHeat();
        return  responses;
    }

}
