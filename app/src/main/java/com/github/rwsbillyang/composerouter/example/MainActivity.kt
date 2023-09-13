package com.github.rwsbillyang.composerouter.example

import android.os.Bundle

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.rwsbillyang.composerouter.*
import com.github.rwsbillyang.composerouter.example.ui.theme.ComposeRouterTheme
import com.github.rwsbillyang.composerouter.ui.RoutableActivity
import com.github.rwsbillyang.composerouter.ui.SimpleNavLayout


class MainActivity : RoutableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Router.addRoutes(
            route("Home", "/", R.string.home){ Home() },
            route("MyScreen1", "/screen1", R.string.screen1){ Screen1(it) },
            route("MyScreen2", "/screen2", R.string.screen2){ Screen2() },
            route("MyScreen3", "/screen3", R.string.screen3){ Screen3() },
            route("Settings", "/settings", R.string.settings){ Settings() },
        )

        setContent {
            ComposeRouterTheme {
                SimpleNavLayout(R.string.app_name) //with navigation title bar on top
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
        Button(onClick = { Router.navByName("Settings") }) {
            Text(text = "Settings")
        }

        //pass props to screen
        Button(onClick = { Router.navByPath("/screen1", "any props") }) {
            Text(text = "enter screen1")
        }

        Button(onClick = { Router.navByName("MyScreen2") }) {
            Text(text = "enter screen2")
        }

        Button(onClick = { Router.navByName("MyScreen3") }) {
            Text(text = "enter screen3")
        }
    }
}


//need receive props
@Composable
fun Screen1(call: ScreenCall) {  Text(text = "Hello, ${call.props}") }

@Composable
fun Screen2() { Text(text = "Screen2") }

@Composable
fun Screen3() {   Text(text = "Screen3") }

@Composable
fun Settings() { Text(text = "Settings Screen") }
