package com.example.project_sem_4.service.mail.mail_order_detail;

import com.example.project_sem_4.database.dto.order.OrderDetailDTO;
import com.example.project_sem_4.database.entities.Order;
import com.example.project_sem_4.database.entities.OrderDetail;
import com.example.project_sem_4.database.entities.ServiceModel;
import com.example.project_sem_4.database.repository.OrderRepository;
import com.example.project_sem_4.database.repository.ServiceRepository;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionNotFound;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThymeleafMailOrderDetailService {
    private static final String MAIL_TEMPLATE_BASE_NAME = "mail/MailMessages";
    private static final String MAIL_TEMPLATE_PREFIX = "/templates/";
    private static final String MAIL_TEMPLATE_SUFFIX = ".html";
    private static final String UTF_8 = "UTF-8";

    private static final String TEMPLATE_NAME = "mail-order-detail-template";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    private static TemplateEngine templateEngine;

    static {
        templateEngine = emailTemplateEngine();
    }

    private static TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(htmlTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }

    private static ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(MAIL_TEMPLATE_BASE_NAME);
        return messageSource;
    }

    private static ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(MAIL_TEMPLATE_PREFIX);
        templateResolver.setSuffix(MAIL_TEMPLATE_SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(UTF_8);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    public String getContent(String orderDetail) {
        final Context context = new Context();
        Gson gson = new Gson();
        OrderDetailDTO orderDetailDTO = gson.fromJson(orderDetail,OrderDetailDTO.class);

        Order order = orderRepository.findById(orderDetailDTO.getOrder_id()).orElse(null);
        if (order == null){
            throw new ApiExceptionNotFound("orders","id",orderDetailDTO.getOrder_id());
        }

        context.setVariable("name", order.getCustomer().getName());
        context.setVariable("order_id", orderDetailDTO.getOrder_id());

        List<ServiceModel> serviceModels = new ArrayList<>();
        Double total_price = 0.0;

        for (OrderDetailDTO.ListServiceDetailDTO serviceDetailDTO: orderDetailDTO.getOrderDetails()) {
            ServiceModel serviceModel = serviceRepository.findById(serviceDetailDTO.getService_id()).orElse(null);
            if (serviceModel == null){
                throw new ApiExceptionNotFound("services","id",serviceDetailDTO.getService_id());
            }
            total_price += serviceDetailDTO.getUnit_price();
            ServiceModel serviceModelAgain = ServiceModel.builder()
                    .name(serviceModel.getName())
                    .price(serviceDetailDTO.getUnit_price())
                    .build();
            serviceModels.add(serviceModelAgain);
        }

        context.setVariable("detail_services", serviceModels);
        context.setVariable("total_price", total_price);

        return templateEngine.process(TEMPLATE_NAME, context);
    }

}
