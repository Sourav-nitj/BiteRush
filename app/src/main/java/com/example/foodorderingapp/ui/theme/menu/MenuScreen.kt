package com.example.foodorderingapp.ui.menu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class MenuItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val rating: Float,
    val preparationTime: String,
    val isPopular: Boolean = false,
    val isVegetarian: Boolean = false,
    val isSpicy: Boolean = false
)

data class CartItem(
    val menuItem: MenuItem,
    val quantity: Int
)

data class MenuCategory(
    val name: String,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onOrderClick: () -> Unit = {},
    onBackPressed: () -> Unit = {},
    onLogout: (() -> Unit)? = null
) {
    // Handle back press properly
    BackHandler {
        onBackPressed()
    }

    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }

    // Sample menu data
    val menuItems = remember {
        listOf(
            MenuItem(1, "Margherita Pizza", "Fresh tomatoes, mozzarella, basil, olive oil", 12.99, "Pizza", 4.5f, "15-20 min", true, true),
            MenuItem(2, "Pepperoni Pizza", "Pepperoni, mozzarella, tomato sauce", 14.99, "Pizza", 4.7f, "15-20 min", true),
            MenuItem(3, "Classic Burger", "Beef patty, lettuce, tomato, onion, pickle", 9.99, "Burger", 4.3f, "10-15 min", true),
            MenuItem(4, "Chicken Burger", "Grilled chicken, lettuce, mayo, tomato", 10.99, "Burger", 4.4f, "12-18 min"),
            MenuItem(5, "Pasta Carbonara", "Creamy pasta with bacon, egg, parmesan", 11.99, "Pasta", 4.6f, "15-20 min", true),
            MenuItem(6, "Penne Arrabbiata", "Spicy tomato sauce with garlic and chili", 10.99, "Pasta", 4.2f, "12-15 min", true, true),
            MenuItem(7, "Caesar Salad", "Romaine lettuce, croutons, parmesan, caesar dressing", 8.99, "Salad", 4.1f, "5-8 min", false, true),
            MenuItem(8, "Greek Salad", "Tomatoes, cucumber, olives, feta cheese", 9.99, "Salad", 4.3f, "5-8 min", false, true),
            MenuItem(9, "BBQ Pizza", "BBQ sauce, chicken, red onion, cilantro", 15.99, "Pizza", 4.5f, "18-22 min"),
            MenuItem(10, "Veggie Burger", "Plant-based patty, avocado, sprouts", 11.99, "Burger", 4.0f, "10-15 min", false, true)
        )
    }

    val categories = remember {
        listOf(
            MenuCategory("All", Color(0xFFFF6B35)),
            MenuCategory("Pizza", Color(0xFFE91E63)),
            MenuCategory("Burger", Color(0xFF2196F3)),
            MenuCategory("Pasta", Color(0xFF4CAF50)),
            MenuCategory("Salad", Color(0xFF8BC34A))
        )
    }

    val filteredItems = menuItems.filter { item ->
        (selectedCategory == "All" || item.category == selectedCategory) &&
                (searchQuery.isEmpty() || item.name.contains(searchQuery, ignoreCase = true))
    }

    val totalCartItems = cartItems.sumOf { it.quantity }
    val totalCartPrice = cartItems.sumOf { it.menuItem.price * it.quantity }

    // Helper functions for cart management
    fun addToCart(menuItem: MenuItem) {
        val existingItem = cartItems.find { it.menuItem.id == menuItem.id }
        cartItems = if (existingItem != null) {
            cartItems.map {
                if (it.menuItem.id == menuItem.id) {
                    it.copy(quantity = it.quantity + 1)
                } else it
            }
        } else {
            cartItems + CartItem(menuItem, 1)
        }
    }

    fun removeFromCart(menuItem: MenuItem) {
        val existingItem = cartItems.find { it.menuItem.id == menuItem.id }
        if (existingItem != null) {
            cartItems = if (existingItem.quantity > 1) {
                cartItems.map {
                    if (it.menuItem.id == menuItem.id) {
                        it.copy(quantity = it.quantity - 1)
                    } else it
                }
            } else {
                cartItems.filter { it.menuItem.id != menuItem.id }
            }
        }
    }

    fun getItemQuantity(menuItem: MenuItem): Int {
        return cartItems.find { it.menuItem.id == menuItem.id }?.quantity ?: 0
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
                .padding(16.dp)
                .padding(bottom = if (totalCartItems > 0) 80.dp else 0.dp), // Add bottom padding when cart has items
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with back button and logout option
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFF6B35)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = onBackPressed, // Fixed: use onBackPressed parameter
                                    modifier = Modifier
                                        .background(
                                            Color.White.copy(alpha = 0.2f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .size(40.dp)
                                ) {
                                    Icon(
                                        Icons.Default.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Our Menu",
                                        color = Color.White,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Discover delicious food",
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontSize = 14.sp
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Logout button (if provided)
                                onLogout?.let {
                                    IconButton(
                                        onClick = it,
                                        modifier = Modifier
                                            .background(
                                                Color.White.copy(alpha = 0.2f),
                                                RoundedCornerShape(8.dp)
                                            )
                                            .size(40.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.ExitToApp,
                                            contentDescription = "Logout",
                                            tint = Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                }

                                // Cart icon with badge
                                if (totalCartItems > 0) {
                                    Box {
                                        IconButton(
                                            onClick = { /* Optional: Open cart preview */ },
                                            modifier = Modifier
                                                .background(
                                                    Color.White.copy(alpha = 0.2f),
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .size(40.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.ShoppingCart,
                                                contentDescription = "Cart",
                                                tint = Color.White
                                            )
                                        }
                                        Badge(
                                            modifier = Modifier
                                                .offset(x = 8.dp, y = (-8).dp)
                                                .align(Alignment.TopEnd),
                                            containerColor = Color.White,
                                            contentColor = Color(0xFFFF6B35)
                                        ) {
                                            Text(
                                                text = totalCartItems.toString(),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Search Bar
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search for food...") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFFFF6B35)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { searchQuery = "" }
                                ) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = "Clear search",
                                        tint = Color(0xFF718096)
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF6B35),
                            focusedLabelColor = Color(0xFFFF6B35)
                        ),
                        singleLine = true
                    )
                }
            }

            // Category Filters
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(
                            category = category,
                            isSelected = selectedCategory == category.name,
                            onClick = { selectedCategory = category.name }
                        )
                    }
                }
            }

            // Cart Summary (only show when cart has items)
            if (totalCartItems > 0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF0FDF4)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    tint = Color(0xFF16A34A),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "$totalCartItems items in cart",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF16A34A)
                                    )
                                    Text(
                                        text = "Total: $${String.format("%.2f", totalCartPrice)}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF166534)
                                    )
                                }
                            }
                            IconButton(
                                onClick = { cartItems = emptyList() }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Clear cart",
                                    tint = Color(0xFF16A34A)
                                )
                            }
                        }
                    }
                }
            }

            // Menu Stats
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Popular",
                        value = filteredItems.count { it.isPopular }.toString(),
                        icon = Icons.Default.Star,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "In Cart",
                        value = totalCartItems.toString(),
                        icon = Icons.Default.ShoppingCart,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Menu Items
            items(filteredItems) { item ->
                MenuItemCard(
                    menuItem = item,
                    quantity = getItemQuantity(item),
                    onAddToCart = { addToCart(item) },
                    onRemoveFromCart = { removeFromCart(item) }
                )
            }

            // Empty State
            if (filteredItems.isEmpty()) {
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
                                .padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color(0xFFA0AEC0)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No items found",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF718096),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Try adjusting your search or filters",
                                fontSize = 14.sp,
                                color = Color(0xFFA0AEC0),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Floating Order Button (only show when cart has items)
        if (totalCartItems > 0) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        // Pass cart data to navigation - you might want to use a ViewModel or pass data differently
                        onOrderClick()
                    },
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Proceed to Order",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$totalCartItems items • $${String.format("%.2f", totalCartPrice)}",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp
                        )
                    }
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "Proceed to order",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: MenuCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) category.color else Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(if (isSelected) 8.dp else 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = category.name,
                color = if (isSelected) Color.White else Color(0xFF2D3748),
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFFF6B35),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color(0xFF718096),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MenuItemCard(
    menuItem: MenuItem,
    quantity: Int,
    onAddToCart: () -> Unit,
    onRemoveFromCart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (quantity > 0) Color(0xFFFFF7ED) else Color.White
        ),
        elevation = CardDefaults.cardElevation(if (quantity > 0) 8.dp else 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = menuItem.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748)
                        )
                        if (menuItem.isPopular) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Popular",
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Text(
                        text = menuItem.description,
                        fontSize = 14.sp,
                        color = Color(0xFF718096),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Rating
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = menuItem.rating.toString(),
                            fontSize = 12.sp,
                            color = Color(0xFF718096),
                            modifier = Modifier.padding(start = 4.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Time

                        Text(
                            text = menuItem.preparationTime,
                            fontSize = 12.sp,
                            color = Color(0xFF718096),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tags
                    Row {
                        if (menuItem.isVegetarian) {
                            Text(
                                text = "Vegetarian",
                                fontSize = 10.sp,
                                color = Color(0xFF16A34A),
                                modifier = Modifier
                                    .background(
                                        Color(0xFF16A34A).copy(alpha = 0.1f),
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        if (menuItem.isSpicy) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Spicy",
                                fontSize = 10.sp,
                                color = Color(0xFFDC2626),
                                modifier = Modifier
                                    .background(
                                        Color(0xFFDC2626).copy(alpha = 0.1f),
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "$${String.format("%.2f", menuItem.price)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B35)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = menuItem.category,
                        fontSize = 12.sp,
                        color = Color(0xFFFF6B35),
                        modifier = Modifier
                            .background(
                                Color(0xFFFF6B35).copy(alpha = 0.1f),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add to cart controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (quantity > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onRemoveFromCart,
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    Color(0xFFFF6B35).copy(alpha = 0.1f),
                                    RoundedCornerShape(8.dp)
                                )
                        ) {

                        }

                        Text(
                            text = quantity.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B35),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        IconButton(
                            onClick = onAddToCart,
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    Color(0xFFFF6B35),
                                    RoundedCornerShape(8.dp)
                                )
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add to cart",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = onAddToCart,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6B35)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add to Cart")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    MaterialTheme {
        MenuScreen()
    }
}