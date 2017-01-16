# Quizlet Math Plus App
The Quizlet Math Plus app allows user to add mathematical formulas, links and custom branding to their [Quizlet](http://www.quizlet.com) flash cards. Quizlet Math Plus is perfect for students and teachers looking for an added level of functionality from their [Quizlet](http://www.quizlet.com) flash cards.

![Quizlet App](http://www.coronite.net/assets/img/github/QuizletApp.jpg)

## Features
- Uses the MathJax JavaScript library to display mathematical formulas in Quizlet.com flash cards. 
- Allows teachers and students to add links to relevant online content for each card.
- Allows the creator of the card set to add “branding” links for businesses and schools.
- Toggles the flash card term and description in the same view, so both are visible at the same time.
- Allows the user to display either the term or definition with the other hidden.
- Displays two lists of flash card sets--those created by the user and those currently being studied by the user.
- Properly displays “standard format” flash card data as well.

## Implementation

### Term
```json
{
 "meta": {
 "id": "Problem 2",
 "link-text": "1.2 Set Elements And Subsets",
 "link-href": "http://www.coronite.net/training/discrete_mathematics/discrete_mathematics_lesson1.php#section-2",
 "image-url": "",
 "branding-text": "Set Created By: Coronite Creative",
 "branding-url": "http://www.coronite.net"
 },
 "header": "List the elements of each set where \\( \\mathbf{N}= \\{ 1, 2, 3, \\dots \\} .\\)",
 "list-style-type": "lower-alpha",
 "list-items": [
 "\\( A=\\{ x \\in \\mathbf{N} \\; | \\; 3 \\lt x \\lt 9 \\} \\)",
 "\\( B=\\{ x \\in \\mathbf{N} \\; | \\; x \\; is \\; even, x \\lt 11 \\} \\)",
 "\\( C=\\{ x \\in \\mathbf{N} \\; | \\; 4 + x = 3 \\} \\)"
 ]
}
```
### Definition
```json
{
 "header": "",
 "list-style-type": "lower-alpha",
 "list-items": [
 "\\( A=\\{ 3, 4, 5, 6, 7, 8 \\} \\)",
 "\\( B=\\{ 2, 4, 6, 8, 10 \\} \\)",
 "\\( C= \\emptyset \\)"
 ]
}
```
The various flash card elements created from the above json are shown below:

![Quizlet App](http://www.coronite.net/assets/img/github/Quizlet2.jpg)



## Android Features Implemented:

- [CustomViews](https://developer.android.com/training/custom-views/index.html)
- [SQLite Databases](https://developer.android.com/training/basics/data-storage/databases.html)
- [Content Providers](https://developer.android.com/reference/android/content/ContentProvider.html)
- [Parcelables](https://developer.android.com/reference/android/os/Parcelable.html)
- [Explicit and Implicit Intents](https://developer.android.com/reference/android/content/Intent.html)


## Specifications
- `compileSdkVersion 25`
- `buildToolsVersion "24.0.1""`
- `minSdkVersion 21`
- `targetSdkVersion 25`

## Dependencies
- `compile fileTree(include: ['*.jar'], dir: 'libs')`
- `testCompile 'junit:junit:4.12'
- `compile project(':MathView')`
- `compile 'com.android.support:appcompat-v7:25.0.1'`
- `compile 'com.android.support:support-v4:25.0.1'`
- `compile "com.android.support:recyclerview-v7:${supportLibVersion}"`
- `compile 'com.squareup.retrofit2:retrofit:2.1.0'`
- `compile 'com.squareup.retrofit2:converter-gson:2.1.0'`
- `compile 'com.google.android.gms:play-services-ads:9.8.0'`
- `compile 'com.google.android.gms:play-services-analytics:9.8.0'`
- `compile 'com.google.firebase:firebase-core:9.8.0'
- `compile 'com.android.support:design:25.0.1'`


This sample uses the Gradle build system. To build this project, use the "gradlew build" command or use "Import Project" in Android Studio.

If you have any questions I'd be happy to try and help. Please contact me at: john@coronite.net.

# License
This is a public domain work under [CC0 1.0](https://creativecommons.org/publicdomain/zero/1.0/). Feel free to use it as you see fit.
