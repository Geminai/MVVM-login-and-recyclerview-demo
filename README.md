# [MVVM with Hilt - Login and RecyclerView demo - Kotlin](https://github.com/Geminai/MVVM-login-and-recyclerview-demo)

Model-View-ViewModel (ie MVVM) is a template of a client application architecture, proposed by John Gossman as an alternative to MVC and MVP patterns when using Data Binding technology. Its concept is to separate data presentation logic from business logic by moving it into particular class for a clear distinction.


**MVVM Best Pratice:**
- Avoid references to Views in ViewModels.
- Instead of pushing data to the UI, let the UI observe changes to it.
- Distribute responsibilities, add a domain layer if needed.
- Add a data repository as the single-point entry to your data.
- Expose information about the state of your data using a wrapper or another LiveData.
- Consider edge cases, leaks and how long-running operations can affect the instances in your architecture.
- Don’t put logic in the ViewModel that is critical to saving clean state or related to data. Any call you make from a ViewModel can be the last one.


### Dependencies

- Hilt (https://developer.android.com/training/dependency-injection/hilt-android)
- Navigation Component (https://developer.android.com/guide/navigation/navigation-getting-started)
- Coroutines (https://developer.android.com/kotlin/coroutines)
- DataStore (https://developer.android.com/topic/libraries/architecture/datastore)
- ViewModel (https://developer.android.com/topic/libraries/architecture/viewmodel)
- Data Binding (https://developer.android.com/topic/libraries/data-binding)
- Retrofit 2 (https://square.github.io/retrofit/)
- OkHttp 3 (https://square.github.io/okhttp/)
- Glide (https://github.com/bumptech/glide)
- AndroidX (https://mvnrepository.com/artifact/androidx)
- Arch Lifecycle (https://developer.android.com/jetpack/androidx/releases/lifecycle)

