/* ==========================================================================
   NiK-TV Universal Web Application Engine (HLS, D-Pad, Tabs, Storage)
   ========================================================================== */

document.addEventListener('DOMContentLoaded', () => {
  
  // --- STATE MANAGEMENT ---
  const state = {
    activeUrl: 'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8',
    currentTitle: 'Big Buck Bunny HLS Stream',
    currentCategory: 'all',
    activeTab: 'channels',
    hls: null,
    isTvMode: false,
    favorites: JSON.parse(localStorage.getItem('niktv_favorites') || '[]'),
    history: JSON.parse(localStorage.getItem('niktv_history') || '[]')
  };

  // --- DEMO CHANNELS DATA ---
  const channels = [
    {
      id: 'c1',
      title: 'Big Buck Bunny (HLS Direct)',
      category: 'movies',
      tag: '1080p60',
      url: 'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8',
      thumb: 'https://images.unsplash.com/photo-1593784991095-a205069470b6?q=80&w=600&auto=format&fit=crop'
    },
    {
      id: 'c2',
      title: 'Tears of Steel (4K Cinema Stream)',
      category: 'movies',
      tag: '4K HLS',
      url: 'https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8',
      thumb: 'https://images.unsplash.com/photo-1485846234645-a62644f84728?q=80&w=600&auto=format&fit=crop'
    },
    {
      id: 'c3',
      title: 'Sintel Open Movie Relayed Stream',
      category: 'movies',
      tag: 'HD STREAM',
      url: 'https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8',
      thumb: 'https://images.unsplash.com/photo-1536440136628-849c177e76a1?q=80&w=600&auto=format&fit=crop'
    },
    {
      id: 'c4',
      title: 'Global News Network Live',
      category: 'news',
      tag: 'LIVE NEWS',
      url: 'https://cdn.jwplayer.com/manifests/pXvldhi2.m3u8',
      thumb: 'https://images.unsplash.com/photo-1585829365295-ab7cd400c167?q=80&w=600&auto=format&fit=crop'
    },
    {
      id: 'c5',
      title: 'Extreme Action Sports Feed',
      category: 'sports',
      tag: '60 FPS',
      url: 'https://playertest.longtailvideo.com/adaptive/oceans/oceans.m3u8',
      thumb: 'https://images.unsplash.com/photo-1461896836934-ffe607ba8211?q=80&w=600&auto=format&fit=crop'
    },
    {
      id: 'c6',
      title: 'Chill Lofi Beats & Cyber Visuals',
      category: 'music',
      tag: '24/7 AUDIO',
      url: 'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8',
      thumb: 'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?q=80&w=600&auto=format&fit=crop'
    }
  ];

  // --- DOM ELEMENTS ---
  const videoPlayer = document.getElementById('video-player');
  const iframePlayer = document.getElementById('iframe-player');
  const urlInput = document.getElementById('stream-url-input');
  const btnLoad = document.getElementById('btn-load-stream');
  const btnFullscreen = document.getElementById('btn-toggle-fullscreen');
  const btnVoice = document.getElementById('btn-voice-search');
  const btnAddFav = document.getElementById('btn-add-favorite');
  const btnCinema = document.getElementById('btn-cinema-mode');
  const channelsGrid = document.getElementById('channels-grid');
  const favoritesGrid = document.getElementById('favorites-grid');
  const historyGrid = document.getElementById('history-grid');
  const playerLoader = document.getElementById('player-loader');
  const streamTitle = document.getElementById('current-stream-title');
  const voiceModal = document.getElementById('voice-modal');
  const btnCloseVoice = document.getElementById('btn-close-voice');
  const deviceTag = document.getElementById('device-tag');

  // --- DEVICE DETECTOR ---
  function detectDeviceType() {
    const ua = navigator.userAgent.toLowerCase();
    const width = window.innerWidth;

    if (ua.includes('tv') || ua.includes('leanback') || ua.includes('smarttv') || width >= 1920) {
      deviceTag.innerHTML = '<i class="fa-solid fa-tv"></i> Smart TV / Large Screen';
      state.isTvMode = true;
    } else if (ua.includes('ipad') || (width >= 768 && width <= 1024)) {
      deviceTag.innerHTML = '<i class="fa-solid fa-tablet-screen-button"></i> iPad / Tablet View';
    } else if (width < 768) {
      deviceTag.innerHTML = '<i class="fa-solid fa-mobile-screen-button"></i> Mobile View';
    } else {
      deviceTag.innerHTML = '<i class="fa-solid fa-laptop"></i> Desktop / Laptop View';
    }
  }

  // --- STREAM PLAYER ENGINE ---
  function loadStream(url, title = 'Custom Stream') {
    if (!url) return;
    
    state.activeUrl = url;
    state.currentTitle = title;
    urlInput.value = url;
    streamTitle.textContent = title;
    playerLoader.classList.remove('hidden');

    // Add to history
    if (!state.history.some(h => h.url === url)) {
      state.history.unshift({ title, url, timestamp: new Date().toLocaleTimeString() });
      if (state.history.length > 20) state.history.pop();
      localStorage.setItem('niktv_history', JSON.stringify(state.history));
      renderHistory();
    }

    // Check YouTube or Embed iframe vs HLS
    if (url.includes('youtube.com') || url.includes('youtu.be')) {
      videoPlayer.classList.add('hidden');
      iframePlayer.classList.remove('hidden');
      let videoId = url.includes('v=') ? url.split('v=')[1].split('&')[0] : url.split('/').pop();
      iframePlayer.src = `https://www.youtube.com/embed/${videoId}?autoplay=1`;
      playerLoader.classList.add('hidden');
      return;
    }

    iframePlayer.classList.add('hidden');
    videoPlayer.classList.remove('hidden');

    // HLS.js video loading
    if (Hls.isSupported() && url.endsWith('.m3u8')) {
      if (state.hls) state.hls.destroy();
      
      const hls = new Hls({ enableWorker: true, lowLatencyMode: true });
      state.hls = hls;
      hls.loadSource(url);
      hls.attachMedia(videoPlayer);
      hls.on(Hls.Events.MANIFEST_PARSED, () => {
        videoPlayer.play().catch(() => {});
        playerLoader.classList.add('hidden');
      });
      hls.on(Hls.Events.ERROR, () => {
        playerLoader.classList.add('hidden');
      });
    } else {
      videoPlayer.src = url;
      videoPlayer.play().then(() => {
        playerLoader.classList.add('hidden');
      }).catch(() => {
        playerLoader.classList.add('hidden');
      });
    }

    updateFavoriteButtonState();
  }

  // --- RENDER CHANNELS GRID ---
  function renderChannels(category = 'all') {
    channelsGrid.innerHTML = '';
    const filtered = category === 'all' ? channels : channels.filter(c => c.category === category);

    filtered.forEach(ch => {
      const isFav = state.favorites.some(f => f.url === ch.url);
      const card = document.createElement('div');
      card.className = 'channel-card focusable';
      card.tabIndex = 0;
      card.innerHTML = `
        <div class="card-thumb">
          <img src="${ch.thumb}" alt="${ch.title}" loading="lazy">
          <span class="card-tag">${ch.tag}</span>
          <button class="card-fav-btn ${isFav ? 'active' : ''}" data-url="${ch.url}"><i class="fa-${isFav ? 'solid' : 'regular'} fa-star"></i></button>
          <div class="card-play-overlay">
            <div class="play-circle"><i class="fa-solid fa-play"></i></div>
          </div>
        </div>
        <div class="card-info">
          <div class="card-title">${ch.title}</div>
          <div class="card-meta"><i class="fa-solid fa-layer-group"></i> ${ch.category.toUpperCase()}</div>
        </div>
      `;

      card.addEventListener('click', () => loadStream(ch.url, ch.title));
      card.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') loadStream(ch.url, ch.title);
      });

      const favBtn = card.querySelector('.card-fav-btn');
      favBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        toggleFavorite(ch);
        renderChannels(category);
        renderFavorites();
      });

      channelsGrid.appendChild(card);
    });
  }

  // --- RENDER FAVORITES GRID ---
  function renderFavorites() {
    favoritesGrid.innerHTML = '';
    if (state.favorites.length === 0) {
      favoritesGrid.innerHTML = '<p class="text-sub" style="grid-column:1/-1; padding:20px; text-align:center;">No favorites bookmarked yet.</p>';
      return;
    }

    state.favorites.forEach(fav => {
      const card = document.createElement('div');
      card.className = 'channel-card focusable';
      card.tabIndex = 0;
      card.innerHTML = `
        <div class="card-thumb">
          <img src="${fav.thumb || 'https://images.unsplash.com/photo-1593784991095-a205069470b6?q=80&w=600'}" alt="${fav.title}">
          <span class="card-tag">FAVORITE</span>
          <button class="card-fav-btn active"><i class="fa-solid fa-star"></i></button>
        </div>
        <div class="card-info">
          <div class="card-title">${fav.title}</div>
        </div>
      `;

      card.addEventListener('click', () => loadStream(fav.url, fav.title));
      favoritesGrid.appendChild(card);
    });
  }

  // --- RENDER HISTORY GRID ---
  function renderHistory() {
    historyGrid.innerHTML = '';
    if (state.history.length === 0) {
      historyGrid.innerHTML = '<p class="text-sub" style="grid-column:1/-1; padding:20px; text-align:center;">History is empty.</p>';
      return;
    }

    state.history.forEach(item => {
      const card = document.createElement('div');
      card.className = 'channel-card focusable';
      card.tabIndex = 0;
      card.innerHTML = `
        <div class="card-info" style="padding:16px;">
          <div class="card-title">${item.title}</div>
          <div class="card-meta">${item.timestamp} • ${item.url}</div>
        </div>
      `;
      card.addEventListener('click', () => loadStream(item.url, item.title));
      historyGrid.appendChild(card);
    });
  }

  // --- FAVORITES TOGGLE ---
  function toggleFavorite(channel) {
    const idx = state.favorites.findIndex(f => f.url === channel.url);
    if (idx >= 0) {
      state.favorites.splice(idx, 1);
    } else {
      state.favorites.push(channel);
    }
    localStorage.setItem('niktv_favorites', JSON.stringify(state.favorites));
    updateFavoriteButtonState();
  }

  function updateFavoriteButtonState() {
    const isFav = state.favorites.some(f => f.url === state.activeUrl);
    btnAddFav.innerHTML = isFav 
      ? '<i class="fa-solid fa-star" style="color:#FFC107"></i> <span>BOOKMARKED</span>'
      : '<i class="fa-regular fa-star"></i> <span>FAVORITE</span>';
  }

  // --- EVENT LISTENERS ---
  btnLoad.addEventListener('click', () => loadStream(urlInput.value, 'Custom Stream'));
  
  urlInput.addEventListener('keydown', (e) => {
    if (e.key === 'Enter') loadStream(urlInput.value, 'Custom Stream');
  });

  btnFullscreen.addEventListener('click', () => {
    if (!document.fullscreenElement) {
      document.documentElement.requestFullscreen();
    } else {
      document.exitFullscreen();
    }
  });

  btnAddFav.addEventListener('click', () => {
    toggleFavorite({ title: state.currentTitle, url: state.activeUrl, thumb: '' });
    renderFavorites();
  });

  btnCinema.addEventListener('click', () => {
    const playerWrapper = document.getElementById('player-container');
    playerWrapper.scrollIntoView({ behavior: 'smooth' });
  });

  // TABS CONTROLLER
  document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
      document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));

      btn.classList.add('active');
      const targetTab = btn.dataset.tab;
      document.getElementById(`panel-${targetTab}`).classList.add('active');
    });
  });

  // CATEGORY PILLS CONTROLLER
  document.querySelectorAll('.pill-btn').forEach(pill => {
    pill.addEventListener('click', () => {
      document.querySelectorAll('.pill-btn').forEach(p => p.classList.remove('active'));
      pill.classList.add('active');
      renderChannels(pill.dataset.category);
    });
  });

  // VOICE MODAL
  btnVoice.addEventListener('click', () => voiceModal.classList.remove('hidden'));
  btnCloseVoice.addEventListener('click', () => voiceModal.classList.add('hidden'));

  // --- TV D-PAD SPATIAL NAVIGATION KEYBOARD SUPPORT ---
  window.addEventListener('keydown', (e) => {
    if (['ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight'].includes(e.key)) {
      const focusables = Array.from(document.querySelectorAll('.focusable'));
      const active = document.activeElement;
      const index = focusables.indexOf(active);

      if (index === -1) {
        if (focusables.length > 0) focusables[0].focus();
        return;
      }

      let nextIndex = index;
      if (e.key === 'ArrowRight') nextIndex = (index + 1) % focusables.length;
      if (e.key === 'ArrowLeft') nextIndex = (index - 1 + focusables.length) % focusables.length;
      if (e.key === 'ArrowDown') nextIndex = Math.min(index + 3, focusables.length - 1);
      if (e.key === 'ArrowUp') nextIndex = Math.max(index - 3, 0);

      focusables[nextIndex].focus();
    } else if (e.key.toLowerCase() === 'f') {
      btnFullscreen.click();
    }
  });

  // --- INITIALIZATION ---
  detectDeviceType();
  window.addEventListener('resize', detectDeviceType);
  renderChannels();
  renderFavorites();
  renderHistory();
  loadStream(state.activeUrl, state.currentTitle);
});
