# ComposeRouter
Android Jetpack Compose React-router-style Router inspired by react dom router

## Features:
1. Web style and extremely lightweight
2. optional: support navbar using scaffold, and configurable
3. scaffold supports Material2 and Material3 theme 

## How to
Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```


Step 2. Add the dependency
```
	dependencies {
	        implementation 'com.github.rwsbillyang:ComposeRouter:1.3.0'
	}
```


## Quickstart

1. A simple example:
```kotlin
class MainActivity : RoutableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRouterTheme {
                NavScaffold3(R.string.app_name) //with navigation title bar on top
                //Router.Screen() //no navigation title bar
            }
        }
    }

    override fun getRoutes() = listOf(
        route("Home",  R.string.home,"/"){ Home() },
        route("MyScreen1", R.string.screen1){ Screen1(it) },
        route("MyScreen2", R.string.screen2){ Screen2(it) },
        route("MyScreen3",  R.string.screen3){ Screen3(it) },
        route("Settings",  R.string.settings){ Settings(it) },
    )
}

@Composable
fun Home() {
    val router = useRouter()
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        //pass props to screen, and modify nav bar title
        Button(onClick = { router.navByPath("/screen1", "any type props", "newTitle of Screen1") }) {
            Text(text = "enter screen1")
        }

        Button(onClick = { router.navByName("MyScreen2") }) {
            Text(text = "enter screen2")
        }

        Button(onClick = { router.navByName("MyScreen3") }) {
            Text(text = "enter screen3")
        }

        Button(onClick = { router.navByName("Settings") }) {
            Text(text = "Settings")
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

```

Note: if use NavScaffold2/NavScaffold3 Material3 in your app as above, need add dependencies as follows:
For material3 theme:
```
    implementation libs.androidx.compose.material3
    implementation libs.androidx.compose.material3.window.sizeclass
```

For Material2 theme:
```
    implementation libs.androidx.compose.material
```

2. Just a little complicated example:

```kotlin
class MainActivity : RoutableActivity() { // or use LocalRoutableActivity: routes and route stack history are only valid/visible in Activity instead of globally
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRouterTheme {
                NavScaffold3(R.string.app_name) //with navigation title bar on top
                //router.Screen() //no navigation title bar
            }
        }
    }

    override fun getRoutes() = listOf(
        route("Home",  R.string.home,"/"){ Home() },
        //route("MyScreen1", R.string.screen1){ Screen1(it) },
        route("MyScreen1", R.string.screen1,"/screen1", screen = ScaffoldScreen3(
            { Screen1(it) },
            {
                val router = useRouter()
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = { router.navByName("Settings") }) {
                    Icon(Icons.Rounded.Settings, contentDescription = "Settings")
                }
            }
        )),

        //route("MyScreen2", R.string.screen2){ Screen2(it) },
        route("MyScreen2",  R.string.screen2,
            screen = ScaffoldScreen3(
                content = { Screen2(it) },
                drawerContent = {
                    val selectedState = rememberSaveable { mutableIntStateOf(0) }
                    ItemsNav(items = (1..50).map{"item$it"}, onItemClick = {index, it ->
                        selectedState.value = index
                        FlowEventBus.tryPost("onItemClick", Pair(index, it))
                    },selectedState.value, title = "Drawer title") {
                        Text(it, modifier = Modifier.padding(14.dp))
                    }
                }
            )),
        route("MyScreen3",  R.string.screen3){ Screen3(it) },
        route("Settings",  R.string.settings){ Settings(it) },
    )
}

//...
```

## Global Router and Activity Router

- Global Router
routes and route stack history are global, shared among activities.
```kotlin
class MainActivity : RoutableActivity() {
    //...
}

//use Router anywhere
Router.navByName("yourRouteName") //Not recommend, only means global router

@Composable
fun Screen(){
    val router = useRouter() //local activity router or global router, depend on context is RoutableActivity or LocalRoutableActivity

    router.navByName("yourRouteName") // recommend, support global and local router automatically
}
```

- Local Router
In some seniors, Activity should have its router(routes and stack history),
only need to extend LocalRoutableActivity as following:
```kotlin
class MainActivity : LocalRoutableActivity() { //MainActivity has its router
    //...
}

@Composable
fun Screen(){
    val router = useRouter() //local activity router or global router, depend on context is RoutableActivity or LocalRoutableActivity

    router.navByName("yourRouteName") // recommend, support global and local router automatically
}
```

## TODO
1. PathMather to support path and query parameters
2. permission check
3. multi-activity support
