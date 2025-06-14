// @ts-check
import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: './tests',
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 1 : undefined,
  reporter: 'html',
  
  use: {
    baseURL: 'http://localhost:5173', // Assure-toi que le port est le bon (Vite utilise 5173 par défaut)
    trace: 'on-first-retry',
  },

  // CORRECTION : La section webServer est décommentée et activée.
  // C'est ESSENTIEL pour que GitHub Actions puisse lancer ton application.
  webServer: {
    // Adapte cette commande si tu utilises npm ('npm run dev')
    command: 'yarn dev', 
    url: 'http://localhost:5173',
    reuseExistingServer: !process.env.CI,
    stdout: 'ignore',
    stderr: 'pipe',
  },
  
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
    // Tu pourras activer les autres navigateurs plus tard si besoin
    /*
    {
      name: 'firefox',
      use: { ...devices['Desktop Firefox'] },
    },
    {
      name: 'webkit',
      use: { ...devices['Desktop Safari'] },
    },
    */
  ],
});