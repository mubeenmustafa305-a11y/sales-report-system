<template>
  <div class="min-h-screen bg-gradient-to-br from-slate-50 to-slate-100">
    <header class="border-b border-slate-200 bg-white/80 backdrop-blur sticky top-0 z-10">
      <div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-4 flex items-center justify-between">
        <router-link to="/" class="flex items-center gap-3">
          <div class="h-9 w-9 rounded-lg bg-brand-600 flex items-center justify-center text-white font-bold text-sm">SR</div>
          <div>
            <h1 class="text-lg font-semibold text-slate-900">Sales Report System</h1>
            <p class="text-xs text-slate-500">Async report generation dashboard</p>
          </div>
        </router-link>
      </div>
    </header>
    <main class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-8">
      <router-view />
    </main>
  </div>
</template>
