package com.example.superspan.data

object FakeRepository {

    // Il nostro finto catalogo del supermercato
    val products = listOf(
        Product(1, "Acqua Naturale 6x1.5L", "Bevande", "Cassa d'acqua naturale, fonte di montagna.", 2.50),
        Product(2, "Biscotti Frollini 500g", "Dolci", "Ottimi per la colazione di tutti i giorni.", 3.20, 2.50), // Questo ha lo sconto!
        Product(3, "Pasta Spaghetti 500g", "Alimentari", "Grano duro 100% italiano, trafilata al bronzo.", 1.10),
        Product(4, "Passata di Pomodoro", "Alimentari", "Pomodori italiani raccolti e lavorati in giornata.", 1.50)
    )

    // Il carrello dell'utente
    val cart = mutableListOf<CartItem>()

    // Funzione per aggiungere un prodotto al carrello
    fun addToCart(product: Product) {
        val existingItem = cart.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity++ // Se c'è già, aumenta la quantità
        } else {
            cart.add(CartItem(product, 1)) // Altrimenti lo aggiunge nuovo
        }
    }

    // Funzione per calcolare il totale da pagare
    fun getTotal(): Double {
        return cart.sumOf {
            // Se c'è lo sconto usa discountPrice, altrimenti usa price
            val currentPrice = it.product.discountPrice ?: it.product.price
            currentPrice * it.quantity
        }
    }

    var isPastaCouponActive: Boolean = false

    // Dentro FakeRepository.kt
    val applications = listOf(
        JobApplication("Marco Rossi", "Scaffalista", "20/05/2024"),
        JobApplication("Anna Bianchi", "Cassiera", "19/05/2024"),
        JobApplication("Luca Verdi", "Magazziniere", "18/05/2024")
    )
}