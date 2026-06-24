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

    // Dati per Claudia (Profilo)
    val orders = mutableListOf(
        Order("ORD-2024-001", "15/05/2024", 12.50, "Consegnato", listOf(CartItem(products[0], 2), CartItem(products[2], 1))),
        Order("ORD-2024-002", "18/05/2024", 8.40, "In consegna", listOf(CartItem(products[1], 1), CartItem(products[3], 2)))
    )

    val addresses = mutableListOf(
        Address(1, "Casa", "Via Roma 10, Milano"),
        Address(2, "Ufficio", "Corso Buenos Aires 45, Milano")
    )
}