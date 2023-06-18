package app.common.client.ui.graphix

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.common.appBar
import app.common.dao
import kotlinx.coroutines.runBlocking
import persistence.database.product.Product

@Composable
fun graphix() {
    var products by remember {
        mutableStateOf(mutableListOf<Product>())
    }
    runBlocking {
        products = dao.getProducts().toMutableList()
    }

    val colors = listOf(Color.Blue, Color.Red, Color.Green, Color.Yellow, Color.Magenta, Color.Cyan)
    val byOwner = products.groupBy { it.owner!!.name }

    val maxX = products.maxOf { it.coordinates?.x?.toFloat()!! }
    val minX = products.minOf { it.coordinates?.x?.toFloat()!! }
    val diffX = (maxX - minX)

    val maxY = products.maxOf { it.coordinates?.y?.toFloat()!! }
    val minY = products.minOf { it.coordinates?.y?.toFloat()!! }
    val diffY = (maxY - minY)

    val maxPrice = products.maxOf { it.price?.toFloat()!! }
    val minPrice = products.minOf { it.price?.toFloat()!! }
    val diffPrice = (maxPrice - minPrice)


    Column {
        appBar()
        Canvas(modifier = Modifier.fillMaxSize().padding(64.dp)) {
            byOwner.keys.forEach {
                byOwner[it]?.forEachIndexed { index, product ->
                    println("draw circle ${product.coordinates}")
                    drawCircle(
                        color = colors[index % colors.size],
                        radius = (product.price?.toFloat()!!) * 30 / diffPrice,
                        center = Offset(
                            x = product.coordinates?.x?.toFloat().toRelative(minX, diffX),
                            y = product.coordinates?.y?.toFloat().toRelative(minY, diffY),
                        ),
                    )
                }
            }
        }
    }

}

private fun Float?.toRelative(minX: Float, diffX: Float): Float {
    return (this!! - minX) / diffX * 1000
}
