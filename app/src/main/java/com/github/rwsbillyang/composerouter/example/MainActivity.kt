package com.github.rwsbillyang.composerouter.example

import android.os.Bundle

import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.rwsbillyang.composerouter.*
import com.github.rwsbillyang.composerouter.example.ui.theme.ComposeRouterTheme
import com.github.rwsbillyang.composerouter.nav.NavScaffold3
import com.github.rwsbillyang.composerouter.nav.RoutableActivity


class MainActivity : RoutableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Router.addRoutes(
            route("Home", "/", R.string.home){ Home() },
            //route("MyScreen1", "/screen1", R.string.screen1){ Screen1(it) },
            route("MyScreen1", "/screen1", R.string.screen1,screen = ScaffoldScreen3(
                { Screen1(it) },
                {
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = { Router.navByName("Settings") }) {
                        Icon(Icons.Rounded.Settings, contentDescription = "Settings")
                    }
                }
            )),

            //route("MyScreen2", "/screen2", R.string.screen2){ Screen2(it) },
            route("MyScreen2", "/screen2", R.string.screen2,
                screen = ScaffoldScreen3(
                    content = { Screen2(it) },
                    drawerContent = {
                        Column{
                            Text("Drawer title", modifier = Modifier.padding(16.dp))
                            HorizontalDivider()
                            // Drawer items
                            Text("item1", modifier = Modifier.padding(14.dp))
                            Text("item2", modifier = Modifier.padding(14.dp))
                        }
                    }
                )),
            route("MyScreen3", "/screen3", R.string.screen3){ Screen3(it) },
            route("Settings", "/settings", R.string.settings){ Settings(it) },
        )

        setContent {
            ComposeRouterTheme {
                NavScaffold3(R.string.app_name) //with navigation title bar on top
                //Router.Screen() //no navigation title bar
            }
        }
    }
// RoutableActivity already provided it
//    override fun onKeyDown(keyCode: Int, event: KeyEvent):Boolean {
//       return if(Router.onKeyDown(keyCode, event)) true else super.onKeyDown(keyCode, event)
//    }
}

@Composable
fun Home() {
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {

        //pass props to screen
        Button(onClick = { Router.navByPath("/screen1", "any type props") }) {
            Text(text = "enter screen1")
        }

        Button(onClick = { Router.navByName("MyScreen2") }) {
            Text(text = "enter screen2")
        }

        Button(onClick = { Router.navByName("MyScreen3") }) {
            Text(text = "enter screen3")
        }

        Button(onClick = { Router.navByName("Settings") }) {
            Text(text = "Settings")
        }
    }
}


//need receive props
@Composable
fun Screen1(call: ScreenCall) {
    Column(Modifier.fillMaxSize().padding(call.scaffoldPadding), Arrangement.Center, Alignment.CenterHorizontally){
        Text(text = "Hello, ${call.props}, with settings in top-right of nav bar")
    }
}

@Composable
fun Screen2(call: ScreenCall) {
    Column(Modifier.fillMaxSize().padding(call.scaffoldPadding), Arrangement.Center, Alignment.CenterHorizontally){
        Text(text = "Screen2 with drawer", Modifier.padding(call.scaffoldPadding))
    }
}

@Composable
fun Screen3(call: ScreenCall) {
    Column(Modifier.fillMaxSize().padding(call.scaffoldPadding).background(Color.Blue), Arrangement.Center, Alignment.CenterHorizontally){
        Text(text = "Screen3")
    }
}

@Composable
fun Settings(call: ScreenCall) {
    Column(Modifier.fillMaxSize().padding(call.scaffoldPadding).background(Color.Red), Arrangement.Center, Alignment.CenterHorizontally){
        Text(text = "Settings Screen")
    }
}
