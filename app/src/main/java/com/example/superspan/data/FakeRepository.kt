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
}