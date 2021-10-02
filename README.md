# Cucina Android App

## Introduction

**This application is created only for educational purposes. All coincidences are accidental.**

Name *Cucina* comes from Italian and means "kitchen".

The essence of the project is to create **native android** application for the fictional pizza franchise.

Basic functionality: user registration and authorization, displaying news, creating and tracking orders, displaying cafes on the map. 

The application is translated into 4 languages: English, Ukrainian, Russian, Polish.

## Used Tools

### Language: Java 1.8 <br>
### Gradle version: 4.0.1
### [Backend scripts](https://github.com/o-r-d-i-n-a-r-y/Cucina-php-Scripts)
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
This is the *start screen* of application. **StartActivity** is used to apply the application theme depending on the user's choice, redirect user to the **MainActivity** or to the **AuthorizationActivity** (if user isn't logged in). Also, **StartActivity** updates user's FCM token in order's table.

![StartActivity](https://i.postimg.cc/Hsfw4vc6/Screenshot-20211002-114720.png)

## MainActivity
**MainActivity** is the *main screen* of the application. It consists of *ToolBar*, *FragmentContainer* and *NavigationView*.

![FragmentContainer](https://i.postimg.cc/PJvTkQb5/Screenshot-20211002-123256.png)
![NavigationView](https://i.postimg.cc/XvxVKJ74/Screenshot-20211002-123247.png)

### There are 4 *Fragments* in **MainActivity**:

#### NewsFragment

By default, user is redirected to the **NewsFragment**. It is used to display news in *ViewPager* and *ListView*. If the news list length is less than 3, then full news list will be displayed both in *VIewPager* and *ListView*. Otherwise, the first half of list will be displayed in *ViewPager* and the second in *ListView*.

#### UserOrdersFragment

This *Fragment* is used to display User's orders. It contains *ListView* with the order list.

![UserOrdersFragment](https://i.postimg.cc/zDWRWsRv/Screenshot-20211002-132639.png)

*ListView Adapter* uses ***Order*** class:

**param_name** | **type**
:---: | :---:
name | String
phone | String
orderList | ArrayList<OrderDish*>
clarifications | String
cafeID | int
state | int
id | int
