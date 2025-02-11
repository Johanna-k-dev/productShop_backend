package com.greta.productShop.daos;

import com.greta.productShop.entity.Invoice;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InvoiceDao implements CrudDao<Invoice> {

    private final JdbcTemplate jdbcTemplate;

    // Constructeur pour injecterJdbcTemplate
    public InvoiceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Sauvegarde une nouvelle facture dans la base de données
    @Override
    public void save(Invoice invoice) {
        String sql = "INSERT INTO invoice (user_id, product_id, order_status) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, invoice.getUserId(), invoice.getProductId(), invoice.getOrderStatus());
    }

    // Met à jour une facture existante
    @Override
    public void update(Invoice invoice) {
        String sql = "UPDATE invoice SET user_id = ?, product_id = ?, order_status = ? WHERE id = ?";
        jdbcTemplate.update(sql, invoice.getUserId(), invoice.getProductId(), invoice.getOrderStatus(), invoice.getId());
    }

    // Méthode pour supprimer une facture par son ID
    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM invoice WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Méthode pour trouver une facture par son ID
    @Override
    public Optional<Invoice> findById(int id) {
        String sql = "SELECT * FROM invoice WHERE id = ?";
        try {
            Invoice invoice = jdbcTemplate.queryForObject(sql, new Object[]{id}, new InvoiceRowMapper());
            return Optional.of(invoice);
        } catch (Exception e) {
            return Optional.empty();  // Retourne un Optional vide si la facture n'existe pas
        }
    }

    // Méthode pour récupérer toutes les factures
    @Override
    public List<Invoice> findAll() {
        String sql = "SELECT * FROM invoice";
        return jdbcTemplate.query(sql, new InvoiceRowMapper());
    }

    // Méthode pour vérifier si une facture existe
    public boolean ifInvoiceExists(int id) {
        String sql = "SELECT COUNT(*) FROM invoice WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    // Classe RowMapper pour mapper les résultats de la requête SQL à l'objet Invoice
    private static class InvoiceRowMapper implements RowMapper<Invoice> {
        @Override
        public Invoice mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            Invoice invoice = new Invoice();
            invoice.setId(rs.getInt("id"));
            invoice.setUserId(rs.getInt("user_id"));
            invoice.setProductId(rs.getInt("product_id"));
            invoice.setOrderStatus(rs.getString("order_status"));
            invoice.setDate(rs.getTimestamp("date").toLocalDateTime().toLocalDate());

            return invoice;
        }
    }
}

