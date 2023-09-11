package com.github.rwsbillyang.composerouter.example

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
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


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRouterTheme {
                Content()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent):Boolean {
       return if(Router.onKeyDown(keyCode, event)) true else super.onKeyDown(keyCode, event)
    }
}


@Composable
private fun Content(){
    Router.withRoutes(
        route("Home", "/", R.string.home){
            Home() //any screen
        },
        route("MyScreen1", "/screen1", R.string.screen1){
            Screen1(it) //any screen need props pass
        },
        route("MyScreen2", "/screen2", R.string.screen2){
            Screen2()//any screen
        },
        route("MyScreen3", "/screen3", R.string.screen2){
            Screen3() //any screen
        },
        route("Settings", "/settings", R.string.settings){
            Settings() //any screen
        },
    ).Screen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content2(){
    SimpleNavLayout(R.string.app_name, Router.withRoutes(
        route("Home", "/", R.string.home){ Home() },

        route("MyScreen1", "/screen1", R.string.screen1){
            Screen1(it) //any screen need props pass
        },
        route("MyScreen2", "/screen2", R.string.screen2){
            Screen2()//any screen
        },
        route("MyScreen3", "/screen3", R.string.screen2){
            Screen3() //any screen
        },
        route("Settings", "/settings", R.string.settings){
        Settings() //any screen
    },
    ))
}

@Composable
fun Home() {
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Button(onClick = { Router.navByName("Settings") }) {
            Text(text = "Settings")
        }

        //pass props to screen
        Button(onClick = { Router.navByPath("/screen1", "some props") }) {
            Text(text = "screen1")
        }
        Button(onClick = { Router.navByName("MyScreen2") }) {
            Text(text = "screen2")
        }
        Button(onClick = { Router.navByName("MyScreen3") }) {
            Text(text = "screen3")
        }
    }
}
@Composable
fun Settings() {
    Text(text = "Settings Screen")
}

//need receive props
@Composable
fun Screen1(call: ScreenCall) {
    Text(text = "Hello, ${call.props}")
}

@Composable
fun Screen2() {
    Text(text = "Screen2")
}
@Composable
fun Screen3() {
    Text(text = "Screen3")
}


