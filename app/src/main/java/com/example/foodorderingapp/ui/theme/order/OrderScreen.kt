package com.example.foodorderingapp.ui.order

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class FoodItem(
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    val category: String,
    val estimatedTime: Int = 15 // in minutes
)

data class OrderItem(
    val foodItem: FoodItem,
    val quantity: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    onBackPressed: () -> Unit = {},
    onOrderPlaced: (List<OrderItem>, String, String, String, Double) -> Unit = { _, _, _, _, _ -> }
) {
    var selectedItems by remember { mutableStateOf(listOf<OrderItem>()) }
    var specialInstructions by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("Credit Card") }
    var deliveryAddress by remember { mutableStateOf("") }
    var isProcessingOrder by remember { mutableStateOf(false) }
    var estimatedDeliveryTime by remember { mutableStateOf(30) }

    val hapticFeedback = LocalHapticFeedback.current

    // Sample food items with estimated preparation times
    val foodItems = remember {
        listOf(
            FoodItem(1, "Margherita Pizza", 12.99, "Fresh tomatoes, mozzarella, basil", "Pizza", 20),
            FoodItem(2, "Chicken Burger", 9.99, "Grilled chicken, lettuce, tomato", "Burgers", 15),
            FoodItem(3, "Caesar Salad", 8.99, "Romaine lettuce, croutons, parmesan", "Salads", 10),
            FoodItem(4, "Pasta Carbonara", 11.99, "Creamy pasta with bacon and egg", "Pasta", 18),
            FoodItem(5, "Fish Tacos", 10.99, "Grilled fish, cabbage, lime crema", "Mexican", 16),
            FoodItem(6, "Chocolate Brownie", 5.99, "Rich chocolate brownie with vanilla ice cream", "Desserts", 5),
            FoodItem(7, "Vegetable Stir Fry", 10.49, "Fresh mixed vegetables with teriyaki sauce", "Asian", 12)
        )
    }

    val totalAmount = selectedItems.sumOf { it.foodItem.price * it.quantity }
    val deliveryFee = if (totalAmount > 25.0) 0.0 else 2.99
    val tax = totalAmount * 0.08
    val discount = if (totalAmount > 50.0) totalAmount * 0.10 else 0.0
    val finalTotal = totalAmount + deliveryFee + tax - discount

    // Calculate estimated delivery time based on selected items
    LaunchedEffect(selectedItems) {
        if (selectedItems.isNotEmpty()) {
            val maxPrepTime = selectedItems.maxOfOrNull { it.foodItem.estimatedTime } ?: 15
            estimatedDeliveryTime = maxPrepTime + 15 // prep time + delivery time
        }
    }

    // Handle order placement
    suspend fun placeOrder() {
        isProcessingOrder = true
        // Simulate order processing delay
        delay(2000)
        isProcessingOrder = false

        // Call the callback with order details
        onOrderPlaced(
            selectedItems,
            deliveryAddress,
            specialInstructions,
            selectedPaymentMethod,
            finalTotal
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8F9FA),
                        Color(0xFFFFFFFF)
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with back button
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFF6B35)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                onBackPressed()
                            },
                            modifier = Modifier
                                .background(
                                    Color.White.copy(alpha = 0.2f),
                                    RoundedCornerShape(8.dp)
                                )
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Your Order",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (selectedItems.isNotEmpty())
                                    "${selectedItems.size} item(s) • Est. $estimatedDeliveryTime min"
                                else "Delicious food awaits!",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 14.sp
                            )
                        }

                        // Cart badge
                        if (selectedItems.isNotEmpty()) {
                            Badge(
                                containerColor = Color.White,
                                contentColor = Color(0xFFFF6B35)
                            ) {
                                Text(
                                    text = selectedItems.sumOf { it.quantity }.toString(),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Promotional banner for free delivery
            if (totalAmount > 0 && totalAmount < 25.0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFEF3C7)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Add $${String.format("%.2f", 25.0 - totalAmount)} for FREE delivery!",
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFD97706)
                                )
                                Text(
                                    text = "Minimum order $25.00",
                                    fontSize = 12.sp,
                                    color = Color(0xFF92400E)
                                )
                            }
                        }
                    }
                }
            }

            // Discount banner for orders over $50
            if (totalAmount >= 50.0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFDCFDF7)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "🎉 You saved $${String.format("%.2f", discount)} with 10% discount!",
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF059669)
                            )
                        }
                    }
                }
            }

            // Food Items Selection
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Items",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )

                    // Clear cart button
                    if (selectedItems.isNotEmpty()) {
                        TextButton(
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                selectedItems = emptyList()
                            }
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Clear Cart")
                        }
                    }
                }
            }

            items(foodItems) { item ->
                AnimatedVisibility(
                    visible = true,
                    enter = slideInHorizontally() + fadeIn(),
                    exit = slideOutHorizontally() + fadeOut()
                ) {
                    FoodItemCard(
                        foodItem = item,
                        quantity = selectedItems.find { it.foodItem.id == item.id }?.quantity ?: 0,
                        onQuantityChanged = { newQuantity ->
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            selectedItems = if (newQuantity == 0) {
                                selectedItems.filter { it.foodItem.id != item.id }
                            } else {
                                val existing = selectedItems.find { it.foodItem.id == item.id }
                                if (existing != null) {
                                    selectedItems.map {
                                        if (it.foodItem.id == item.id) it.copy(quantity = newQuantity) else it
                                    }
                                } else {
                                    selectedItems + OrderItem(item, newQuantity)
                                }
                            }
                        }
                    )
                }
            }

            // Order details section
            if (selectedItems.isNotEmpty()) {
                item {
                    Text(
                        text = "Delivery Details",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            OutlinedTextField(
                                value = deliveryAddress,
                                onValueChange = { deliveryAddress = it },
                                label = { Text("Delivery Address") },
                                placeholder = { Text("Enter your full address") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = Color(0xFFFF6B35)
                                    )
                                },
                                trailingIcon = {
                                    IconButton(
                                        onClick = { /* Open map or current location */ }
                                    ) {

                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }

                // Special Instructions
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            OutlinedTextField(
                                value = specialInstructions,
                                onValueChange = { specialInstructions = it },
                                label = { Text("Special Instructions") },
                                placeholder = { Text("Any special requests? (Optional)") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = Color(0xFFFF6B35)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                minLines = 2
                            )
                        }
                    }
                }

                // Payment Method
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Payment Method",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF2D3748)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            val paymentMethods = listOf("Credit Card", "Debit Card", "Cash on Delivery", "Digital Wallet")

                            paymentMethods.forEach { method ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedPaymentMethod == method,
                                        onClick = { selectedPaymentMethod = method }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = method,
                                        fontSize = 14.sp,
                                        color = Color(0xFF2D3748)
                                    )
                                }
                            }
                        }
                    }
                }

                // Order Summary
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FAFC)),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Order Summary",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3748)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            selectedItems.forEach { orderItem ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("${orderItem.quantity}x ${orderItem.foodItem.name}")
                                    Text("$${String.format("%.2f", orderItem.foodItem.price * orderItem.quantity)}")
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Subtotal")
                                Text("$${String.format("%.2f", totalAmount)}")
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Delivery Fee")
                                Text(
                                    if (deliveryFee == 0.0) "FREE" else "$${String.format("%.2f", deliveryFee)}",
                                    color = if (deliveryFee == 0.0) Color(0xFF10B981) else Color.Unspecified
                                )
                            }
                            if (discount > 0) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Discount (10%)")
                                    Text("-$${String.format("%.2f", discount)}", color = Color(0xFF10B981))
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Tax")
                                Text("$${String.format("%.2f", tax)}")
                            }

                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Total",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = "$${String.format("%.2f", finalTotal)}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFFFF6B35)
                                )
                            }
                        }
                    }
                }

                // Place Order Button
                item {
                    Button(
                        onClick = {
                            if (deliveryAddress.isNotBlank() && !isProcessingOrder) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                // Launch coroutine to handle order placement

                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6B35)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(8.dp),
                        enabled = deliveryAddress.isNotBlank() && !isProcessingOrder
                    ) {
                        if (isProcessingOrder) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Processing...",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Place Order - $${String.format("%.2f", finalTotal)}",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    if (deliveryAddress.isBlank()) {
                        Text(
                            text = "Please enter delivery address to continue",
                            color = Color(0xFFEF4444),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Empty state
            if (selectedItems.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = Color(0xFFE2E8F0),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Your cart is empty",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF718096),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Add some delicious items to get started!",
                                fontSize = 14.sp,
                                color = Color(0xFFA0AEC0),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodItemCard(
    foodItem: FoodItem,
    quantity: Int,
    onQuantityChanged: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = foodItem.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )
                    Text(
                        text = foodItem.description,
                        fontSize = 12.sp,
                        color = Color(0xFF718096),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = foodItem.category,
                            fontSize = 10.sp,
                            color = Color(0xFFFF6B35),
                            modifier = Modifier
                                .background(
                                    Color(0xFFFF6B35).copy(alpha = 0.1f),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${foodItem.estimatedTime} min",
                                fontSize = 10.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "$${String.format("%.2f", foodItem.price)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B35)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (quantity == 0) {
                        Button(
                            onClick = { onQuantityChanged(1) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF6B35)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(
                                text = "Add",
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = { onQuantityChanged(quantity - 1) },
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        Color(0xFFFF6B35).copy(alpha = 0.1f),
                                        RoundedCornerShape(8.dp)
                                    )
                            ) {

                            }

                            Text(
                                text = quantity.toString(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3748)
                            )

                            IconButton(
                                onClick = { onQuantityChanged(quantity + 1) },
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        Color(0xFFFF6B35).copy(alpha = 0.1f),
                                        RoundedCornerShape(8.dp)
                                    )
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = Color(0xFFFF6B35),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    MaterialTheme {
        OrderScreen()
    }
}