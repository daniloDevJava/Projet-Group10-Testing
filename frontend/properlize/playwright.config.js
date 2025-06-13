// @ts-check
import { defineConfig, devices } from '@playwright/test';

/**
 * @see https://playwright.dev/docs/test-configuration
 */
export default defineConfig({
  // Le dossier où se trouvent tes fichiers de test
  testDir: './tests',
  
  // Timeout global pour chaque test (en millisecondes). 60 secondes est plus prudent.
  timeout: 60 * 1000, 

  // Timeout pour les assertions `expect` (en millisecondes)
  expect: {
    timeout: 10000,
  },

  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 1 : undefined,
  reporter: 'html',

  /* --- SECTION LA PLUS IMPORTANTE : 'use' ET 'webServer' --- */
  
  use: {
    // --- 1. L'URL DE BASE DE TON APPLICATION ---
    // Change le port si ton application utilise autre chose que 5173 (ex: 3000)
    baseURL: 'http://localhost:5173',

    // Enregistre une "trace" de l'exécution du test s'il échoue,
    // ce qui te permet de le rejouer étape par étape. Très utile pour le débogage.
    trace: 'on-first-retry',
  },

  // --- 2. DÉMARRAGE AUTOMATIQUE DU SERVEUR DE DÉVELOPPEMENT ---
  // Playwright va lancer cette commande AVANT de lancer les tests.
  // Plus besoin d'avoir deux terminaux ouverts !
  webServer: {
    // La commande pour démarrer ton serveur (vérifie ton package.json)
    command: 'npm run dev', // ou 'npm start'
    // L'URL que Playwright doit attendre avant de considérer que le serveur est prêt
    url: 'http://localhost:5173',
    // Si tu lances les tests alors que le serveur tourne déjà, il le réutilisera
    reuseExistingServer: !process.env.CI,
  },


  /* --- 3. CONFIGURATION DES PROJETS ET DE L'AUTHENTIFICATION --- */
  projects: [
    // --- PROJET DE SETUP ---
    // Il s'exécute en premier, se connecte, et crée le fichier user.json.
    {
      name: 'setup',
      // Fais en sorte qu'il ne lance QUE le fichier de setup.
      // Renomme ton auth.spec.js en auth.setup.js pour que ça corresponde.
      testMatch: /.*\.setup\.js/, 
    },

    // --- PROJETS DE TESTS QUI NÉCESSITENT UNE CONNEXION ---
    {
      name: 'chromium-authenticated',
      use: { 
        ...devices['Desktop Chrome'],
        // Utilise l'état de stockage sauvegardé
        storageState: 'playwright/.auth/user.json',
      },
      // Ces tests ne se lanceront que si le 'setup' a réussi.
      dependencies: ['setup'],
      // Ne lance PAS les tests de login ici.
      testIgnore: /.*login\.spec\.js/, 
    },

    // --- PROJETS DE TESTS "PUBLICS" (PAS BESOIN DE CONNEXION) ---
    {
        name: 'chromium-public',
        use: { ...devices['Desktop Chrome'] },
        // Ne lance QUE les tests de login ici.
        testMatch: /.*login\.spec\.js/,
    },
    
    // Tu peux faire de même pour firefox et webkit si tu les utilises...
  ],
});