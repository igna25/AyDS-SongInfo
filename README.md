# SongInfo - Android Music Information App

Una aplicación Android desarrollada en **Kotlin** que permite a los usuarios buscar información detallada sobre canciones y artistas integrando múltiples fuentes de datos externas.

---

## 📌 Descripción

**SongInfo** es una aplicación móvil nativa para Android que proporciona información completa sobre canciones y artistas. Utiliza **Spotify** como fuente principal de búsqueda de canciones y agrega detalles enriquecidos de varias APIs externas (Last.fm, Wikipedia, New York Times).

La aplicación está diseñada siguiendo patrones arquitectónicos sólidos como **MVC**, **MVP** y **Clean Architecture ligera**, con separación clara de responsabilidades y bajo acoplamiento entre capas.

---

## 📋 Funcionalidades principales

### Pantalla de búsqueda (Home)
- Búsqueda de canciones por término usando Spotify API.
- Visualización de información básica: título, artista, imagen, URL directa.
- Acceso directo a la canción en Spotify.
- Almacenamiento local de búsquedas frecuentes.
- Navegación a detalles del artista.

### Pantalla de detalles del artista (More Details)
- Información enriquecida del artista desde múltiples fuentes:
  - **Last.fm**: biografía y estadísticas del artista.
  - **Wikipedia**: información enciclopédica.
  - **New York Times**: artículos y noticias relacionadas.
- Presentación de información en tarjetas organizadas.
- Enlaces directos a fuentes externas.

### Características transversales
- Persistencia de datos local con **Room** (SQLite).
- Carga asíncrona de datos con Observable pattern.
- Interfaz reactiva que responde a cambios de estado.
- Manejo robusto de errores y casos sin conexión.

---

## 🏗️ Arquitectura

El proyecto combina distintos estilos arquitectónicos adaptándose a cada pantalla:

### Pantalla `home` - Estilo MVC
```
HomeView (UI)
    ↑
    ↓
HomeController (Coordinador de eventos)
    ↑
    ↓
HomeModel (Lógica + Observable)
    ↑
    ↓
SongRepository (Acceso a datos)
    ↑
    ├─→ SpotifyLocalStorage (BD local)
    └─→ SpotifyTrackService (API remota)
```

### Pantalla `moredetails` - Estilo MVP / Clean Architecture
```
MoreDetailsViewActivity (UI)
    ↑
    ↓
MoreDetailsPresenter (Presentación + Mappers)
    ↑
    ↓
MoreDetailsRepository (Interfaz de dominio)
    ↑
    ├─→ Broker (Agregador de fuentes)
    │   ├─→ LastFMProxy
    │   ├─→ WikipediaProxy
    │   └─→ NewYorkTimesProxy
    └─→ MoreDetailsLocalStorage (BD local)
```
---

## 🛠️ Tecnologías

### Android & Kotlin
- **Kotlin** 1.x - Lenguaje principal
- **Android API Level** 21+ (Android 5.0+)
- **Activity** - Ciclo de vida y vista

### Persistencia
- **Room** - ORM para SQLite local

### Networking & APIs Externas
- **Spotify Web API** - Búsqueda de canciones
- **Last.fm API** - Información de artistas
- **Wikipedia API** - Datos enciclopédicos
- **New York Times API** - Noticias y artículos
- **HTTP Client** (native Kotlin)

### Librerías Auxiliares
- **Picasso** - Carga y caché de imágenes
- **Observable Pattern** (módulo `observer` propio)

### Testing
- **JUnit 4** - Tests unitarios
- **MockK** - Mocking en tests

---
## 📦 Requisitos previos

- **Android Studio** 2021.1+ o superior
- **JDK 11** o superior
- **Android SDK** API Level 21+
- **Gradle** 7.0+ (incluido en Android Studio)
- Credenciales de APIs externas:
  - Spotify Web API (Client ID + Secret)
  - Last.fm API Key
  - Wikipedia API (pública, sin credenciales)
  - New York Times API Key

---

## 🚀 Instalación y configuración

### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/AyDS-SongInfo.git
cd AyDS-SongInfo
```
### 2. Abrir en Android Studio
- Importar proyecto como **Gradle Project**
- Sincronizar dependencias (Gradle Sync)

### 3. Compilar y ejecutar
```bash
# Desde Android Studio o terminal:
./gradlew build              # Compilar
./gradlew installDebug       # Instalar en emulador/dispositivo
```

O directamente desde Android Studio:
- `Build` → `Make Project`
- `Run` → `Run 'app'` (seleccionar dispositivo/emulador)

---

## 💭 Posibles mejoras futuras

### Mejoras arquitectónicas recomendadas
- **Asincronía moderna**: Reemplazar `Thread { }.start()` por **Coroutines** para un manejo más seguro y eficiente.
- **Framework de DI**: Considerar **Hilt** o **Koin** si el proyecto escala, para evitar inyección manual.
- **Patrón unificado**: Adoptar **MVVM** globalmente para consistencia entre pantallas.
- **Pruebas mejoradas**: Expandir cobertura de tests, especialmente en repositorios y presenters.

### Features futuros
- Historial de búsquedas persistente.
- Recomendaciones basadas en historial.

---

##

Desarrollado basado en el proyecto académico resuelto en el contexto de **Arquitectura y Diseño de Software (AyDS)**.

