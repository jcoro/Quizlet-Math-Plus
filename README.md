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
Quizlet Math Plus requires entering flash card terms and definitions on Quizlet.com using a "JSON-like" syntax (like that shown below). Quizlet Math Plus allows for the following data fields:

### Term

- "id": An identifier for the flash card (shown in the upper-left).
- "link-text": Text for a link to relevant web content (shown in the upper-right).
- "link-href": URL for the link to relevant web content.
- "branding-text": Text for a link to "branding" for an organization or educational institution (shown in the lower-right).
- "branding-url": URL for the link to "branding" for an organization or educational institution.
- "header": The main text for the term / question.
- "list-style-type": CSS list-style-type property (e.g., "none", "lower-alpha", "lower-roman", etc.) to display multiple-part or multiple-choice questions.
"list-items": An array of strings representing the list items.

### Term JSON
```json
{
  "meta": {
    "id": "Definition 1.1",
    "link-text": "1.1 Variables and Statements",
    "link-href": "http://www.coronite.net/training/discrete_mathematics/discrete_mathematics_lesson1.php#section-1",
    "branding-text": "Set Created By: Coronite Creative",
    "branding-url": "http://www.coronite.net"
  },
  "header": "Define the following term:",
  "list-style-type": "none",
  "list-items": [
    "Statement"
  ]
}
```
### Definition
- "header": The definition or answer to the question.
- "list-style-type": CSS list-style-type property (e.g., "none", "lower-alpha", "lower-roman", etc.) to display answers tomultiple-part or multiple-choice questions.
"list-items": An array of strings representing the list item answers.


### Definition JSON
```json
{
  "header": "A universal declarative sentence which is either true or false.",
  "list-style-type": "lower-alpha",
  "list-items": []
}
```

Quizlet Math Plus also displays "standard-format" Quizlet flash cards appropriately.

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