package com.example.superspan.data

object FakeRepository {

    val products = listOf(
        Product(1, "Acqua Naturale 6x1.5L", "Bevande", "Cassa d'acqua naturale.", 2.50),
        Product(2, "Biscotti Frollini 500g", "Dolci", "Ottimi per la colazione.", 3.20),
        Product(3, "Pasta Spaghetti 500g", "Alimentari", "Grano duro 100% italiano.", 1.20),
        Product(4, "Passata di Pomodoro", "Alimentari", "Pomodori italiani.", 1.50),
        Product(5, "Set 3 Padelle Antiaderenti", "Casalinghi", "Padelle di alta qualità per la tua cucina.", 29.90)
    )

    // Funzione per il prezzo dinamico (Richiesta per l'aggiornamento automatico)
    fun getFinalPrice(product: Product): Double {
        var price = product.price
        
        // 1. Offerta fissa: -10% su tutte le Bevande
        if (product.category == "Bevande") {
            price *= 0.90
        }
        
        // 2. Coupon Pasta (se attivato da Michele e pubblicato da admin)
        if (isPastaCouponPublished && isPastaCouponActive && product.category == "Alimentari") {
            price *= 0.85
        }
        
        return price
    }

    val cart = mutableListOf<CartItem>()

    fun addToCart(product: Product) {
        val existingItem = cart.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            cart.add(CartItem(product, 1))
        }
    }

    fun getTotal(): Double {
        return cart.sumOf { item ->
            getFinalPrice(item.product) * item.quantity
        }
    }

    var isPastaCouponActive: Boolean = false
    
    // Flag per la pubblicazione individuale dei coupon da parte dell'admin
    var isPastaCouponPublished: Boolean = true
    var isGiftCouponPublished: Boolean = true
    var isShopOnlyCouponPublished: Boolean = true

    var isShopOnlyCouponActive: Boolean = false

    val applications = listOf(
        JobApplication("Marco Rossi", "Scaffalista", "20/05/2024"),
        JobApplication("Anna Bianchi", "Cassiera", "19/05/2024"),
        JobApplication("Luca Verdi", "Magazziniere", "18/05/2024")
    )

    val favorites = mutableListOf<FavoriteItem>()

    fun toggleFavorite(product: Product) {
        val existing = favorites.find { it.product.id == product.id }
        if (existing != null) {
            favorites.remove(existing)
        } else {
            // Salviamo il prodotto insieme al prezzo "congelato" in questo momento
            favorites.add(FavoriteItem(product, getFinalPrice(product)))
        }
    }

    val giftCoupon = GiftCoupon(
        id = 101,
        title = "Regalo di Benvenuto",
        options = listOf(products[1], products[2], products[3]) // Biscotti, Pasta, Passata
    )

    // Dati per Claudia (Profilo) - Inizialmente vuoti o con semi realistici
    val orders = mutableListOf<Order>()
    val addresses = mutableListOf<Address>()

    fun addOrder(items: List<CartItem>, total: Double, address: String) {
        val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        val date = dateFormat.format(java.util.Date())
        val orderId = "ORD-${System.currentTimeMillis().toString().takeLast(6)}"
        
        orders.add(0, Order(orderId, date, total, "In elaborazione", items.toList()))
        
        // Aggiungiamo l'indirizzo se non esiste già
        if (addresses.none { it.fullAddress.equals(address, ignoreCase = true) }) {
            val newId = (addresses.maxOfOrNull { it.id } ?: 0) + 1
            addresses.add(Address(newId, "Indirizzo $newId", address))
        }
    }
}