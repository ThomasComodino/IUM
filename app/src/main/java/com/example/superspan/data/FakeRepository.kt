package com.example.superspan.data

object FakeRepository {

    val products = listOf(
        Product(1, "Acqua Naturale 6x1.5L", "Bevande", "Cassa d'acqua naturale.", 2.50),
        Product(2, "Biscotti Frollini 500g", "Dolci", "Ottimi per la colazione.", 3.20),
        Product(3, "Pasta Spaghetti 500g", "Alimentari", "Grano duro 100% italiano.", 1.20),
        Product(4, "Passata di Pomodoro", "Alimentari", "Pomodori italiani.", 1.50)
    )

    // Funzione per il prezzo dinamico (Richiesta per l'aggiornamento automatico)
    fun getFinalPrice(product: Product): Double {
        return if (isPastaCouponActive && product.category == "Alimentari") {
            product.price * 0.85 // Ora 'price' sarà riconosciuto!
        } else {
            product.price
        }
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
    var isCouponPublishedByAdmin: Boolean = true

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