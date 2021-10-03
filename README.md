# Cucina Android App

## Introduction

**This application is created only for educational purposes. All coincidences are accidental.**

Name ***Cucina*** comes from Italian and means "kitchen".

The essence of the project is to create **native android** application for the fictional pizza franchise.

Basic functionality: user registration and authorization, displaying news, creating and tracking orders, displaying cafes on the map. 

The application is translated into 4 languages: *English, Ukrainian, Russian, Polish*.

## Used Tools

### Language: Java 1.8
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
### [Cafe Control app](https://github.com/o-r-d-i-n-a-r-y/Cucina-admin)
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
This is the *start screen* of application. **StartActivity** is used to apply the application theme depending on the user's choice, redirect user to the **MainActivity** or to the **AuthorizationActivity** (if user isn't logged in). Also, **StartActivity** updates user's FCM token in order's DB table.

![StartActivity](https://i.postimg.cc/Hsfw4vc6/Screenshot-20211002-114720.png)

## MainActivity
**MainActivity** is the *main screen* of the application. It consists of *ToolBar*, *FragmentContainer* and *NavigationView*.

![FragmentContainer](https://i.postimg.cc/PJvTkQb5/Screenshot-20211002-123256.png)
![NavigationView](https://i.postimg.cc/XvxVKJ74/Screenshot-20211002-123247.png)

### There are 4 *Fragments* in **MainActivity**:

#### NewsFragment

By default, user is redirected to the **NewsFragment**. It is used to display news in *ViewPager* and *ListView*. If the news list length is less than 3, then full news list will be displayed both in *VIewPager* and *ListView*. Otherwise, the first half of list will be displayed in *ViewPager* and the second in *ListView*. When user clicks *ViewPager* or *ListView* item, **AnnoncementActivity** with the chosen **Announcement** starts.

**Announcement** class:

**param_name** | **param_type**
:---: | :---:
type\* | int
image_url | String
title | String
desc | String
end_date | String

\*type is an Integer, that takes values from 0 to 3: 0 - new opening, 1 - good news, 2 - warning, 3 - bad news.

Depending on a type of **Announcement**, *ListView* items may show different icons.

#### UserOrdersFragment

This *Fragment* is used to display User's orders. It contains *ListView* with the order list.

![UserOrdersFragment](https://i.postimg.cc/zDWRWsRv/Screenshot-20211002-132639.png)

*ListView Adapter* uses **Order** class:

**param_name** | **param_type**
:---: | :---:
name | String
phone | String
orderList | ArrayList<OrderDish*>
clarifications | String
cafeID | int
state | int
id | int

\***orderList** ArrayList uses **OrderDish** class:

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
imgUrls | ArrayList\<String\>
cafeID | int

Then, the **CafeActivity** starts.

![CafeActivity](https://i.postimg.cc/bYVwvkrf/Screenshot-20211002-141936.png)

#### OrderFragment
  
**OrderFragment** contains only *TabLayout*, *ViewPager* and 2 *FABs*: the first adds a new User menu, the second redirects user to the **OrderActivity** (the first is shown when user switches to the user menus tab, the second is shown when user orders at least 1 meal).

![OrderFragment1](https://i.postimg.cc/3J5SvGp6/Screenshot-20211002-181421.png)
![OrderFragment2](https://i.postimg.cc/K8fSvfKx/Screenshot-20211002-180257.png)

All main functional is realized in **OrderPageFragment** and **UserMenusFragment**.

**OrderPageFragment** contains *ListView*, which in turn contains *ViewPager* in its each item. All dishes are divided into grous (**DishGroup**), so each *ListView* item represents each category, and each *ViewPager* item represents each dish. There are 3 buttons in *ViewPager* item: to add 1 dish, to remove 1 dish, to see the information about the dish. Also, there are 2 *TextViews* with the amount of ordered dish and dishes' price.

**DishGroup** class:

**param_name** | **param_type**
:---: | :---:
name | String
dishes | ArrayList\<Dish\>

**OrderPageFragment** also used in **OrderFragment** in **OrderActivity**, if it's launched from **CafeActvity**. Its functional is the same, excepting the moment, that it doesn't use **OrderFragment** *ArrayList\<OrderDish\>* and directy adds dishes to the **OrderActivity** *Order* order.

```java
if(!usesActivityList) {   // boolean usesActivityList is used to show if we use the OrderFragment ArrayList or OrderActivity.order ArrayList
    if(OrderFragment.orderList.size() == 1) {
        OrderFragment.orderInterface.showHideFABNext(true);
    }
}
else {
    if(OrderActivity.order.getOrderList().size() == 1) {
        OrderFragment.orderInterface.showHideFABNext(true);
    }
}
```

**UserMenusFragment** is used to add dishes from User menus to the order, to change or delete User menus. When **OrderFragment** is called from **OrderActivity**, 'Add menu' *FAB* is disabled.

It contains only one *RecyclerView*. Each item represents each menu from User menus ArrayList.

**UserMenu** class:

**param_name** | **param_type**
:---: | :---:
id | String
name | String
dishes | ArrayList\<OrderDish\>

The principle of adding/removing dishes is the same as in the **OrderPageFragment**.

## SettingsActivity

**SettingsActivity** is only a container for **PreferenceFragment**. When the user changes the theme, after closing **SettingsActivity**, the whole application closes. This is done in order to avoid collateral problems when the application applies a new theme.

### PreferenceFragment

![PreferenceFragment](https://i.postimg.cc/hjZk4RqR/Screenshot-20211003-130124.png)

**PreferenceFragment** is the *PreferenceScreen*, which contains *Preferences* to:
- Change the theme
- Change the username
- Change the phone number
- Change the city
- Send bug report
- Go to the user agreement
- Show the current application version

The first one changes the application theme. When user chooses new color theme, the application closes after user leaves **SettingsActivity**.

The next three are used to change user data, sending *StringRequest* with the changed data. *Note*: when user wants to change city, it can't be done, while user has active orders (when state < 3). The same thing for the phone number changing. Also, when user changes the phone number to the taken one, he/she gets notified, that the phone number is already taken.

'Bug report' *Preference* redirects user to the Gmail app or notifies user that the Gmail app is not installed. 

![Gmail Redirect](https://i.postimg.cc/dVwcBYww/Screenshot-20211003-131948.png)

'User agreement' *Preference* opens user agreement sample in browser (I'm sorry, I don't have my own user agreement)

The last one needs no introduction.

### AuthorizationActivity

**AuthorizationActivity** is the simple activity to login user. It has 2 *EditText*, *Button* and *TextView*. If the user entered login data correctly, *StringRequest* will return all user data, which will be placed in new **User** object and then user will be redirected to the **MainActivity** and his/her logged in state will be saved in SharedPreferences. Otherwise, *StringRequest* will return error and user will be notified that login data is incorrect.

###### Note: password restoration is not available at this moment. It will be implemented in future updates (maybe)

![AuthActivity](https://i.postimg.cc/fLr0cCdQ/Screenshot-20211003-132922.png)

**User** class:

**param_name** | **param_type**
:---: | :---:
id | int
name | String
phone | String
city | String

The *TextView* below the login button redirects user to the **RegistrationActivity**

## RegistrationActivity

![GreetingFragment](https://i.postimg.cc/1tytyXj8/Screenshot-20211003-190520.png)
![InfoFragment](https://i.postimg.cc/85F1pvbP/Screenshot-20211003-190536.png)
![RegFragment](https://i.postimg.cc/RV1vZ4wf/Screenshot-20211003-190558.png)

This activity contains only *ViewPager* with a custom *FragmentPager* adapter (it can be swiped only programatically) and *FAB* to swipe *ViewPager* to the next item. There are 3 *Fragments* in the *FragmentPager*.

### GreetingFragment and InfoFragment

Nothing special. The first one is just the screen with the greeting (particles were used there). The second one is the screen with the enticing slogans. Just decorations.

### RegFragment

This *Fragment* does the whole stuff. It is quite similar to the **AuthorizationActivity**, but there user also has to enter his name, choose city and confirm his password. If somewhere user enters wrong data, he/she will be notified about that. Then, app sends request, and if it's successful and user's phone is not taken by anyone, user data will be added to the *users* DB table and user will be logged in with this data as in the **AuthorizationActivity**.

## OrderActivity

**OrderActivity** has already been mentioned before, this activity stores order information and sends it, when user goes to the last page of *FragmentPager*. This activity is similar to the **RegistrationActivity**, **OrderActivity** uses the same *FragmentPager* adapter. As in the **RegistrationActivity** there is only one *FAB* and *ViewPager*. When the user is at the first or last page of the *FragmentPager*, ***onBackPressed*** closes the activity. Otherwise, it will swipe one page back.

**OrderActivity** can be started not only from **MainActivity**, but also from **CafeActivity** (if user presses 'Order here' button). If **it** is started from **MainActivity**, then the *Fragment* at the first page will be **MapFragment**. Otherwise it will be **OrderFragment** at the first page.

**MapFragment** and **OrderFragment** have already been mentioned before, so they will not be described there. Excepting these two, activity has 3 fragments.

### DescFragment

This *Fragment* is used to get order clarifications from *EditText*. Clarifications string cannot be null.

### ConfFragment

**ConfFragment** displays all order information: address of cafe, clarifications and dishes list. It is used to check the order before sending.

### ResFragment

The last one is **ResFragment**. It shows the result of sending the request.

![DescFragment](https://i.postimg.cc/SsLC7VRj/Screenshot-20211003-200809.png)
![ConfFragment](https://i.postimg.cc/jSWfkR2p/Screenshot-20211003-200819.png)
![ResFragment](https://i.postimg.cc/sXzZXSmz/Screenshot-20211003-200831.png)

## AnnouncementActivity

As it has been mentioned in **NewsFragment** part, **AnnouncementActivity** starts when the user clicks at the **NewsFragment** *ListView* or *ViewPager* item (chooses announcement). There is an *ImageView* at the top of activity and a *CardView* with announcement header and body *TextViews*. If the device language is not russian, there will be also translate *FAB*, which translates header and body text into device language. If it is clicked again, it translates text back into russian. 

![AnnouncementActivity1](https://i.postimg.cc/X7XKtRv0/Screenshot-20211003-202922.png)
![AnnouncementActivity2](https://i.postimg.cc/5yrzxttW/Screenshot-20211003-202944.png)
