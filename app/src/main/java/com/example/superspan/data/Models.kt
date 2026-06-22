package com.example.superspan.data

// Modello per il singolo Prodotto
data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val description: String,
    val price: Double, // Prezzo di listino sempre fisso
    var isFavorite: Boolean = false,
    val imageUrl: Int = android.R.drawable.ic_menu_gallery
)

// Modello per il prodotto dentro il carrello (con la quantità)
data class CartItem(
    val product: Product,
    var quantity: Int
)

// Modello per i Coupon (Area Michele)
data class Coupon(
    val id: Int,
    val title: String,
    val requiredItemsCount: Int,
    var isActive: Boolean
)

data class JobApplication(
    val candidateName: String,
    val role: String,
    val date: String
)

