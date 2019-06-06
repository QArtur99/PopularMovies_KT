# Popular Movies
Popular Movies app inspired by the Udacity Android Developer Nanodegree. Rewritten in Kotlin and implements the new Jetpack libraries.
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
* [Retrofit 2](https://github.com/square/retrofit) - Handle REST api communication
* [Moshi](https://github.com/square/moshi) - Serialize Kotlin objects and deserialize JSON objects
* [Glide](https://github.com/bumptech/glide) - Load and cache images by URL.
* [Test](https://developer.android.com/training/testing/) - An Android testing framework for unit and runtime UI tests.


### Screenshots
![image](https://user-images.githubusercontent.com/25232443/58781649-ca9b8580-85dc-11e9-8469-7127191becb0.png)
![image](https://user-images.githubusercontent.com/25232443/58781686-e0a94600-85dc-11e9-8bd4-fb9f5ebd12c9.png)


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


### Google Play
<a href='https://play.google.com/store/apps/details?id=com.qartf.popularmovies&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'>
<img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>

