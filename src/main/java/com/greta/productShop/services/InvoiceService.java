package com.greta.productShop.services;

import com.greta.productShop.daos.InvoiceDao;
import com.greta.productShop.daos.ProductDao;
import com.greta.productShop.daos.UserDao;
import com.greta.productShop.entity.Invoice;
import com.greta.productShop.entity.User;
import com.greta.productShop.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceDao invoiceDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    // Constructeur pour injecter les DAO
    public InvoiceService(InvoiceDao invoiceDao, UserDao userDao, ProductDao productDao) {
        this.invoiceDao = invoiceDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // Méthode pour générer une facture complète
    public Invoice generateInvoice(int invoiceId) {
        // Étape 1 : Récupérer les informations de la facture
        Invoice invoice = invoiceDao.findById(invoiceId).orElseThrow(() -> new RuntimeException("Invoice not found"));

        // Étape 2 : Récupérer l'utilisateur lié à cette facture
        User user = userDao.findById(invoice.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        // Étape 3 : Récupérer les produits associés à cette facture (en fonction de productId)
        Product product = productDao.findById(invoice.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));

        // Étape 4 : Ajouter l'utilisateur et le produit à la facture
        invoice.setOrderStatus("Paid");  // Exemple d'état, à personnaliser
        invoice.setUser(user);  // Ajouter l'utilisateur à la facture
        invoice.setProduct(product);  // Ajouter le produit à la facture

        // Ajouter un champ quantity dans Invoice pour la quantité de chaque produit acheté
        int quantity = 2; // Exemple d'achat de 2 produits (tu peux récupérer cette info à partir de la base)

        // Calculer le total par produit
        double productTotal = product.getPrice() * quantity;  // Calcul pour un produit spécifique

        // Calculer le total global de la facture (si tu as plusieurs produits, fais ça dans une boucle)
        double totalAmount = productTotal;  // Pour le moment, on n'a qu'un seul produit, donc total = productTotal

        // Étape 5 : Ajouter le total calculé à la facture
        invoice.setTotalAmount(totalAmount); // Exemple d'ajout du total de la facture

        // Retourner la facture complète
        return invoice;
    }
}
