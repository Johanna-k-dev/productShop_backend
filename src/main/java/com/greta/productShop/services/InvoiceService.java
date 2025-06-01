package com.greta.productShop.services;

import com.greta.productShop.daos.InvoiceDao;
import com.greta.productShop.daos.OrderDao;
import com.greta.productShop.entity.Invoice;
import com.greta.productShop.entity.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class InvoiceService {

    private final InvoiceDao invoiceDao;
    private final OrderDao orderDao;

    public InvoiceService(InvoiceDao invoiceDao, OrderDao orderDao) {
        this.invoiceDao = invoiceDao;
        this.orderDao = orderDao;
    }

    public boolean generateInvoice(int orderId) {
        Optional<Order> orderOpt = orderDao.findOrderById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            Invoice invoice = new Invoice();
            invoice.setOrderId(orderId);
            invoice.setTotalAmount(order.getTotal());
            invoice.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            invoiceDao.save(invoice);
            return true;
        }
        return false;
    }

    public Optional<Invoice> getInvoiceByOrderId(int orderId) {
        return invoiceDao.findByOrderId(orderId);
    }
}
