# Assignment for Android Support Engineer 

## Application DEMO Video : <a href="https://drive.google.com/file/d/1_s8P4X17CKtBIO2Z077c4med3uYtyTvk/view?usp=sharing">**Click here to see the Application**</a>
## Application link : <a href="https://drive.google.com/file/d/115CFYv5WVlb_N8LAtqmmt7h1RrHfwIR2/view?usp=sharing">**Click here to download apk**</a>


## Overview :
This Android application is a Article App built entirely in Kotlin, providing users with access to a wide range of news articles. It fetches news article details from a [Provided API](https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json) and displays them on the home screen. The displayed information includes headlines, descriptions, authors, and corresponding URLs, among other variables. When the user clicks on a listed headline, a browser window should load and display the corresponding article.

## 📷 Demo :

 <table align="center">
  <tr>
    <td><img src="https://github.com/UKnow-IKnow/Article-App-Assignment/assets/92221289/a2346141-634f-4499-8eed-27bdc4ccb825" alt="start" style="width:200px;height:430px;"></td>
  </tr>
</table><br>



 ## Screenshots : 

 <table align="center">
  <tr>
    <td><img src="https://github.com/UKnow-IKnow/Article-App-Assignment/assets/92221289/95727bab-c9a0-4bc6-9c83-073a5c0dfa4c" alt="News home" style="width:200px;height:400px;"></td>
    <td><img src="https://github.com/UKnow-IKnow/Article-App-Assignment/assets/92221289/e1c81533-d579-4641-b8f7-21910bf6fd61" alt="Sort News" style="width:200px;height:400px;"></td>
    <td><img src="https://github.com/UKnow-IKnow/Article-App-Assignment/assets/92221289/e25cd1d7-a1df-41c4-9f0d-9286b5122af1" alt="Notification" style="width:200px;height:400px;"></td>
  </tr>
</table><br>


## Features :
* Kotlin Development: Built entirely using Kotlin .
* Adhered to the MVVM (Model-View-ViewModel) design principle to architect the application's codebase effectively. Leveraged a combination of repository, view model, and LiveData components to manage API calls and facilitate data flow. This architectural approach fosters separation of concerns, ensuring code maintainability and scalability while promoting a clean and organized codebase.
* API Integration: Fetches news details from a specified [Web API](https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json) using System APIs.
* Material UI: Designed layouts with Material UI, employing RecyclerViews and card-based design for articles.
* Custom Design Elements: Incorporates custom design elements, fonts, and icons for a unique user experience.
* Enhanced User Experience: Prioritizes user experience with intuitive features and smooth navigation.
* Error Handling : Handles input exceptions, Unicode, and null values with clear code documentation.
* Sorting Functionality: Allows sorting and listing of articles based on old-to-new and new-to-old criteria.
* Firebase Cloud Messaging Integration: Implemented Firebase Cloud Messaging (FCM) to send remote notifications to mobile devices.


## Tools and Tech stack used 🛠 : 

- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development. Our app is totally written in kotlin.
- [DataStore](https://developer.android.com/jetpack/androidx/releases/datastore) - Store data asynchronously, consistently, and transactionally, overcoming some of the drawbacks of SharedPreferences
- [Material UI](https://m2.material.io/develop/android) - Material 3 is the latest version of Google’s open-source design system. Design and build beautiful, usable products with Material 3.
- [Different Layouts](https://developer.android.com/guide/topics/ui/declaring-layout) -  In this app we have used difrenet layouts to make the app UI responsive. The used layouts are LinearLayout, ConstraintLayout and FrameLayout .
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes. 
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - LiveData is an observable data holder class. Unlike a regular observable, LiveData is lifecycle-aware, meaning it respects the lifecycle of other app components, such as activities, fragments, or services.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more. Speacially used at the time of networking calls and using database .
- [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview?authuser=2) - RecyclerView makes it easy to efficiently display large sets of data. To show large lists.
- [Glide](https://github.com/bumptech/glide.git) - Glide is a fast and efficient open-source media management and image loading framework for Android developed by bumptech. In this tutorial, we will use this awesome library to show an image from the internet on our apps screen.
- [MVVM](https://developer.android.com/jetpack/guide) - MVVM is one of the architectural patterns which enhances separation of concerns, it allows separating the user interface logic from the business (or the back-end) logic. Its target (with other MVC patterns goal) is to achieve the following principle “Keeping UI code simple and free of app logic in order to make it easier to manage”.

<table align="center">
  <tr>
    <td><img src="https://github.com/UKnow-IKnow/Article-App-Assignment/assets/92221289/6184439f-e6d3-47de-b4d6-10fd8788e323" alt="News home"></td>
  </tr>
</table><br> 

