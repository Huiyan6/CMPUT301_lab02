package com.example.listycity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {  // tells Compose what UI to display
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it) }, //onAddCity is a function, we assign this code to the function.
                        onDeleteCity = { cityRepository.deleteCity(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CityListScreen(cities: List<String>, onAddCity: (String) -> Unit, onDeleteCity: (String) -> Boolean, modifier: Modifier = Modifier){
    var newCityName by remember {mutableStateOf("")}
    Column(modifier = modifier.fillMaxSize()){
        val context = LocalContext.current
        Row(modifier = Modifier.padding(16.dp)){
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = {Text("City Name")},
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))
            Column() {
                Button(
                    onClick = {
                        if (newCityName.isNotBlank()){
                            onAddCity(newCityName)
                            newCityName = ""
                        }
                    }
                ){
                    Text("Add City")
                }
                Button(
                    onClick = {
                        if (newCityName.isNotBlank()){
                            if(!onDeleteCity(newCityName)) {
                                Toast.makeText(context,
                                    "city does not exist, deletions are CASE SENSITIVE",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            newCityName = ""
                        }
                    }
                ){
                    Text("Delete City")
                }
            }

        }
    }
    LazyColumn(modifier = Modifier.fillMaxHeight().padding(vertical = 100.dp)) {//implemented vertical padding because text was blocked by City Name textbox
        //I'm now sure if this padding would fix UI overlapping for ALL devices, however it works for the "Medium Phone API 37.1" Emulator
        items(cities){city ->
            CityRow(city = city)
        }
    }
}

@Composable
fun CityRow(city: String){
    Text(
        text = city,
        fontSize = 28.sp,
        modifier = Modifier
            .fillMaxWidth(0.5f) // I changed this from default to 0.5f because the UI
                                        // was overlapping with my delete city button
            .padding(horizontal = 18.dp, vertical = 14.dp)
    )
}
/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val cityRepository = CityRepository()
    ListyCityTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            CityListScreen(
                cities = cityRepository.cities,
                onAddCity = {cityRepository.addCity(it)},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}*/

class CityRepository{
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Moscow",
        "Sydney", "Berlin", "Vienna",
        "Tokyo", "Beijing", "Osaka",
        "New Delhi"
    )

    //read only list for UI to display
    val cities: List<String>
        get() = _cities

    fun addCity(city: String){
        _cities.add(city)
    }

    fun deleteCity(city: String): Boolean{
        return _cities.remove(city)
    }
}

