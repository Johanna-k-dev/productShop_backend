package com.greta.productShop.daos;

import com.greta.productShop.entity.Invoice;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class InvoiceDao {
    private final JdbcTemplate jdbcTemplate;

    public InvoiceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Invoice> rowMapper = (rs, rowNum) -> new Invoice(
            rs.getInt("id"),
            rs.getInt("order_id"),
            rs.getDouble("total_amount"),
            rs.getString("date")
    );

    public boolean save(Invoice invoice) {
        String sql = "INSERT INTO invoice (order_id, total_amount, date) VALUES (?, ?, ?)";
        try {
            int rows = jdbcTemplate.update(sql,
                    invoice.getOrderId(),
                    invoice.getTotalAmount(),
                    invoice.getDate());
            return rows > 0;
        } catch (DataAccessException ex) {
            System.err.println("Error while saving invoice: " + ex.getMessage());
            return false;
        }
    }

    public Optional<Invoice> findByOrderId(int orderId) {
        String sql = "SELECT * FROM invoice WHERE order_id = ?";
        try {
            return jdbcTemplate.query(sql, rowMapper, orderId).stream().findFirst();
        } catch (DataAccessException ex) {
            System.err.println("Error while fetching invoice by orderId: " + ex.getMessage());
            return Optional.empty();
        }
    }
}

