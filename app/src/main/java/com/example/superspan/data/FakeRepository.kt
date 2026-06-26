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
        
        // Applichiamo i coupon online attivati
        adminCoupons.filter { it.isActive && it.isOnline && it.type == "SCONTO" }.forEach { coupon ->
            if (activatedCouponIds.contains(coupon.id) && product.category == coupon.category) {
                price *= (1.0 - (coupon.discountPercent?.toDouble() ?: 0.0) / 100.0)
            }
        }
        
        return price
    }

    val cart = mutableListOf<CartItem>()
    val activatedCouponIds = mutableSetOf<Int>()

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

    // Elenco Coupon gestiti dall'admin
    val adminCoupons = mutableListOf(
        Coupon(1, "Sconto del 15% su Alimentari", "01/01/2024", true, isOnline = true, type = "SCONTO", category = "Alimentari", discountPercent = 15),
        Coupon(2, "Sconto del 10% su Bevande", "31/12/2026", true, isOnline = true, type = "SCONTO", category = "Bevande", discountPercent = 10),
        Coupon(3, "Regalo di Benvenuto", "31/12/2026", true, isOnline = false, type = "GIFT", productIds = listOf(2, 3, 4)),
        Coupon(4, "Sconto del 20% su Casalinghi", "31/12/2026", true, isOnline = false, type = "SCONTO", category = "Casalinghi", discountPercent = 20)
    )

    val promotions = mutableListOf(
        Promotion(1, "Sconto Bevande", "Bevande", 10, "31/12/2024"),
        Promotion(2, "Sconto Colazione", "Dolci", 5, "15/06/2024")
    )

    val jobOffers = mutableListOf(
        JobOffer(1, "Cassiera Part-Time", "Gestione cassa e clienti.", "Milano Central", "Determinato"),
        JobOffer(2, "Scaffalista", "Caricamento scaffali e magazzino.", "Milano Bovisa", "Indeterminato")
    )

    val applications = mutableListOf(
        JobApplication("Marco Rossi", "Scaffalista", "20/05/2024", "Milano Bovisa"),
        JobApplication("Anna Bianchi", "Cassiera", "19/05/2024", "Milano Central"),
        JobApplication("Luca Verdi", "Magazziniere", "18/05/2024", "Milano Bovisa")
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