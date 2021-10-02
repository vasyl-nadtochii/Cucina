# Cucina Android App

## Introduction

**This application is created only for educational purposes. All coincidences are accidental.**

Name *Cucina* comes from Italian and means "kitchen". The essence of the project is to create **native android** application for the fictional pizza franchise. Basic functionality: user registration and authorization, displaying news, creating and tracking orders, displaying cafes on the map.

## Used Tools

### Language: Java 1.8 <br>
### Gradle version: 4.0.1
### Libraries (non-Android):
  - [Glide](https://github.com/bumptech/glide)
  - [Volley](https://github.com/google/volley)
  - [Gson](https://github.com/google/gson)
  - [FAB Progress Circle](https://github.com/JorgeCastilloPrz/FABProgressCircle)
  - [Particles](https://github.com/plattysoft/Leonids)
  - [Firebase Android SDK](https://github.com/firebase/firebase-android-sdk)
### Databases: MySQL 8.0, SQLite (for User menus)

## Additional Information

### [Content Telegram bot](https://github.com/o-r-d-i-n-a-r-y/Cucina-Content-Tg-Bot)
### Min. SDK version: 7.0 (Nougat)

## App Components

### Activities and Fragments

- StartActivity
- MainActivity
  - NewsFragment
  - MapFragment
  - OrderFragment (OrderPageFragment, UserMenusFragment)
  - UserOrdersFragment
- SettingsActivity
  - PreferenceFragment
- AuthorizationActivity
- RegistrationActivity
  - GreetingFragment
  - InfoFragment
  - RegFragment
- OrderActivity
  - OrderFragment/MapFragment (depends on where the Activity was called)
  - MapFragment
  - DescFragment
  - ResultFragment
- AnnouncementActivity
- CafeActivity
- ComplaintActivity
- DishDescActivity
- UserMenuActivity

## StartActivity
This is the *Start screen* of application. 
