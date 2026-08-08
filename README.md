# 📺 Nik-TV — Premium OTT-Style Web Launcher

Nik-TV is a production-grade, highly optimized, and premium OTT-style website launcher. Designed specifically with a TV-first user experience, it serves as a central hub to access and bookmark favorite web portals, streaming sites, and custom URLs using rich visual cards, spatial D-pad remote controls, and dynamic layouts.

**Live Deployment URL:** [https://ni-k-tv.vercel.app/](https://ni-k-tv.vercel.app/)

---

## ✨ Features Implemented

*   **📺 Android TV Spatial Navigation Ready**: Full support for D-Pad keyboard keys (`ArrowUp`, `ArrowDown`, `ArrowLeft`, `ArrowRight`, `Enter`, and `Escape`) for fluid remote-based control.
*   **🌓 Unified Theme System**: Toggle seamlessly between Light (Default), Dark (OLED Black), and System Theme preference with persistent `localStorage` synchronization. Shortcut key `T` toggles theme instantly.
*   **📝 Interactive Portal Manager**: Add new website shortcuts directly inside the application using a polished modal complete with name, URL, thumbnail, and category properties. Saved securely inside local storage.
*   **🔍 Advanced Live Filter**: Real-time fuzzy query filter matching names, domains, categories, and descriptions.
*   **🖼️ AvatarPlaceholder Component**: Dynamic glassmorphic initial/avatar placeholders displaying brand-matching or deterministic gradients whenever an image fails to load or is invalid.
*   **📱 Fully Responsive Grid**: Optimized interface scaling beautifully across desktop screens, tablets, phones, and 43-inch/large Smart TVs.

---

## 🛠️ Run Locally

1. **Clone the repository:**
   ```bash
   git clone https://github.com/nitishya/NiK-TV.git
   cd NiK-TV
   ```

2. **Install local server dependency:**
   ```bash
   npm install
   ```

3. **Start the development server:**
   ```bash
   npm start
   ```
   Open [http://localhost:8080](http://localhost:8080) to test your launcher dashboard locally.

---

## 🚀 Vercel Deployment

This project is fully structured for zero-config Vercel deployments. The static router rewrites to the `/web` directories are declared in [vercel.json](vercel.json).

To deploy from your terminal:
```bash
npm install -g vercel
vercel
```

---

## ✒️ Author
Developed with ❤️ by **[Nitish Kumar Yadav](https://github.com/nitishya)** (Software Engineer).
