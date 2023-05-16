# ComposeRouter
Android Jetpack Compose React-router-style Router inspired by react dom router

```kotlin
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
        route("Settings", "/settings", R.string.settings){
            Settings() //any screen
        },
        route("MyScreen1", "/screen1", R.string.screen1){
            Screen1("screen1") //any screen
        },
        route("MyScreen2", "/screen2", R.string.screen2){
            Screen2()//any screen
        },
        route("MyScreen3", "/screen3", R.string.screen2){
            Screen3() //any screen
        }
    ).Screen()
}

@Composable
fun Home() {
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Button(onClick = { Router.navByName("Settings") }) {
            Text(text = "Settings")
        }
        Button(onClick = { Router.navByPath("/screen1") }) {
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
```

## TODO
1. PathMather to support path and query parameters
2. permission check
3. multi-activity support
