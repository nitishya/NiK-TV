/* ==========================================================================
   Nik-TV Premium OTT Web Launcher Engine (URLs, D-Pad Remote, Search, Storage)
   ========================================================================== */

document.addEventListener('DOMContentLoaded', () => {

  let websites = [];
  let recentHistory = JSON.parse(localStorage.getItem('niktv_recent') || '[]');
  let activeCategory = 'All';
  let searchQuery = '';

  // --- AVATAR PLACEHOLDER COMPONENT SYSTEM ---
  function getWebsiteInitial(name, url) {
    if (name && name.trim().length > 0) {
      const cleanName = name.trim();
      return cleanName.charAt(0).toUpperCase();
    }
    if (url && url.trim().length > 0) {
      try {
        const domain = new URL(url).hostname.replace(/^www\./, '');
        if (domain.length > 0) {
          return domain.charAt(0).toUpperCase();
        }
      } catch (e) {
        const cleanUrl = url.replace(/^(https?:\/\/)?(www\.)?/, '');
        if (cleanUrl.length > 0) return cleanUrl.charAt(0).toUpperCase();
      }
    }
    return '?';
  }

  function getGradientByName(name) {
    if (!name) return 'linear-gradient(135deg, #4A5568, #2D3748)';
    const lower = name.toLowerCase().trim();

    if (lower.includes('netflix')) return 'linear-gradient(135deg, #E50914, #8B0000)';
    if (lower.includes('hotstar') || lower.includes('disney')) return 'linear-gradient(135deg, #0284C7, #0369A1)';
    if (lower.includes('sony') || lower.includes('twitch')) return 'linear-gradient(135deg, #9333EA, #6B21A8)';
    if (lower.includes('espn') || lower.includes('jio') || lower.includes('sports')) return 'linear-gradient(135deg, #16A34A, #15803D)';
    if (lower.includes('google') || lower.includes('youtube')) return 'linear-gradient(135deg, #DC2626, #EA580C)';

    // Deterministic HSL color generator for all other website names
    let hash = 0;
    for (let i = 0; i < lower.length; i++) {
      hash = lower.charCodeAt(i) + ((hash << 5) - hash);
    }
    const h1 = Math.abs(hash) % 360;
    const h2 = (h1 + 40) % 360;
    return `linear-gradient(135deg, hsl(${h1}, 75%, 45%), hsl(${h2}, 85%, 35%))`;
  }

  function createAvatarPlaceholder(name, url, isCircle = false, extraClass = '') {
    const initial = getWebsiteInitial(name, url);
    const gradient = getGradientByName(name || url);
    const shapeClass = isCircle ? 'avatar-circle' : 'avatar-square';
    
    const container = document.createElement('div');
    container.className = `avatar-placeholder-container ${shapeClass} ${extraClass}`;
    container.style.background = gradient;
    
    const text = document.createElement('span');
    text.className = 'avatar-placeholder-text';
    text.textContent = initial;
    container.appendChild(text);

    return container;
  }

  function attachSafeImageLoading(imgElement, containerElement, name, url, isCircle = false) {
    if (!imgElement || !imgElement.getAttribute('src')) {
      const placeholder = createAvatarPlaceholder(name, url, isCircle);
      if (imgElement && imgElement.parentNode) {
        imgElement.parentNode.replaceChild(placeholder, imgElement);
      } else if (containerElement) {
        containerElement.appendChild(placeholder);
      }
      return;
    }

    const placeholder = createAvatarPlaceholder(name, url, isCircle);
    placeholder.classList.add('avatar-loading-placeholder');

    if (imgElement.parentNode) {
      imgElement.parentNode.insertBefore(placeholder, imgElement);
    }

    imgElement.classList.add('img-hidden');

    imgElement.onload = () => {
      imgElement.classList.remove('img-hidden');
      imgElement.classList.add('img-loaded');
      if (placeholder && placeholder.parentNode) {
        placeholder.style.opacity = '0';
        setTimeout(() => placeholder.remove(), 300);
      }
    };

    imgElement.onerror = () => {
      imgElement.remove();
      placeholder.classList.remove('avatar-loading-placeholder');
      placeholder.style.opacity = '1';
    };
  }
  async function loadWebsitesConfig() {
    try {
      const res = await fetch('websites.json');
      if (res.ok) {
        const data = await res.json();
        const savedFavs = JSON.parse(localStorage.getItem('niktv_fav_ids') || '[]');
        websites = data.map(item => ({
          ...item,
          isFavorite: savedFavs.includes(item.id) || item.isFavorite
        }));
      }
    } catch (e) {
      console.warn("Using fallback local data", e);
    }
    renderAll();
  }

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
        <img class="hero-bg" src="${site.poster || ''}" alt="${site.name || ''}">
        <div class="hero-overlay">
          <div class="hero-meta">
            <img class="hero-logo" src="${site.logo || ''}" alt="${site.name || ''}">
            <span class="hero-name">${site.name || 'Website'}</span>
          </div>
          <p class="hero-desc">${site.desc || ''}</p>
          <div class="hero-actions">
            <button class="btn-open focusable"><i class="fa-solid fa-play"></i> Open Website</button>
            <button class="btn-icon-circle focusable btn-fav-toggle" data-id="${site.id}"><i class="fa-${site.isFavorite ? 'solid' : 'regular'} fa-star"></i></button>
          </div>
        </div>
      `;

      const heroBg = card.querySelector('.hero-bg');
      attachSafeImageLoading(heroBg, card, site.name, site.url, false);

      const heroLogo = card.querySelector('.hero-logo');
      attachSafeImageLoading(heroLogo, card.querySelector('.hero-meta'), site.name, site.url, true);

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
      const matchesSearch = (site.name || '').toLowerCase().includes(searchQuery.toLowerCase()) || 
                            (site.category || '').toLowerCase().includes(searchQuery.toLowerCase());
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
        <img class="card-poster-img" src="${site.poster || ''}" alt="${site.name || ''}" loading="lazy">
        <div class="card-glass-overlay">
          <div class="card-top-row">
            ${site.isLive ? '<span class="live-badge"><i class="fa-solid fa-circle"></i> LIVE</span>' : '<span></span>'}
            <i class="fa-${site.isFavorite ? 'solid' : 'regular'} fa-star fav-star ${site.isFavorite ? 'active' : ''}" data-id="${site.id}"></i>
          </div>
          <div class="card-bottom-row">
            <img class="site-icon" src="${site.logo || ''}" alt="${site.name || ''}">
            <div class="site-info">
              <div class="site-name">${site.name || 'Website'}</div>
              <div class="site-cat"><span class="online-dot"></span> ${site.category || 'Portal'}</div>
            </div>
          </div>
        </div>
      </div>
    `;

    const posterImg = card.querySelector('.card-poster-img');
    attachSafeImageLoading(posterImg, card.querySelector('.card-poster'), site.name, site.url, false);

    const logoImg = card.querySelector('.site-icon');
    attachSafeImageLoading(logoImg, card.querySelector('.card-bottom-row'), site.name, site.url, true);

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
    document.getElementById('ctx-name').textContent = site.name || 'Website';
    const ctxLogo = document.getElementById('ctx-logo');
    ctxLogo.src = site.logo || '';
    attachSafeImageLoading(ctxLogo, ctxLogo.parentNode, site.name, site.url, true);

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
  loadWebsitesConfig();
});
