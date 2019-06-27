# Popular Movies
<a href='https://play.google.com/store/apps/details?id=com.qartf.popularmovies'>
<img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width="auto" height="100"/></a>

Popular Movies app inspired by the Udacity Android Developer Nanodegree. Written in Kotlin and implements the Jetpack libraries.
The application fetches movie data using https://www.themoviedb.org (TMDB).


### Used Tech
* [Kotlin](https://kotlinlang.org/)
* [MVVM](https://developer.android.com/jetpack/docs/guide)
* [Data Binding](https://developer.android.com/topic/libraries/data-binding/) - Declaratively bind observable data to UI elements.
* [Lifecycles](https://developer.android.com/topic/libraries/architecture/lifecycle) - Create a UI that automatically responds to lifecycle events.
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Build data objects that notify views when the underlying database changes.
* [Navigation](https://developer.android.com/guide/navigation/) - Handle everything needed for in-app navigation.
* [Paging](https://developer.android.com/topic/libraries/architecture/paging/) - Load and display small chunks of data at a time.
* [Room](https://developer.android.com/topic/libraries/architecture/room) - Access your app's SQLite database with in-app objects and compile-time checks.
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Store UI-related data that isn't destroyed on app rotations. Easily schedule asynchronous tasks.
* [Retrofit 2](https://github.com/square/retrofit) - Handle REST api communication.
* [Moshi](https://github.com/square/moshi) - Serialize Kotlin objects and deserialize JSON objects.
* [Glide](https://github.com/bumptech/glide) - Load and cache images by URL.
* [Test](https://developer.android.com/training/testing/) - An Android testing framework for unit and runtime UI tests.
* [ktlint](https://ktlint.github.io/) - Enforce Kotlin coding styles.

### Screenshots
![image](https://user-images.githubusercontent.com/25232443/59937609-342be880-9453-11e9-957c-d324107db543.png)
![image](https://user-images.githubusercontent.com/25232443/59937627-3db55080-9453-11e9-9a2b-b62c49b23451.png)

### How to run the project in development mode
* Clone or download repository as a zip file.
* Open project in Android Studio.
* Set TheMovieDb API key in build.gradle(line 41).
* Run 'app' `SHIFT+F10`.


### Features
* Discover the most popular and the most rated movies
* Mark favorite movies by tapping a heart icon and store them in local database
* Watch and play trailers on youtube
* Read reviews


### Report issues
Something not working quite as expected? Do you need a feature that has not been implemented yet? Check the [issue tracker](https://github.com/QArtur99/PopularMovies_KT/issues) and add a new one if your problem is not already listed. Please try to provide a detailed description of your problem, including the steps to reproduce it.


### Contribute
Awesome! If you would like to contribute with a new feature or submit a bugfix, fork this repo and send a pull request. Please, make sure all the [unit tests](https://github.com/QArtur99/PopularMovies_KT/tree/master/app/src/test/java/com/qartf/popularmovies) & [integration tests](https://github.com/QArtur99/PopularMovies_KT/tree/master/app/src/androidTest/java/com/qartf/popularmovies) are passing before submitting and add new ones in case you introduced new features.


### License
    Copyright 2019 Artur Gniewowski
 
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
 
        http://www.apache.org/licenses/LICENSE-2.0
 
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

