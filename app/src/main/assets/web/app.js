/* ==========================================================================
   Nik-TV Premium OTT Web Launcher Engine (URLs, D-Pad Remote, Search, Storage)
   ========================================================================== */

document.addEventListener('DOMContentLoaded', () => {

  // --- DEFAULT WEBSITES DATABASE (OTT POSTER CARDS) ---
  const defaultWebsites = [
    {
      id: 'w1',
      name: 'Netflix',
      url: 'https://www.netflix.com',
      category: 'Movies',
      isLive: false,
      isFavorite: true,
      logo: 'https://cdn.iconscout.com/icon/free/png-256/free-netflix-logo-icon-download-in-svg-png-gif-file-formats--brand-social-media-pack-logos-icons-2673960.png?f=webp&w=256',
      poster: 'https://images.unsplash.com/photo-1574375927938-d5a98e8ffe85?q=80&w=800&auto=format&fit=crop',
      desc: 'Watch Movies, TV Shows, and Originals online.'
    },
    {
      id: 'w2',
      name: 'YouTube',
      url: 'https://www.youtube.com',
      category: 'Live TV',
      isLive: true,
      isFavorite: true,
      logo: 'https://cdn.iconscout.com/icon/free/png-256/free-youtube-logo-icon-download-in-svg-png-gif-file-formats--social-media-video-brand-pack-logos-icons-2673775.png?f=webp&w=256',
      poster: 'https://images.unsplash.com/photo-1611162617213-7d7a39e9b1d7?q=80&w=800&auto=format&fit=crop',
      desc: 'Enjoy live streams, music videos, and trending content.'
    },
    {
      id: 'w3',
      name: 'JioCinema / JioTV',
      url: 'https://www.jiocinema.com',
      category: 'Sports',
      isLive: true,
      isFavorite: true,
      logo: 'https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?q=80&w=200&auto=format&fit=crop',
      poster: 'https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?q=80&w=800&auto=format&fit=crop',
      desc: 'Live Cricket, IPL, FIFA, Movies & TV Serials.'
    },
    {
      id: 'w4',
      name: 'Disney+ Hotstar',
      url: 'https://www.hotstar.com',
      category: 'Movies',
      isLive: false,
      isFavorite: true,
      logo: 'https://images.unsplash.com/photo-1536440136628-849c177e76a1?q=80&w=200&auto=format&fit=crop',
      poster: 'https://images.unsplash.com/photo-1536440136628-849c177e76a1?q=80&w=800&auto=format&fit=crop',
      desc: 'Blockbuster Movies, Marvel, Star Wars & Live Cricket.'
    },
    {
      id: 'w5',
      name: 'Twitch TV',
      url: 'https://www.twitch.tv',
      category: 'Sports',
      isLive: true,
      isFavorite: false,
      logo: 'https://cdn.iconscout.com/icon/free/png-256/free-twitch-logo-icon-download-in-svg-png-gif-file-formats--social-media-stream-gaming-brand-pack-logos-icons-2673756.png?f=webp&w=256',
      poster: 'https://images.unsplash.com/photo-1542751371-adc38448a05e?q=80&w=800&auto=format&fit=crop',
      desc: 'Live Esports tournaments and gaming broadcasts.'
    },
    {
      id: 'w6',
      name: 'BBC News',
      url: 'https://www.bbc.com/news',
      category: 'News',
      isLive: true,
      isFavorite: false,
      logo: 'https://images.unsplash.com/photo-1585829365295-ab7cd400c167?q=80&w=200&auto=format&fit=crop',
      poster: 'https://images.unsplash.com/photo-1585829365295-ab7cd400c167?q=80&w=800&auto=format&fit=crop',
      desc: '24/7 Global breaking news and video reports.'
    },
    {
      id: 'w7',
      name: 'Spotify Web',
      url: 'https://open.spotify.com',
      category: 'Music',
      isLive: false,
      isFavorite: false,
      logo: 'https://cdn.iconscout.com/icon/free/png-256/free-spotify-logo-icon-download-in-svg-png-gif-file-formats--social-media-music-brand-pack-logos-icons-2673771.png?f=webp&w=256',
      poster: 'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?q=80&w=800&auto=format&fit=crop',
      desc: 'Millions of songs and podcasts.'
    },
    {
      id: 'w8',
      name: 'TED Talks',
      url: 'https://www.ted.com',
      category: 'Education',
      isLive: false,
      isFavorite: false,
      logo: 'https://images.unsplash.com/photo-1524178232363-1fb2b075b655?q=80&w=200&auto=format&fit=crop',
      poster: 'https://images.unsplash.com/photo-1524178232363-1fb2b075b655?q=80&w=800&auto=format&fit=crop',
      desc: 'Ideas worth spreading from global visionaries.'
    },
    {
      id: 'w9',
      name: 'Amazon Shopping',
      url: 'https://www.amazon.com',
      category: 'Shopping',
      isLive: false,
      isFavorite: false,
      logo: 'https://cdn.iconscout.com/icon/free/png-256/free-amazon-logo-icon-download-in-svg-png-gif-file-formats--social-media-brand-pack-logos-icons-2673752.png?f=webp&w=256',
      poster: 'https://images.unsplash.com/photo-1523474253046-8cd2748b5fd2?q=80&w=800&auto=format&fit=crop',
      desc: 'Online shopping for electronics, fashion & products.'
    },
    {
      id: 'w10',
      name: 'TechCrunch',
      url: 'https://techcrunch.com',
      category: 'Technology',
      isLive: false,
      isFavorite: false,
      logo: 'https://images.unsplash.com/photo-1518770660439-4636190af475?q=80&w=200&auto=format&fit=crop',
      poster: 'https://images.unsplash.com/photo-1518770660439-4636190af475?q=80&w=800&auto=format&fit=crop',
      desc: 'Latest technology, startup & gadget reviews.'
    }
  ];

  // --- STATE ---
  let websites = JSON.parse(localStorage.getItem('niktv_websites') || JSON.stringify(defaultWebsites));
  let recentHistory = JSON.parse(localStorage.getItem('niktv_recent') || '[]');
  let activeCategory = 'All';
  let searchQuery = '';

  // --- DOM ELEMENTS ---
  const splashScreen = document.getElementById('splash-screen');
  const sideMenu = document.getElementById('side-menu');
  const btnToggleMenu = document.getElementById('btn-toggle-menu');
  const searchInput = document.getElementById('search-input');
  const btnClearSearch = document.getElementById('btn-clear-search');
  const categoryChips = document.getElementById('category-chips');
  const heroCarousel = document.getElementById('hero-carousel');
  const ottSectionsContainer = document.getElementById('ott-sections-container');
  const emptyState = document.getElementById('empty-state');
  const contextMenu = document.getElementById('context-menu');
  const totalSitesCount = document.getElementById('total-sites-count');
  const currentCatDisplay = document.getElementById('current-cat-display');
  const clockDisplay = document.getElementById('clock-display');
  const netStatus = document.getElementById('net-status');
  const offlineState = document.getElementById('offline-state');

  // --- INITIALIZE SPLASH SCREEN ---
  setTimeout(() => {
    splashScreen.style.opacity = '0';
    setTimeout(() => splashScreen.classList.add('hidden'), 500);
  }, 1200);

  // --- CLOCK & NETWORK STATUS ---
  function updateClock() {
    const now = new Date();
    clockDisplay.textContent = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  }
  setInterval(updateClock, 1000);
  updateClock();

  function updateNetworkStatus() {
    if (navigator.onLine) {
      netStatus.className = 'status-pill online';
      netStatus.innerHTML = '<i class="fa-solid fa-circle"></i> ONLINE';
      offlineState.classList.add('hidden');
    } else {
      netStatus.className = 'status-pill danger-glow';
      netStatus.innerHTML = '<i class="fa-solid fa-circle"></i> OFFLINE';
      offlineState.classList.remove('hidden');
    }
  }
  window.addEventListener('online', updateNetworkStatus);
  window.addEventListener('offline', updateNetworkStatus);
  updateNetworkStatus();

  // --- SIDE MENU NAVIGATION ---
  btnToggleMenu.addEventListener('click', () => {
    sideMenu.classList.toggle('expanded');
    sideMenu.classList.toggle('collapsed');
  });

  document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', () => {
      document.querySelectorAll('.nav-item').forEach(i => i.classList.remove('active'));
      document.querySelectorAll('.view-panel').forEach(v => v.classList.remove('active'));
      
      item.classList.add('active');
      const targetId = item.dataset.target;
      document.getElementById(targetId).classList.add('active');

      if (targetId === 'favorites-view') renderFavoritesGrid();
      if (targetId === 'recent-view') renderRecentGrid();
      if (targetId === 'categories-view') renderCategoriesView();
    });
  });

  // --- RENDER HERO CAROUSEL ("CONTINUE BROWSING") ---
  function renderHeroCarousel() {
    heroCarousel.innerHTML = '';
    const featured = websites.slice(0, 4);

    featured.forEach(site => {
      const card = document.createElement('div');
      card.className = 'hero-card focusable';
      card.tabIndex = 0;
      card.innerHTML = `
        <img class="hero-bg" src="${site.poster}" alt="${site.name}">
        <div class="hero-overlay">
          <div class="hero-meta">
            <img class="hero-logo" src="${site.logo}" alt="${site.name}">
            <span class="hero-name">${site.name}</span>
          </div>
          <p class="hero-desc">${site.desc}</p>
          <div class="hero-actions">
            <button class="btn-open focusable"><i class="fa-solid fa-play"></i> Open Website</button>
            <button class="btn-icon-circle focusable btn-fav-toggle" data-id="${site.id}"><i class="fa-${site.isFavorite ? 'solid' : 'regular'} fa-star"></i></button>
          </div>
        </div>
      `;

      card.addEventListener('click', () => openWebsite(site));
      const favBtn = card.querySelector('.btn-fav-toggle');
      favBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        toggleFavorite(site.id);
        renderAll();
      });

      heroCarousel.appendChild(card);
    });
  }

  // --- RENDER OTT SECTIONS (CATEGORIES AS OTT ROWS) ---
  function renderOTTSections() {
    ottSectionsContainer.innerHTML = '';
    
    // Filter websites
    let filtered = websites.filter(site => {
      const matchesCat = (activeCategory === 'All' || site.category === activeCategory);
      const matchesSearch = site.name.toLowerCase().includes(searchQuery.toLowerCase()) || 
                            site.category.toLowerCase().includes(searchQuery.toLowerCase());
      return matchesCat && matchesSearch;
    });

    totalSitesCount.textContent = filtered.length;
    currentCatDisplay.innerHTML = `<i class="fa-solid fa-layer-group"></i> ${activeCategory}`;

    if (filtered.length === 0) {
      emptyState.classList.remove('hidden');
      return;
    }
    emptyState.classList.add('hidden');

    // Group by categories
    const categories = activeCategory === 'All' 
      ? ['Favorites', 'Live TV', 'Movies', 'Sports', 'News', 'Music', 'Education', 'Shopping', 'Technology']
      : [activeCategory];

    categories.forEach(cat => {
      let catSites = [];
      if (cat === 'Favorites') {
        catSites = filtered.filter(s => s.isFavorite);
      } else {
        catSites = filtered.filter(s => s.category === cat);
      }

      if (catSites.length === 0) return;

      const section = document.createElement('div');
      section.className = 'ott-section';
      section.innerHTML = `
        <div class="section-title-row">
          <h2 class="section-title"><i class="fa-solid fa-${getCategoryIcon(cat)} text-primary"></i> ${cat}</h2>
          <span class="section-subtitle">${catSites.length} websites</span>
        </div>
        <div class="ott-grid"></div>
      `;

      const grid = section.querySelector('.ott-grid');
      catSites.forEach(site => {
        grid.appendChild(createWebsiteCard(site));
      });

      ottSectionsContainer.appendChild(section);
    });
  }

  // --- CREATE WEBSITE MOVIE POSTER CARD ---
  function createWebsiteCard(site) {
    const card = document.createElement('div');
    card.className = 'website-card focusable';
    card.tabIndex = 0;
    card.innerHTML = `
      <div class="card-poster">
        <img src="${site.poster}" alt="${site.name}" loading="lazy">
        <div class="card-glass-overlay">
          <div class="card-top-row">
            ${site.isLive ? '<span class="live-badge"><i class="fa-solid fa-circle"></i> LIVE</span>' : '<span></span>'}
            <i class="fa-${site.isFavorite ? 'solid' : 'regular'} fa-star fav-star ${site.isFavorite ? 'active' : ''}" data-id="${site.id}"></i>
          </div>
          <div class="card-bottom-row">
            <img class="site-icon" src="${site.logo}" alt="${site.name}">
            <div class="site-info">
              <div class="site-name">${site.name}</div>
              <div class="site-cat"><span class="online-dot"></span> ${site.category}</div>
            </div>
          </div>
        </div>
      </div>
    `;

    // Click -> Navigate directly to website URL
    card.addEventListener('click', () => openWebsite(site));
    card.addEventListener('keydown', (e) => {
      if (e.key === 'Enter') openWebsite(site);
    });

    // Favorite star click
    const favStar = card.querySelector('.fav-star');
    favStar.addEventListener('click', (e) => {
      e.stopPropagation();
      toggleFavorite(site.id);
      renderAll();
    });

    // TV Remote Long Press / Context Menu Trigger
    card.addEventListener('contextmenu', (e) => {
      e.preventDefault();
      showContextMenu(e.clientX, e.clientY, site);
    });

    return card;
  }

  // --- OPEN WEBSITE URL DIRECTLY ---
  function openWebsite(site) {
    // Add to recently opened history
    recentHistory = recentHistory.filter(h => h.id !== site.id);
    recentHistory.unshift(site);
    if (recentHistory.length > 10) recentHistory.pop();
    localStorage.setItem('niktv_recent', JSON.stringify(recentHistory));

    // Open target website URL directly
    window.location.href = site.url;
  }

  // --- FAVORITES TOGGLE ---
  function toggleFavorite(id) {
    websites = websites.map(w => w.id === id ? { ...w, isFavorite: !w.isFavorite } : w);
    localStorage.setItem('niktv_websites', JSON.stringify(websites));
  }

  // --- CATEGORY FILTERS ---
  categoryChips.querySelectorAll('.chip').forEach(chip => {
    chip.addEventListener('click', () => {
      categoryChips.querySelectorAll('.chip').forEach(c => c.classList.remove('active'));
      chip.classList.add('active');
      activeCategory = chip.dataset.category;
      renderOTTSections();
    });
  });

  // --- REAL-TIME SEARCH ---
  searchInput.addEventListener('input', (e) => {
    searchQuery = e.target.value;
    if (searchQuery.length > 0) {
      btnClearSearch.classList.remove('hidden');
    } else {
      btnClearSearch.classList.add('hidden');
    }
    renderOTTSections();
  });

  btnClearSearch.addEventListener('click', () => {
    searchInput.value = '';
    searchQuery = '';
    btnClearSearch.classList.add('hidden');
    renderOTTSections();
  });

  // --- CONTEXT MENU ---
  function showContextMenu(x, y, site) {
    document.getElementById('ctx-name').textContent = site.name;
    document.getElementById('ctx-logo').src = site.logo;

    contextMenu.style.left = `${Math.min(x, window.innerWidth - 250)}px`;
    contextMenu.style.top = `${Math.min(y, window.innerHeight - 250)}px`;
    contextMenu.classList.remove('hidden');

    document.getElementById('ctx-open').onclick = () => openWebsite(site);
    document.getElementById('ctx-favorite').onclick = () => {
      toggleFavorite(site.id);
      contextMenu.classList.add('hidden');
      renderAll();
    };
    document.getElementById('ctx-copy').onclick = () => {
      navigator.clipboard.writeText(site.url);
      alert(`Copied URL: ${site.url}`);
      contextMenu.classList.add('hidden');
    };
  }

  document.addEventListener('click', () => contextMenu.classList.add('hidden'));

  // --- HELPER FUNCTION FOR CATEGORY ICONS ---
  function getCategoryIcon(cat) {
    const icons = {
      'Favorites': 'star',
      'Movies': 'film',
      'Live TV': 'tv',
      'Sports': 'futbol',
      'News': 'newspaper',
      'Kids': 'child-reaching',
      'Music': 'music',
      'Education': 'graduation-cap',
      'Shopping': 'cart-shopping',
      'Technology': 'microchip'
    };
    return icons[cat] || 'layer-group';
  }

  // --- RENDER ALL VIEWS ---
  function renderFavoritesGrid() {
    const favGrid = document.getElementById('favorites-grid');
    favGrid.innerHTML = '';
    const favs = websites.filter(s => s.isFavorite);
    favs.forEach(site => favGrid.appendChild(createWebsiteCard(site)));
  }

  function renderRecentGrid() {
    const recGrid = document.getElementById('recent-grid');
    recGrid.innerHTML = '';
    recentHistory.forEach(site => recGrid.appendChild(createWebsiteCard(site)));
  }

  function renderCategoriesView() {
    const container = document.getElementById('all-categories-container');
    container.innerHTML = '';
    renderOTTSections();
  }

  function renderAll() {
    renderHeroCarousel();
    renderOTTSections();
  }

  // --- TV D-PAD REMOTE SPATIAL NAVIGATION ---
  window.addEventListener('keydown', (e) => {
    if (['ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight'].includes(e.key)) {
      const focusables = Array.from(document.querySelectorAll('.focusable:not(.hidden)'));
      const active = document.activeElement;
      const index = focusables.indexOf(active);

      if (index === -1) {
        if (focusables.length > 0) focusables[0].focus();
        return;
      }

      let nextIndex = index;
      if (e.key === 'ArrowRight') nextIndex = (index + 1) % focusables.length;
      if (e.key === 'ArrowLeft') nextIndex = (index - 1 + focusables.length) % focusables.length;
      if (e.key === 'ArrowDown') nextIndex = Math.min(index + 5, focusables.length - 1);
      if (e.key === 'ArrowUp') nextIndex = Math.max(index - 5, 0);

      focusables[nextIndex].focus();
    }
  });

  // --- INITIAL RENDER ---
  document.body.classList.add('tv-mode');
  renderAll();
});
