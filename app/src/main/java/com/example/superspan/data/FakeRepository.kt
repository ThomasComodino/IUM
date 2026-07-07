package com.example.superspan.data

import com.example.superspan.R

object FakeRepository {

    // --- GESTIONE UTENTI ---
    var currentUser: User? = null

    val users = mutableListOf(
        User("Admin", "admin", "admin", isAdmin = true),
        User("Claudia", "claudia@example.com", "password123", 
            addresses = mutableListOf(Address(1, "Casa", "Via Roma 10, Milano")),
            points = 1250
        )
    )

    fun registerUser(user: User): Boolean {
        if (users.any { it.email == user.email }) return false
        users.add(user)
        return true
    }

    fun authenticate(email: String, pass: String): User? {
        val user = users.find { it.email == email && it.password == pass }
        currentUser = user
        return user
    }

    // --- DATI PRODOTTI ---
    val products = listOf(
        Product(1, "Acqua Naturale 6x1.5L", "Bevande", "Cassa d'acqua naturale.", 2.50, imageUrl = R.drawable.acqua_6x1_5l),
        Product(2, "Biscotti Frollini 500g", "Dolci", "Ottimi per la colazione.", 3.20, imageUrl = R.drawable.biscotti_frollini),
        Product(3, "Pasta Spaghetti 500g", "Alimentari", "Grano duro 100% italiano.", 1.20, imageUrl = R.drawable.spaghetti_500g),
        Product(4, "Passata di Pomodoro", "Alimentari", "Pomodori italiani.", 1.50, imageUrl = R.drawable.passata_di_pomodoro),
        Product(5, "Set 3 Padelle Antiaderenti", "Casalinghi", "Padelle di alta qualità per la tua cucina.", 29.90, imageUrl = R.drawable.set_padelle),
        Product(6, "Banane", "Frutta e Verdura", "Banane fresche di categoria extra.", 1.99, imageUrl = R.drawable.banane)
    )

    // --- LOGICA PREZZI E OFFERTE ---
    fun getFinalPrice(product: Product): Double {
        var price = product.price
        
        // 1. Offerte Generali Attive (Promozioni Admin)
        promotions.filter { isPromotionValid(it) }.forEach { promo ->
            if (product.category == promo.category) {
                price *= (1.0 - (promo.discountPercent.toDouble() / 100.0))
            }
        }

        // 2. Coupon Online attivati dall'utente corrente
        currentUser?.let { user ->
            adminCoupons.filter { it.isActive && it.isOnline && it.type == "SCONTO" }.forEach { coupon ->
                if (user.activatedCouponIds.contains(coupon.id) && product.category == coupon.category) {
                    price *= (1.0 - (coupon.discountPercent?.toDouble() ?: 0.0) / 100.0)
                }
            }
        }
        
        return price
    }

    private fun isPromotionValid(promo: Promotion): Boolean {
        if (!promo.isActive) return false
        return try {
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            val expiry = sdf.parse(promo.validUntil)
            val now = sdf.parse(sdf.format(java.util.Date()))
            expiry != null && !expiry.before(now)
        } catch (e: Exception) {
            true
        }
    }

    // --- ACCESSO AI DATI DELL'UTENTE CORRENTE ---
    val cart: MutableList<CartItem> get() = currentUser?.cart ?: mutableListOf()
    val orders: MutableList<Order> get() = currentUser?.orders ?: mutableListOf()
    val addresses: MutableList<Address> get() = currentUser?.addresses ?: mutableListOf()
    val favorites: MutableList<FavoriteItem> get() = currentUser?.favorites ?: mutableListOf()
    val activatedCouponIds: MutableSet<Int> get() = currentUser?.activatedCouponIds ?: mutableSetOf()

    fun addToCart(product: Product) {
        currentUser?.let { user ->
            val existingItem = user.cart.find { it.product.id == product.id }
            if (existingItem != null) {
                existingItem.quantity++
            } else {
                user.cart.add(CartItem(product, 1))
            }
        }
    }

    fun getTotal(): Double {
        return cart.sumOf { item ->
            getFinalPrice(item.product) * item.quantity
        }
    }

    fun toggleFavorite(product: Product) {
        currentUser?.let { user ->
            val existing = user.favorites.find { it.product.id == product.id }
            if (existing != null) {
                user.favorites.remove(existing)
            } else {
                user.favorites.add(FavoriteItem(product, getFinalPrice(product)))
            }
        }
    }

    fun addOrder(items: List<CartItem>, total: Double, address: String, skipAddressSave: Boolean = false) {
        currentUser?.let { user ->
            val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            val date = dateFormat.format(java.util.Date())
            val orderId = "ORD-${System.currentTimeMillis().toString().takeLast(6)}"
            
            val newOrder = Order(orderId, date, total, "In elaborazione", items.toList(), address)
            user.orders.add(0, newOrder)
            
            // Incremento punti (es. 1 punto ogni euro)
            user.points += total.toInt()
            
            // Sincronizzazione indirizzo se nuovo (e non esplicitamente saltata)
            if (!skipAddressSave && user.addresses.none { it.fullAddress.equals(address, ignoreCase = true) }) {
                val newId = (user.addresses.maxOfOrNull { it.id } ?: 0) + 1
                user.addresses.add(Address(newId, "Indirizzo $newId", address))
            }
        }
    }

    // --- FUNZIONI PER ADMIN (STATISTICHE GLOBALI) ---
    fun getTotalOrdersCount(): Int = users.sumOf { it.orders.size }
    fun getTotalApplicationsCount(): Int = applications.size
    fun getActiveCouponsCount(): Int = adminCoupons.count { it.isActive }
    fun getActivePromosCount(): Int = promotions.count { it.isActive }

    // --- DATI GESTIONALI (ADMIN) ---
    val adminCoupons = mutableListOf(
        Coupon(1, "Sconto del 15% su Alimentari", "01/01/2024", false, isOnline = true, type = "SCONTO", category = "Alimentari", discountPercent = 15),
        Coupon(2, "Sconto del 10% su Bevande", "31/12/2026", false, isOnline = true, type = "SCONTO", category = "Bevande", discountPercent = 10),
        Coupon(3, "Regalo di Benvenuto", "31/12/2026", false, isOnline = false, type = "GIFT", productIds = listOf(2, 3, 4)),
        Coupon(4, "Sconto del 20% su Casalinghi", "31/12/2026", false, isOnline = false, type = "SCONTO", category = "Casalinghi", discountPercent = 20)
    )

    val promotions = mutableListOf(
        Promotion(1, "Sconto del 10% su Bevande", "Bevande", 10, "31/12/2026", false),
        Promotion(2, "Sconto del 5% su Dolci", "Dolci", 5, "31/12/2026", false)
    )

    val jobOffers = mutableListOf(
        JobOffer(1, "Cassiera Part-Time", "Gestione cassa e clienti.", "Sesto S.G.", "Part-Time"),
        JobOffer(2, "Scaffalista", "Caricamento scaffali e magazzino.", "Sesto S.G.", "Indeterminato")
    )

    val applications = mutableListOf(
        JobApplication(1, "Marco Rossi", "Scaffalista", "20/05/2024", "Milano Bovisa", jobOfferId = 2),
        JobApplication(2, "Anna Bianchi", "Cassiera", "19/05/2024", "Milano Central", jobOfferId = 1),
        JobApplication(3, "Luca Verdi", "Magazziniere", "18/05/2024", "Milano Bovisa")
    )

    // Coupon Regalo (Mantenuto per compatibilità, ma ora legato all'utente)
    val giftCoupon: GiftCoupon get() = GiftCoupon(
        id = 101,
        title = "Regalo di Benvenuto",
        options = listOf(products[1], products[2], products[3]),
        isActivated = currentUser?.giftCouponActivated ?: false,
        selectedProductId = currentUser?.giftSelectedProductId
    )
}
