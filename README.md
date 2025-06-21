<p align="center">
  <img src="https://raw.githubusercontent.com/PedroMazarini/resapublic/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp" alt="Resa Logo" width="60" style="vertical-align: middle;"/>
  <span style="font-size: 2.5em; vertical-align: middle; margin-left: 10px;">Resa</span>
</p>


<p align="center">
  <b>Your personal travel companion for public transport in Västra Götaland, Sweden.</b><br>
  <a href="https://play.google.com/store/apps/details?id=com.mazarini.resa">
    <img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" alt="Get it on Google Play" height="60"/>
  </a>
</p>

---

## 🚀 About Resa

Resa is a modern, open-source Android app designed to make public transport in Västra Götaland, Sweden, easier and more intuitive. Powered by the [Västtrafik API](https://developer.vasttrafik.se/), Resa helps you plan journeys, search for stops, save favorite locations, and get real-time departure information—all in a beautiful, user-friendly interface.

Inspired by the need for a better, privacy-friendly, and more customizable public transport app, Resa is built for both locals and visitors who want a seamless travel experience.

> “I built Resa to bring a fresh, modern, and open-source alternative to public transport apps in Sweden. It’s fast, beautiful, and built with a strong focus on user experience and reliability.”  
> — [Pedro Mazarini](https://www.linkedin.com/in/pedromazarini/)

---

## 🏗️ Architecture

Resa is built using a clean, modular architecture inspired by best practices in modern Android development:

- **MVVM (Model-View-ViewModel)** for clear separation of concerns.
- **Kotlin Coroutines & Flow** for asynchronous and reactive programming.
- **Jetpack Compose** for declarative, modern UI.
- **Hilt** for dependency injection.
- **Room** for local database caching.
- **Repository Pattern** to abstract data sources (remote and local).
- **Single-Activity Architecture** with Navigation Compose.

---

## 🛠️ Tech Stack

- **Kotlin** (100% codebase)
- **Jetpack Compose** (UI)
- **Coroutines & Flow** (async/reactive)
- **Room** (local database)
- **Hilt** (DI)
- **Retrofit** (network)
- **Västtrafik API** ([docs](https://developer.vasttrafik.se/))
- **Material Design 3**
- **Firebase** (analytics, crashlytics)
- **JUnit, MockK** (testing)

---

## ✨ Features

- 🚏 Search for stops and locations
- 🗺️ Plan journeys with real-time data
- 📍 Live track vehicles
- ⭐ Save favorite and recent locations
- ⏰ View upcoming departures
- 🏠 Pin journeys to home
- 🌙 Light & dark theme support
- 🔒 100% privacy—no tracking, no ads


## 🧑‍💻 Getting Started

1. Clone the repo:
   ```bash
   git clone https://github.com/PedroMazarini/resapublic.git
   ```
2. Get a [Västtrafik API key](https://developer.vasttrafik.se/).
3. Add your API key to `local.properties`:
   ```ini
   VASTTRAFIK_API_KEY="your_api_key_here"
   ```
4. Open in Android Studio and build!

### Architecture Description

#### Top level
    .
    ├── data                    # Data sources
    ├── domain                  # Business logic, usecases and repository abstractions
    └── ui                      # View layer with screens components

#### Data
    ...
    ├── cache                   # Cache local data source
    ├── di                      # Dependency Injection modules
    ├── mappers                 # Entity mappers
    └── network                 # Remote data source

#### Domain
    ...
    ├── usecase                 # Features usecases
    ├── di                      # Dependency Injection modules
    ├── mappers                 # Entity mappers

#### UI
    ...
    ui
    ├── navigation              # Compose navigation graph and routes
    ├── theme                   # Custom Theme, colors and text styles
    ├── screens                 # Screens UI components
---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!  
Feel free to check the [issues page](https://github.com/PedroMazarini/resapublic/issues) or submit a pull request.

---

## 📚 Learn More

- [Västtrafik API Documentation](https://developer.vasttrafik.se/)
- [LinkedIn Post about Resa](https://www.linkedin.com/feed/update/urn:li:activity:7216003091194650625/)
- [Play Store Listing](https://play.google.com/store/apps/details?id=com.mazarini.resa)

---

## 📝 License

This project is [MIT](LICENSE) licensed.

---

<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp" alt="Resa Logo" width="60"/>
  <br>
  <b>Made with ❤️ in Göteborg by <a href="https://www.linkedin.com/in/pedromazarini/">Pedro Mazarini</a></b>
</p>

