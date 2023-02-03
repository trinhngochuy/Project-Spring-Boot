package com.example.project_sem_4.seed;
import com.example.project_sem_4.database.dto.ServiceDTO;
import com.example.project_sem_4.database.entities.*;
import com.example.project_sem_4.database.repository.*;
import com.example.project_sem_4.enum_project.GenderEnum;
import com.example.project_sem_4.enum_project.RoleEnum;
import com.example.project_sem_4.enum_project.StatusEnum;
import com.example.project_sem_4.enum_project.TimeBookingEnum;
import com.example.project_sem_4.service.authen.AuthenticationService;
import com.example.project_sem_4.service.authen.RoleService;
import com.example.project_sem_4.service.service.ServiceHair;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SeedTest implements CommandLineRunner {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    MembershipClassRepository membershipClassRepository;

    @Autowired
    TypeServiceRepository typeServiceRepository;
    @Autowired
    RoleService roleService;
    @Autowired
    ServiceHair serviceHair;

    private boolean createSeed = true;

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ServiceRepository serviceModelRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private OrderDetailRepository orderDetailRepository;




    @Override
    public void run(String... args) throws Exception {
        Random rand = new Random();
        Calendar cal = Calendar.getInstance();
        String sql;
        if (createSeed) {
            List<Branch> branches = branchRepository.findAll();
            if(branches.size() == 0){
                Integer[] statuss = {0, StatusEnum.ACTIVE.status};

                String hotLine = "0773776942";

                    String thumbnail1 = "https://lawkey.vn/wp-content/uploads/2016/10/72358PICV9G.jpg";
                    String address1 = "Hà Nội";
                    String name1 = "Cơ Sở Tôn Đức Thắng";


                String thumbnail2 = "https://2doctor.org/wp-content/uploads/2021/08/dia-chi-cat-toc-nam-dep-o-ha-noi.jpg";
                String address2 = "Hồ Chí Minh";
                String name2 = "Cơ Sở Hoàng Quốc Việt";

                    Date dateBranch = getRamdomDate(2020, 2020, "yyyy-MM-dd");
                    Integer status = statuss[rand.nextInt(statuss.length)];
                    String bookingDateBranch = String.valueOf(dateBranch.getDate());
                    if (dateBranch.getDate() < 10) {
                        bookingDateBranch = 0 + String.valueOf(dateBranch.getDate());
                    }

                    int validateMonthBranch = dateBranch.getMonth() + 1;
                    String bookingMonthBranch = String.valueOf(validateMonthBranch);
                    if (validateMonthBranch < 10) {
                        bookingMonthBranch = 0 + String.valueOf(validateMonthBranch);
                    }

                    cal.setTime(dateBranch);
                    String bookingYearBranch = String.valueOf(cal.get(Calendar.YEAR));

                   String sql1 = "INSERT INTO branchs VALUES (" +
                       1 + ","+
                           '"' + bookingYearBranch + "-" + bookingMonthBranch + "-" + bookingDateBranch + '"' + ","+
                           status + ","+
                           '"' + bookingYearBranch + "-" + bookingMonthBranch + "-" + bookingDateBranch + '"' + ","+
                            '"' + address1+ '"' + ","+
                            '"' + hotLine+ '"' + ","+
                            '"' + name1+ '"' + ","+
                            '"' + thumbnail1+ '"' + ")"
                   ;
                   jdbcTemplate.update(
                           sql1);
                String sql2 = "INSERT INTO branchs VALUES (" +
                        2 + ","+
                        '"' + bookingYearBranch + "-" + bookingMonthBranch + "-" + bookingDateBranch + '"' + ","+
                        status + ","+
                        '"' + bookingYearBranch + "-" + bookingMonthBranch + "-" + bookingDateBranch + '"' + ","+
                        '"' + address2+ '"' + ","+
                        '"' + hotLine+ '"' + ","+
                        '"' + name2+ '"' + ","+
                        '"' + thumbnail2+ '"' + ")"
                        ;
                jdbcTemplate.update(
                        sql2);

            }

            MembershipClass membershipClass = membershipClassRepository.findById(1).orElse(null);
            if (membershipClass == null) {
                membershipClassRepository.save(MembershipClass.builder().name("Hạng S").build());
                membershipClassRepository.save(MembershipClass.builder().name("Hạng A").build());
                membershipClassRepository.save(MembershipClass.builder().name("Hạng B").build());
                membershipClassRepository.save(MembershipClass.builder().name("Hạng C").build());
                membershipClassRepository.save(MembershipClass.builder().name("Hạng D").build());

                roleRepository.save(Role.builder().name(RoleEnum.ADMIN.role).build());
                roleRepository.save(Role.builder().name(RoleEnum.RECEPTIONISTS.role).build());
                roleRepository.save(Role.builder().name(RoleEnum.STAFF.role).build());
                roleRepository.save(Role.builder().name(RoleEnum.CUSTOMER_CARE.role).build());
                roleRepository.save(Role.builder().name(RoleEnum.CUSTOMER.role).build());
            }

            Account Walk_In_Guest = accountRepository.findById(1).orElse(null);
            if (Walk_In_Guest == null) {
                authenticationService.saveWalk_In_Guest();
                createAccount("ADMIN");
                createAccount("RECEPTIONISTS");
                createAccount("STAFF1");
                createAccount("CUSTOMER_CARE");
                createAccount("CUSTOMER");
                createAccount("STAFF2");
                createAccount("STAFF3");
                createAccount("STAFF4");
            }


            List<TypeService> typeServices = typeServiceRepository.findAll();
            if (typeServices.size() == 0) {
                typeServiceRepository.save(new TypeService(1,"Cắt Tóc", 1));
                typeServiceRepository.save(new TypeService(2,"Gội đầu", 1));
                typeServiceRepository.save(new TypeService(3,"Massage", 1));
                typeServiceRepository.save(new TypeService(4,"Nhuộm tóc", 1));
                typeServiceRepository.save(new TypeService(5,"Chăm sóc da", 1));

                serviceHair.createService(ServiceDTO.builder().name("Uốn Hàn Quốc 8 cấp độ")
                        .description("Thuốc uốn đầu tiên trên thế giới loại bỏ thành phần Hydrochloride khỏi Cysteamine không gây hại da đầu, cam kết không gây rụng tóc.")
                        .thumbnail("https://storage.30shine.com/ResourceWeb/data/images/Service/uon-han-quoc/ep-side.jpg")
                        .typeServiceId(1).price(300000.0)
                        .build());
                serviceHair.createService(ServiceDTO.builder().name("Nhuộm màu thời trang")
                        .description("Nhuộm màu thời trang\n" +
                                "Bảng màu mới chia làm 4 gói nhuộm theo tone màu khác nhau phù hợp với từng đối tượng đặc biệt: Elegant Black, Modern Man, Lady Killah và Fashionisto\n" +
                                "Với gói màu Modern Man này, 30Shine muốn hướng tới một màu đen classic, đem đến sự thanh lịch, tút lại phong độ cho người đàn ông Việt Nam")
                        .thumbnail("https://storage.30shine.com/ResourceWeb/data/images/landingpage/nhuom/nau/30shine-Shinecolor-nhuom-nau-cao-cap-dinh-hinh-nguoi-dan-ong-hien-dai-2.jpg")
                        .typeServiceId(4).price(350000.0)
                        .build());

                serviceHair.createService(ServiceDTO.builder().name("Massage cổ vai gáy")
                        .description("Massage cổ vai gáy cam ngọt, chín tầng mây")
                        .thumbnail("https://storage.30shine.com/ResourceWeb/data/images/Service/tat-ca-dich-vu/20220105-massage-co-vai-gay.jpg")
                        .typeServiceId(3).price(400000.0)
                        .build());
                serviceHair.createService(ServiceDTO.builder().name("Lột mụn đầu đen")
                        .description("Combo lột mun đầu đen full face, đắp mặt nạ, tẩy da chết.")
                        .thumbnail("https://storage.30shine.com/ResourceWeb/data/images/Service/tat-ca-dich-vu/30shine-lot-mun-cam-3.jpg?v=2")
                        .typeServiceId(5).price(85000.0)
                        .build());
                serviceHair.createService(ServiceDTO.builder().name("Chăm sóc cấp thiết, ultra white")
                        .description("Đắp mặt nạ lạnh, tẩy da chết sủi.")
                        .thumbnail("https://storage.30shine.com/ResourceWeb/data/images/Service/tat-ca-dich-vu/30shine-detox-3.jpg?v=2https://storage.30shine.com/ResourceWeb/data/images/Service/tat-ca-dich-vu/mat-na-sui-bot-tay-da-chet-3.jpg?v=2")
                        .typeServiceId(5).price(50000.0)
                        .build());
                serviceHair.createService(ServiceDTO.builder().name("Gội đầu dưỡng sinh Đài Loan")
                        .description("Gội đầu dưỡng sinh Đài Loan là phương pháp gội đầu nhằm làm sạch tóc và da đầu và thư giãn với kỹ thuật massage, day ấn tập trung vào các huyệt đạo giúp hệ thần kinh, hệ tuần hoàn bạch huyết được lưu thông, độc tố được đào thải, cơ thể được thư giãn và sức khỏe được tăng cường.")
                        .thumbnail("https://perlaspa.com.vn/wp-content/uploads/2021/01/goi-dau-duong-sinh-2.jpg")
                        .typeServiceId(2).price(300000.0)
                        .build());
                serviceHair.createService(ServiceDTO.builder().name("Gội đầu Detox tinh chất muối biển")
                        .description("Sử dụng muối biển sâu tự nhiên, vô trùng độ tinh khiết 100% giúp làm sạch sâu da đầu, sát khuẩn nhẹ loại bỏ hoàn toàn lớp dầu thừa, phần da chết trên da đầu.")
                        .thumbnail("https://amoon.vn/wp-content/uploads/2020/02/dich-vu-goi-dau.png")
                        .typeServiceId(2).price(150000.0)
                        .build());
                serviceHair.createService(ServiceDTO.builder().name("Cut Luxury")
                        .description("Tư vấn skincare, xông hơi mặt, cắt xả và tạo kiểu tóc bằng các sản phẩm cao cấp.")
                        .thumbnail("http://luxuryman.vn/Content/image/article.jpg")
                        .typeServiceId(1).price(200000.0)
                        .build());

            }

            List<ServiceModel> services = serviceModelRepository.findAll();
        if (services.size() == 0){
            String[] link = {"https://i.pinimg.com/236x/47/ae/24/47ae2447e4cd688098398f6c8687bea0.jpg",
            "https://i.pinimg.com/236x/35/e5/a8/35e5a8cb6c8f31599b6cdff138ba13ef.jpg",
            "https://i.pinimg.com/236x/6c/93/d6/6c93d61f013b9e7ec3ea47f998574e7e.jpg",
            "https://i.pinimg.com/236x/83/4e/f6/834ef6a7e9cbd9388c3b2345af769ef3.jpg",
            "https://i.pinimg.com/236x/c3/ea/23/c3ea233f4cd95b9c6c9ee0bc0212d938.jpg"};
            Integer status = 1;
            String description = "Kiểu tóc nam,nữ đẹp ";
            String name = "Đầu cắt moi";
            double[] price = {200000,300000,400000,500000};
            Integer[] type = {1,2,3,4,5};
            for (int i = 1; i < 20; i++) {
                Date dateservice = getRamdomDate(2020, 2022, "yyyy-MM-dd");
                String linkz = link[rand.nextInt(link.length)];
                double pricez = price[rand.nextInt(price.length)];
                Integer typez = type[rand.nextInt(type.length)];

                String bookingDateService = String.valueOf(dateservice.getDate());
                if (dateservice.getDate() < 10) {
                    bookingDateService = 0 + String.valueOf(dateservice.getDate());
                }

                int validateMonthService = dateservice.getMonth() + 1;
                String bookingMonthService = String.valueOf(validateMonthService);
                if (validateMonthService < 10) {
                    bookingMonthService = 0 + String.valueOf(validateMonthService);
                }
                 cal = Calendar.getInstance();
                cal.setTime(dateservice);
                String bookingYearService = String.valueOf(cal.get(Calendar.YEAR));

                 sql = "INSERT INTO services VALUES (" +
                       i + ","+
                        '"' + bookingYearService + "-" + bookingMonthService + "-" + bookingDateService + '"' + ","+
                        status + ","+
                        '"' + bookingYearService + "-" + bookingMonthService + "-" + bookingDateService + '"' + ","+
                        '"'+description+'"' + ","+
                        '"'+name+'"' + ","+
                       pricez+ ","+
                        '"'+ linkz+'"'+ ","+
                        typez+")";
                jdbcTemplate.update(sql);
            }
        }

        List<Booking> booking = bookingRepository.findAll();
        if (booking.size() == 0) {
            Integer[] branch_ids = {1, 2};
//            Integer[] emp_ids = {2, 3, 4, 5, 6, 7, 8};
            Integer[] emp_ids = {4};
            Integer[] service_names = {1,2,3,4,5,6,7,8};

            TimeBookingEnum[] time_bookings = TimeBookingEnum.values();
//            Integer[] user_ids = {5};
            Integer[] user_ids = {1,6,7,8,9};
            Integer user_ids_zero = 0;
            Integer[] statuss = {-1, 0, 1, 2};

            for (int i = 0; i < 600; i++) {
                Integer status = statuss[rand.nextInt(statuss.length)];
                Date date = getRamdomDate(2020, 2022, "yyyy-MM-dd");
                Integer branch_id = branch_ids[rand.nextInt(branch_ids.length)];
                Integer emp_id = emp_ids[rand.nextInt(emp_ids.length)];
                Integer service_name = service_names[rand.nextInt(service_names.length)];
                String time_booking = time_bookings[rand.nextInt(time_bookings.length)].timeName;
                Integer user_id = user_ids[rand.nextInt(user_ids.length)];
                String email = "Seeder@gmail.com";

                String bookingDate = String.valueOf(date.getDate());
                if (date.getDate() < 10) {
                    bookingDate = 0 + String.valueOf(date.getDate());
                }

                int validateMonth = date.getMonth() + 1;
                String bookingMonth = String.valueOf(validateMonth);
                if (validateMonth < 10) {
                    bookingMonth = 0 + String.valueOf(validateMonth);
                }
                 cal = Calendar.getInstance();
                cal.setTime(date);
                String bookingYear = String.valueOf(cal.get(Calendar.YEAR));

                 sql = "INSERT INTO bookings (id,created_at,status,updated_at,branch_id,date,date_booking,email,employee_id,phone,time_booking,name_booking,user_id) VALUES (" +
                        '"' +  "HN" +i+ '"' + ","+
                        '"' + bookingYear + "-" + bookingMonth + "-" + bookingDate + '"' + ","+
                        status + ","+
                        '"' + bookingYear + "-" + bookingMonth + "-" + bookingDate + '"' + ","+
                        branch_id + ","+
                        '"' + String.valueOf(date.getTime()) + '"'  + ","+
                        '"' +bookingDate + "-" + bookingMonth + "-" + bookingYear + '"' + ","+
                        '"' +email+ '"' + ","+
                        emp_id + ","+
                        "''" + ","+
                         '"' + time_booking + '"' + ","+
                         service_name+ ","+
                         user_id + ")"
                        ;
                jdbcTemplate.update(
                        sql);
            }
            for (int i = 601; i < 631; i++) {
                Integer status = statuss[rand.nextInt(statuss.length)];
                Date date = getRamdomDate(2021, 2022, "yyyy-MM-dd");
                Integer branch_id = branch_ids[rand.nextInt(branch_ids.length)];
                Integer service_name = service_names[rand.nextInt(service_names.length)];
                Integer emp_id = emp_ids[rand.nextInt(emp_ids.length)];

                String time_booking = time_bookings[rand.nextInt(time_bookings.length)].timeName;
                Integer user_id = user_ids_zero;
                String email = "Seeder@gmail.com";

                String bookingDate = String.valueOf(date.getDate());
                if (date.getDate() < 10) {
                    bookingDate = 0 + String.valueOf(date.getDate());
                }

                int validateMonth = date.getMonth() + 1;
                String bookingMonth = String.valueOf(validateMonth);
                if (validateMonth < 10) {
                    bookingMonth = 0 + String.valueOf(validateMonth);
                }
                cal = Calendar.getInstance();
                cal.setTime(date);
                String bookingYear = String.valueOf(cal.get(Calendar.YEAR));

                sql = "INSERT INTO bookings (id,created_at,status,updated_at,branch_id,date,date_booking,email,employee_id,phone,time_booking,name_booking,user_id) VALUES (" +
                        '"' +  "HN" +i+ '"' + ","+
                        '"' + bookingYear + "-" + bookingMonth + "-" + bookingDate + '"' + ","+
                        status + ","+
                        '"' + bookingYear + "-" + bookingMonth + "-" + bookingDate + '"' + ","+
                        branch_id + ","+
                        '"' + String.valueOf(date.getTime()) + '"'  + ","+
                        '"' +bookingDate + "-" + bookingMonth + "-" + bookingYear + '"' + ","+
                        '"' +email+ '"' + ","+
                        emp_id + ","+
                        "''" + ","+
                        '"' + time_booking + '"' + ","+
                        service_name+ ","+
                        user_id + ")"
                ;
                jdbcTemplate.update(
                        sql);
            }
        }
        List<Order> orders = orderRepository.findAll();
        if (orders.size() == 0 && booking.size()> 0) {

            Integer[] statussOrder = {-1,0,1,2};
            Integer[] customer_ids = {1, 6};
            double[] pricesOrder = {200000,300000,400000,500000};

            for (int i = 1; i < 100; i++) {
                Date dateorder = getRamdomDate(2020, 2022, "yyyy-MM-dd");
                Booking bookin =  booking.get(rand.nextInt(booking.size()));
                String booking_id = bookin.getId();
                Integer customer_id = customer_ids[rand.nextInt(customer_ids.length)];
                Double priceOrder = pricesOrder[rand.nextInt(pricesOrder.length)];
                Integer statusOrder = statussOrder[rand.nextInt(statussOrder.length)];

                String bookingDateOrder = String.valueOf(dateorder.getDate());
                if (dateorder.getDate() < 10) {
                    bookingDateOrder = 0 + String.valueOf(dateorder.getDate());
                }

                int validateMonthOrder = dateorder.getMonth() + 1;
                String bookingMonthOrder = String.valueOf(validateMonthOrder);
                if (validateMonthOrder < 10) {
                    bookingMonthOrder = 0 + String.valueOf(validateMonthOrder);
                }
                 cal = Calendar.getInstance();
                cal.setTime(dateorder);
                String bookingYearOrder = String.valueOf(cal.get(Calendar.YEAR));

                 sql = "INSERT INTO orders VALUES (" +
                        i + ","+
                        '"' + bookingYearOrder + "-" + bookingMonthOrder + "-" + bookingDateOrder + '"' + ","+
                        statusOrder + ","+
                        '"' + bookingYearOrder + "-" + bookingMonthOrder + "-" + bookingDateOrder + '"' + ","+
                        '"'+booking_id+'"' + ","+
                        customer_id + ","+
                        priceOrder + ","+ "''" +
                          ")";
                jdbcTemplate.update(
                        sql);
            }
        }
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        if(orderDetails.size() == 0 && orders.size() > 0 && services.size() > 0){
            for (int i = 0; i < orders.size(); i++) {
                Order order =  orders.get(i);
                int numberLoop = rand.nextInt(4);
                numberLoop = numberLoop == 0 ? 1 : numberLoop;
                Integer order_id = order.getId();
               for(int j = 1;j <= numberLoop;j++){
                   ServiceModel serviceModel = services.get(rand.nextInt(services.size()));
                   Integer service_id = serviceModel.getId();
                   double unit_price = serviceModel.getPrice();
                   sql = "SELECT count(*) FROM order_details WHERE order_id = ? AND service_id = ?";

                   int count = jdbcTemplate.queryForObject(sql, new Object[] { order_id,service_id }, Integer.class);
                   if (count == 0){
                       sql = "INSERT INTO order_details VALUES (" +
                               order_id + ","+
                               service_id + ","+
                               unit_price +")";
                       jdbcTemplate.update(
                               sql);
                   }
               }
               }
              }


        }
    }

    private void createAccount(String checkAccount){
        MembershipClass membershipClass = membershipClassRepository.findById(5).orElse(null);

        Account account = new Account();
        String name;
        String email;
        String phone;
        String description;
        String roleName;
        switch (checkAccount) {
                case "ADMIN":
                name = "Admin";
                roleName = "ADMIN";
                description = "Quản trị viên ";
                email = "admin@gmail.com";
                phone = "0123523532";
                break;
            case "RECEPTIONISTS":
                name = "Receptionists";
                roleName = "RECEPTIONISTS";
                description = "Lễ tân";
                email = "receptionists@gmail.com";
                phone = "43241414141532";
                break;
            case "STAFF1":
                name = "Staff1";
                roleName = "STAFF";
                description = "Trước đây, mọi người thường nghĩ đàn ông không cần quá quan trọng ngoại hình, xuề xoà là một nét đặc trưng của đàn ông. Nhưng ở thời đại bây giờ thì mọi thứ khác rồi. Không chỉ phụ nữ mà đàn ông cũng nên chăm chút ngoại hình. Ngoại hình là thứ ảnh hưởng rất nhiều tới các mối quan hệ và công việc, cuộc sống và kể từ khi mình nhận ra điều đó thì mình dần quan tâm chăm sóc ngoại hình bản thân nhiều hơn.Bạn sinh ra đã đẹp thì là một điều tốt, nhưng nếu như mình chưa có thì không có nghĩa mình mãi mãi không có được. Có rất nhiều cách để khiến mình trở nên đẹp hơn như tập gym, thời trang và kiểu tóc";
                email = "staff1@gmail.com";
                phone = "0214124142";
                break;
            case "STAFF2":
                name = "Staff2";
                roleName = "STAFF";
                description = "Nghệ thuật không bao giờ có điểm dừng. Mình hiểu rất rõ điều này và luôn tỉ mỉ trong từng đường kéo.Với tay nghề chuẩn quốc tế, cùng với sự am hiểu về mái tóc, con người Việt, chắc chắn các mình sẽ cắt cho bạn kiểu tóc ưng ý nhất.Không những thế, cắt Sport ở tiệm, bạn còn được vuốt sáp tạo kiểu miễn phí không nơi nào có, hướng dẫn vuốt tại nhà nhanh, dễ dàng và đẹp nhất.";
                email = "staff2@gmail.com";
                phone = "0214124143";
                break;
            case "STAFF3":
                name = "Staff3";
                roleName = "STAFF";
                description = "Tôi thấy có ngoại hình đẹp sáng sủa sẽ giúp đàn ông tự tin hơn rất nhiều. Nam giới Việt Nam đẹp trai chứ, nhưng chưa được định hình phong cách, chưa có người tư vấn, dẫn dắt xu hướng. Thậm chí rất hiếm luôn. Tôi rất khao khát giúp ae trở thành phiên bản tốt hơn, nên có động lực đi tìm trend tóc, tìm phong cách cho ae nam giới Việt. Vì vậy mà tôi đã thử rất nhiều kiểu tóc, màu tóc, thu nạp đc rất nhiều kiến thức và đã có tôi của ngày hôm nay." +
                        "Hành trình để đi tìm những Trend tóc đang là xu hướng, những Trend tóc mới cho ae phải nói là cũng gặp khá nhiều gian nan, khó khăn bởi một chút lơ là thôi thì cái Trend đó đã nguội hay là giảm độ nóng độ hot rồi. Nên bắt buộc bạn phải cực kì nhạy cảm. Tôi còn nhớ trend tóc con sâu từ Hàn Quôc tràn về VN, tôi và anh em stylist tại thức cả đêm để nghiên cứu kỹ thuật, làm sao vẫn giữ được nét đặc trưng của kiểu tóc mà vẫn phù hợp với khuôn mặt, chất tóc nam giới Việt. Có những lúc thử đi thử lại vẫn chưa ưng ý còn cãi nhau to, nhưng khi xong thì mấy ae vui lắm,...";
                email = "staff3@gmail.com";
                phone = "0214124256";
                break;
            case "STAFF4":
                name = "Staff4";
                roleName = "STAFF";
                description = "Việc thay đổi kiểu tóc giúp tôi nhanh tìm được phiên bản hoàn thiện nhất của mình, nhờ vậy mà tôi cũng nhạy cảm hơn trong việc bắt Trend. tôi đã tẩy tóc khoảng mấy chục lần rồi còn nhuộm thì rất nhiều lần chứ tôi cũng không nhớ rõ để đưa ra con số cụ thể. Mục đích là để vừa trải nghiệm, vừa thử kiểu tóc, màu tóc mới và show ra cho anh em tham khảo." +
                        "Ko phải đơn thuần là kiểu tóc này nó nhiều màu, kiểu này xoăn tít lên thì là Trend, mà Trend là những kiểu tóc ở thời điểm đó đang thực sự được nhiều ae chú ý tới, nhiều ae để cái kiểu tóc ấy, lạ mắt, độc đáo nhưng ứng dụng cao, phù hợp với đa số anh em nam giới.\n" +
                        "Ví dụ như kiểu tóc Mohican đình đám năm ngoái hay gần đây nhất là kiểu tóc uốn con sâu. Kiểu tóc này có thể hot ở thế giới, tây để rất đẹp nhưng với gương mặt, chất tóc của người Việt chưa chắc đã hợp nên tôi và các chuyên gia tóc ở tiệm phải nghiên cứu rất nhiều, thử nghiệm trên chính mái tóc của tôi và các anh em đến khi nào ra chuẩn thì thôi.\n" +
                        "Tuy khó khăn nhưng làm vì đam mê vì ae thôi, nên tôi cũng đã theo đuổi cũng từ rất lâu rồi. Quan trọng là anh em đẹp hơn, anh em có chỗ dựa tin tưởng để luôn luôn bắt kịp những xu hướng tóc mới. Ko bao giờ sợ lỗi thời.";
                email = "staff4@gmail.com";
                phone = "0214124589";
                break;
            case "CUSTOMER_CARE":
                name = "Customer Care";
                roleName = "CUSTOMER_CARE";
                description = "Nhân viên chăm sóc";
                email = "customer_care@gmail.com";
                phone = "543564312";
                break;
            case "CUSTOMER":
                name = "Customer";
                roleName = "CUSTOMER";
                description = "Khách hàng";
                email = "customer@gmail.com";
                phone = "464314141";
                break;
            default:
                name = "";
                roleName = "CUSTOMER";
                description = "";
                email = "";
                phone = "";
                break;
                }
        Set<Role> roles = new HashSet<>();
        Role role = roleService.getRole(roleName);
        roles.add(role);

        account.setName(name);
        account.setDescription(description);
        account.setEmail(email);
        account.setAddress("From No Where");
        account.setPhone(phone);
        account.setPassword(passwordEncoder.encode("12345678"));
        account.setGender(GenderEnum.MALE.gender);
        account.setMembershipClass(membershipClass);
        account.setRoles(roles);
        account.setCreated_at(new Date());
        account.setStatus(StatusEnum.ACTIVE.status);
        accountRepository.save(account);
    }

    public static int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    public Date getRamdomDate(int yearFrom, int yearEnd, String format) throws ParseException {
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(yearFrom, yearEnd);
        gc.set(gc.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
        gc.set(gc.DAY_OF_YEAR, dayOfYear);
        String created_at = gc.get(gc.YEAR) + "-" + (gc.get(gc.MONTH) + 1) + "-" + gc.get(gc.DAY_OF_MONTH);
        //"yyyy-MM-dd"
        return new SimpleDateFormat(format).parse(created_at);
    }
}
