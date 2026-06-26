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
    val expiryDate: String,
    var isActive: Boolean = true,
    val isOnline: Boolean = true,
    val type: String = "SCONTO", // SCONTO, GIFT
    val category: String? = null,
    val discountPercent: Int? = null,
    val productIds: List<Int> = emptyList() // Per i 3 prodotti della campagna GIFT
)

data class JobApplication(
    val candidateName: String,
    val role: String,
    val date: String,
    val location: String = "Milano", // Aggiunto per filtri
    val cvUrl: String = "cv.pdf",
    val videoUrl: String = "video.mp4"
)

data class JobOffer(
    val id: Int,
    val title: String,
    val description: String,
    val location: String,
    val contractType: String,
    var isActive: Boolean = true
)

data class Promotion(
    val id: Int,
    val title: String,
    val category: String,
    val discountPercent: Int,
    val validUntil: String,
    var isActive: Boolean = true
)

data class FavoriteItem(
    val product: Product,
    val savedPrice: Double
)

data class GiftCoupon(
    val id: Int,
    val title: String,
    val options: List<Product>, // I 3 prodotti tra cui scegliere
    var selectedProductId: Int? = null, // Quello scelto da Michele
    var isActivated: Boolean = false
)

// Modelli per Claudia
data class Order(
    val id: String,
    val date: String,
    val total: Double,
    val status: String,
    val items: List<CartItem>
)

data class Address(
    val id: Int,
    val name: String, // Esempio: "Casa", "Ufficio"
    val fullAddress: String
)

