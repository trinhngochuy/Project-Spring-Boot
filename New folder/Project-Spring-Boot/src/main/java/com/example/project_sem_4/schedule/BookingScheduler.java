package com.example.project_sem_4.schedule;

import com.example.project_sem_4.database.entities.Booking;
import com.example.project_sem_4.database.entities.Order;
import com.example.project_sem_4.database.repository.BookingRepository;
import com.example.project_sem_4.database.repository.OrderRepository;
import com.example.project_sem_4.service.mail.mail_order_booking.MailOrderBooking;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
@EnableAsync
@Log4j2
public class BookingScheduler {

//	@Scheduled(fixedRate = 1000)
//	public void scheduleFixedRateTask() throws InterruptedException {
//		System.out.println(new Date().toGMTString() + "1 Fixed rate task - " + System.currentTimeMillis() / 1000);
//		// TODO do somethings
//		Thread.sleep(2000);
//		System.out.println(new Date().toGMTString() + "-1 Fixed rate task - " + System.currentTimeMillis() / 1000);
//	}
//
//	@Scheduled(fixedRate = 3000)
//	public void scheduleFixedRateTask2() throws InterruptedException {
//		System.out.println(new Date().toGMTString() + "2 Fixed rate task - " + System.currentTimeMillis() / 1000);
//		// TODO do somethings
//		Thread.sleep(1000);
//		System.out.println(new Date().toGMTString() + "-2 Fixed rate task - " + System.currentTimeMillis() / 1000);
//	}
//	@Async
//	@Scheduled(fixedRate = 1000)
//	public void scheduleFixedRateTaskAsync() throws InterruptedException {
//		System.out.println(new Date().toGMTString()+"--2 Fixed rate task async - " + System.currentTimeMillis() / 1000);
//		Thread.sleep(2000);
//		System.out.println(new Date().toGMTString()+"-->2 Fixed rate task async - " + System.currentTimeMillis() / 1000);
//	}

 // Sau khi chạy 6 giây thì sẽ chỉ chạy 3 giây
//	@Scheduled(fixedDelay = 3000, initialDelay = 6000)
//	public void scheduleFixedRateWithInitialDelayTask() {
//
//		long now = System.currentTimeMillis() / 1000;
//		System.out.println(new Date().toGMTString()+"---3 Fixed rate task with one second initial delay - " + now);
//	}

//	@Scheduled(cron = "* * 12 * * ?") //dinh dang: giay - phut - gio - ngay trong thang - thang - ngay trong tuan
//	public void scheduleTaskUsingCronExpression() {
//
//	    long now = System.currentTimeMillis() / 1000;
//	    System.out.println(new Date().toGMTString()+
//	      "schedule tasks using cron jobs - " + now);
//	}
//
//	@Autowired
//	public BooksService service;
//
//	@Scheduled(fixedRate = 1000)
//	public void scheduleUpdateQuantityOfBooks() throws InterruptedException {
//		// TODO do somethings
//		Thread.sleep(1000);
//		service.updateQuantityOfBook();
//	}
//	@Autowired
//	public MailService mailService;
//	@Autowired
//	public MailLogService mailLogService;
//	@Autowired
//	public BookService bookService;
//
//	@Scheduled(fixedRate = 1000)
//    public void sendSimpleEmailLoginFailed() throws InterruptedException{
//		List<MailLog> listMailLog = mailLogService.getAll();
//		listMailLog.forEach(s -> {
//			if (s.getStatus() == 0){
//				mailService.sendSimpleEmail(s.getEmail(),s.getTitle(), "Lỗi đăng nhập");
//				mailLogService.updateMailLog(s.getId());
//			}
//		});
//		System.out.println("SentEmail");
//		Thread.sleep(12000);
//    }
//
//	@Scheduled(fixedRate = 1000)
//	public void  checkTotalAmountBook() throws InterruptedException{
//		List<Book> bookList = bookService.getAll();
//		bookList.forEach(s -> {
//			System.out.println(s.getName() + "co so luong la: " + s.getTotalAmount());
//		});
//		Thread.sleep(5000);
//	}

	@Autowired
	private BookingRepository bookingRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MailOrderBooking mailOrderBooking;

//    @Scheduled( fixedRate = 1800000)
//    public void checkBookingStatus() throws InterruptedException{
//        SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MM-yyyy");
//
//        Date date = new Date();
//        String formattedDateStr = dmyFormat.format(date);
//        int hour1Tieng = date.getHours() - 1;
////        log.info("Ngày hôm nay là: "+formattedDateStr);
////        log.info("Lúc mấy giờ: "+ date.getHours());
//        //Tìm tất cả order có trạng thái là 1 và booking có trạng thái là 1 và booking hôm nay và có giờ đó
//        List<Order> orders = orderRepository.findOrderByStatusAndBooking_idInHour(formattedDateStr, String.valueOf(hour1Tieng));
//        for (Order order: orders) {
//            String email = order.getCustomer().getEmail();
//            String dateHour = String.valueOf(date.getHours());
//            mailOrderBooking.sendMailOrderBooking(email,dateHour);
//        }
//    }

}
