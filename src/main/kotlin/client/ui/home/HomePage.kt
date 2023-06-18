package app.common.client.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.common.Page
import app.common.appBar
import app.common.dao
import app.common.globalNetworkScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import persistence.database.product.*
import ru.alexgladkov.odyssey.compose.extensions.push
import ru.alexgladkov.odyssey.compose.local.LocalRootController

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun home() {
    val products = remember { mutableStateOf(emptyList<Product>()) }
    LaunchedEffect("getProducts") {
        dao.getProducts().let {
            products.value = it.sortedBy { it.id }
        }
    }
    var editing by remember { mutableStateOf(false) }
    var editingId by remember { mutableStateOf(0L) }
    val nav = LocalRootController.current

    @Composable
    fun editDialog(id: Long) {
        val name = remember { mutableStateOf("") }
        val x = remember { mutableStateOf("") }
        val y = remember { mutableStateOf("") }
        val price = remember { mutableStateOf("") }
        val partNumber = remember { mutableStateOf("") }
        //val unitOfMeasure = remember { mutableStateOf("") }
        val ownerName = remember { mutableStateOf("") }
        val ownerHeight = remember { mutableStateOf("") }
        val ownerWeight = remember { mutableStateOf("") }
        val ownerLocationX = remember { mutableStateOf("") }
        val ownerLocationY = remember { mutableStateOf("") }
        val ownerLocationZ = remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { !editing },
            title = { Text("Editing") },
            text = {
                val scroll = rememberScrollState()
                Box(Modifier.verticalScroll(scroll).fillMaxSize()) {
                    Row {
                        Column(Modifier.weight(.5f)) {
                            TextField(
                                value = name.value,
                                onValueChange = { name.value = it },
                                placeholder = { Text("Name") },
                                modifier = Modifier.fillMaxWidth().padding(4.dp)
                            )
                            TextField(
                                value = x.value,
                                onValueChange = { x.value = it },
                                placeholder = { Text("X") },
                                modifier = Modifier.fillMaxWidth().padding(4.dp)
                            )
                            TextField(
                                value = y.value,
                                onValueChange = { y.value = it },
                                placeholder = { Text("Y") },
                                modifier = Modifier.fillMaxWidth().padding(4.dp)
                            )
                            TextField(
                                value = price.value,
                                onValueChange = { price.value = it },
                                placeholder = { Text("Price") },
                                modifier = Modifier.fillMaxWidth().padding(4.dp)
                            )
                            TextField(
                                value = partNumber.value,
                                onValueChange = { partNumber.value = it },
                                placeholder = { Text("Part number") },
                                modifier = Modifier.fillMaxWidth().padding(4.dp)
                            )
                        }
                        Column(Modifier.weight(.5f)) {
                            TextField(
                                value = ownerName.value,
                                onValueChange = { ownerName.value = it },
                                placeholder = { Text("Owner name") },
                                modifier = Modifier.fillMaxWidth().padding(4.dp)
                            )
                            TextField(
                                value = ownerHeight.value,
                                onValueChange = { ownerHeight.value = it },
                                placeholder = { Text("Owner height") },
                                modifier = Modifier.fillMaxWidth().padding(4.dp)
                            )
                            TextField(
                                value = ownerWeight.value,
                                onValueChange = { ownerWeight.value = it },
                                placeholder = { Text("Owner weight") },
                                modifier = Modifier.fillMaxWidth().padding(4.dp)
                            )
                            TextField(
                                value = ownerLocationX.value,
                                onValueChange = { ownerLocationX.value = it },
                                placeholder = { Text("Owner location X") },
                                modifier = Modifier.fillMaxWidth().padding(4.dp)
                            )
                            TextField(
                                value = ownerLocationY.value,
                                onValueChange = { ownerLocationY.value = it },
                                placeholder = { Text("Owner location Y") },
                                modifier = Modifier.fillMaxWidth().padding(4.dp)
                            )
                            TextField(
                                value = ownerLocationZ.value,
                                onValueChange = { ownerLocationZ.value = it },
                                placeholder = { Text("Owner location Z") },
                                modifier = Modifier.fillMaxWidth().padding(4.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    globalNetworkScope.launch {
                        val product = Product(
                            id = id,
                            name0 = name.value,
                            coordinates = Coordinates(x.value.toLong(), y.value.toInt()),
                            price = price.value.toLong(),
                            partNumber = partNumber.value,
                            unitOfMeasure = UnitOfMeasure.CENTIMETERS,
                            owner = Person(
                                name = ownerName.value,
                                height = ownerHeight.value.toLong(),
                                weight = ownerWeight.value.toFloat(),
                                location = Location(
                                    x = ownerLocationX.value.toInt(),
                                    y = ownerLocationY.value.toLong(),
                                    z = ownerLocationZ.value.toInt()
                                )
                            )
                        )
                        async { dao.editProduct(product, id) }.await()
                        editing = false
                    }
                }) {
                    Text("Ok")
                }
            },
            dismissButton = {
                Row {
                    Button(onClick = {
                        globalNetworkScope.launch {
                            async { dao.deleteProduct(id) }.await()
                            editing = false
                        }
                    }, colors = ButtonDefaults.buttonColors(Color.Red)) {
                        Text("Delete")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { editing = false }) {
                        Text("Cancel")
                    }
                }
            },
            modifier = Modifier.padding(8.dp).requiredWidth(600.dp)
        )
    }

    fun onEdit(id: Long) {
        editingId = id
        editing = true
    }

    if (editing) {
        editDialog(editingId)
    }

    Column(Modifier.fillMaxSize()) {
        appBar()
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            LazyColumn {
                item {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.padding(20.dp).wrapContentSize()
                    ) {
                        Cell("id", 1f) {
                            products.value = products.value.sortedBy { it.id }
                        }
                        Cell("name", 1f) {
                            products.value = products.value.sortedBy { it.name0 }
                        }
                        Cell("x", 1f) {
                            products.value = products.value.sortedBy { it.coordinates?.x }
                        }
                        Cell("y", 1f) {
                            products.value = products.value.sortedBy { it.coordinates?.y }
                        }
                        Cell("price", 1f) {
                            products.value = products.value.sortedBy { it.price }
                        }
                        Cell("partNumber", 1f) {
                            products.value = products.value.sortedBy { it.partNumber }
                        }
                        Cell("unitOfMeasure", 1f) {
                            products.value = products.value.sortedBy { it.unitOfMeasure }
                        }
                        Cell("owner.name", 1f) {
                            products.value = products.value.sortedBy { it.owner?.name }
                        }
                        Cell("owner.height", 1f) {
                            products.value = products.value.sortedBy { it.owner?.height }
                        }
                        Cell("owner.weight", 1f) {
                            products.value = products.value.sortedBy { it.owner?.weight }
                        }
                        Cell("owner.location.x", 1f) {
                            products.value = products.value.sortedBy { it.owner?.location?.x }
                        }
                        Cell("owner.location.y", 1f) {
                            products.value = products.value.sortedBy { it.owner?.location?.y }
                        }
                        Cell("owner.location.z", 1f) {
                            products.value = products.value.sortedBy { it.owner?.location?.z }
                        }
                    }
                }
                items(products.value) { product ->
                    Card(Modifier.padding(16.dp).wrapContentWidth(), elevation = 3.dp) {
                        val defaultWeight = 1f
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.padding(4.dp).wrapContentWidth()
                        ) {
                            IconButton(onClick = { product.id?.let { onEdit(it) } },
                                content = {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "Edit",
                                        tint = Color.Green,
                                        modifier = Modifier.size(24.dp).padding(4.dp)
                                    )
                                })
                            Cell(product.id.toString(), defaultWeight)
                            Cell(product.name0.toString(), defaultWeight)
                            Cell(product.coordinates?.x.toString(), defaultWeight)
                            Cell(product.coordinates?.y.toString(), defaultWeight)
                            Cell(product.price.toString(), defaultWeight)
                            Cell(product.partNumber.toString(), (defaultWeight))
                            Cell(product.unitOfMeasure.toString(), (defaultWeight))
                            Cell(product.owner?.name.toString(), defaultWeight)
                            Cell(product.owner?.height.toString(), defaultWeight)
                            Cell(product.owner?.weight.toString(), defaultWeight)
                            Cell(product.owner?.location?.x.toString(), defaultWeight)
                            Cell(product.owner?.location?.y.toString(), defaultWeight)
                            Cell(product.owner?.location?.z.toString(), defaultWeight)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            nav.push(Page.GRAPHICS.destination)
        }) {
            Text("To graphix")
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun RowScope.Cell(
    text: String,
    weight: Float,
    onClick: () -> Unit = {}
) {
    Text(
        text = text,
        Modifier
            .padding(4.dp)
            .weight(weight)
            .onClick {
                onClick()
            }
    )
}
