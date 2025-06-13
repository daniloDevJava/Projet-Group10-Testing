import { test, expect } from '@playwright/test';

// Les fausses données restent les mêmes
const mockSuccessTokens = {
  accessToken: 'mocked-access-token-from-playwright',
  refreshToken: 'mocked-refresh-token-from-playwright',
};

const mockErrorResponse = {
  message: 'Identifiants invalides (mocked)',
};

// --- LA MEILLEURE FAÇON DE FAIRE ---
// Le pattern à intercepter. On le met dans une constante pour ne pas faire de faute de frappe.
const LOGIN_API_ROUTE = '**/users/login';

test.describe('Login Page with API Mocking', () => {

  test('should log in successfully with valid credentials (mocked)', async ({ page }) => {
    // --- On utilise le glob pattern ici aussi ---
    await page.route(LOGIN_API_ROUTE, async route => {
      console.log(`[SUCCESS MOCK] Intercepted a POST request to ${route.request().url()}`);
      
      // On vérifie que la méthode est bien POST, c'est une bonne pratique
      expect(route.request().method()).toBe('POST');
      
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(mockSuccessTokens),
      });
    });

    // Le reste du test est parfait
    await page.goto('/login');
    await page.getByTestId('login-input').fill('any-valid-user');
    await page.getByTestId('password-input').fill('any-valid-password');
    await page.getByTestId('login-button').click();

    await expect(page).toHaveURL(/.*\/vehicles/); // Utilise une expression régulière pour plus de flexibilité
    await expect(page.getByText('Pick the perfect vehicule for every occasion')).toBeVisible();
  });


  test('should show an error with invalid credentials (mocked)', async ({ page }) => {
    // On utilise le même glob pattern
    await page.route(LOGIN_API_ROUTE, async route => {
      console.log(`[ERROR MOCK] Intercepted a POST request to ${route.request().url()}`);
      
      expect(route.request().method()).toBe('POST');

      await route.fulfill({
        status: 401, // Unauthorized
        contentType: 'application/json',
        body: JSON.stringify(mockErrorResponse),
      });
    });

    // Le reste du test est parfait
    await page.goto('/login');
    await page.getByTestId('login-input').fill('any-invalid-user');
    await page.getByTestId('password-input').fill('any-wrong-password');
    await page.getByTestId('login-button').click();

    const errorMessage = page.getByTestId('error-message');
    await expect(errorMessage).toBeVisible();
    await expect(errorMessage).toContainText('Identifiants invalides (mocked)');
  });
});