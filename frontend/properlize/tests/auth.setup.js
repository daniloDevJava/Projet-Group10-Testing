// tests/auth.setup.js
import { test as setup, expect } from '@playwright/test';

// Le chemin vers le fichier où la session sera sauvegardée
const authFile = 'playwright/.auth/user.json';
const LOGIN_API_ROUTE = '**/users/login';

setup('authenticate', async ({ page }) => {
  // On met en place le mock pour l'API de login, comme dans login.spec.js
  await page.route(LOGIN_API_ROUTE, async route => {
    await route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify({
        accessToken: 'super-secret-access-token-for-setup',
        refreshToken: 'super-secret-refresh-token-for-setup'
      }),
    });
  });

  // Actions pour se connecter
  await page.goto('/login');
  await page.getByTestId('login-input').fill('setup-user');
  await page.getByTestId('password-input').fill('setup-password');
  await page.getByTestId('login-button').click();

  // Attendre que la page post-connexion soit chargée
  // C'est un point de contrôle crucial pour s'assurer que le login a bien marché.
  await expect(page.getByText('Pick the perfect vehicule for every occasion')).toBeVisible();

  // Fin de l'authentification. On sauvegarde l'état (cookies, localStorage) dans le fichier.
  // C'est cette ligne qui CRÉE le fichier user.json !
  await page.context().storageState({ path: authFile });

  console.log(`Authentication state saved to ${authFile}`);
});