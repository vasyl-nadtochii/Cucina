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

**param_name** | **param_type**
:---: | :---:
name | String
phone | String
orderList | ArrayList<OrderDish*>
clarifications | String
cafeID | int
state | int
id | int

\***orderList** ArrayList uses OrderDish class:

**param_name** | **param_type**
:---: | :---:
amount | int
price | int
name | String

When the user clicks on *ListView* item, an *AlertDialog* with order information pops up.

![Order information](https://i.postimg.cc/C1k0X5BQ/Screenshot-20211002-140606.png)

If the order **state == 3** (declined), there will be button in *AlertDialog* to delete this order from *ListView* and database. *FloatingActionButton* (*FAB*) in the bottom-right corner does the same thing, but it removes all user's declined orders.

#### MapFragment

**MapFragment** contains *Fragment* container (SupportMapFragment), three *FABs* in the bottom-right corner: button to move to the current location, to refresh cafe markers, to move to **DescFragment** (hidden when **MapFragment** is launched from **NavigationView**), information layout with chosen cafe address (hidden when **MapFragment** is launched from **NavigationView**).

**MapFragment** is used not only in **MainActivity**, but also in **OrderActivity**.

First image is **MapFragment** launched from **NavigationView** and the second is **MapFragment** in **OrderActivity**:

![1](https://i.postimg.cc/3Jvm84dx/Screenshot-20211002-141917.png)
![2](https://i.postimg.cc/Z5YNrfwC/Screenshot-20211002-142701.png)

Cafe markers have different colors, depending on a state:
  1. Opened - green
  2. Only for takeaway - yellow
  3. Soon closing - red

Closed cafes are not shown on the map. If cafe state == 3 (closes soon), it's not shown on the map, when **MapFragment** is launched from **OrderActivity**.

When the **MapFragment** is started, the application retrieves cafes' coordinates from DB and then creates *Markers* on the map. 

When the user clicks one of that *Markers* (**MapFragment** should be started from OrderActivity\*), selected **Cafe** *address* and *id* are added to the user's **Order**.

Otherwise, the application retrieves all the data about the chosen cafe and loads it into **Cafe** object. 

**Cafe** class:

**param_name** | **param_type**
:---: | :---:
latitude | double
longitude | double
state | int
address | String
cafeID | int
imgUrls | ArrayList<String>

Then, the **CafeActivity** starts.

![CafeActivity](https://i.postimg.cc/bYVwvkrf/Screenshot-20211002-141936.png)
