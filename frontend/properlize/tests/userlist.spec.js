import { test, expect } from '@playwright/test';

const API_URL = 'http://localhost:9000/users';

test.describe('UserList Core Functionality Tests', () => {
  test.beforeEach(async ({ page }) => {
    // Mock de base pour la liste des utilisateurs
    await page.route(`${API_URL}/all`, route => route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify([
        { id: 1, name: 'Alice', email: 'alice@example.com' },
        { id: 2, name: 'Bob', email: 'bob@example.com' }
      ])
    }));
    
    await page.goto('/users');
    await page.waitForLoadState('networkidle');
  });

  test('Page loads successfully', async ({ page }) => {
    // Vérification basique que la page a chargé
    await expect(page).toHaveURL(/users/);
    await expect(page.locator('table')).toBeVisible();
  });

  test('Initial user list display', async ({ page }) => {
    // Vérifie que les utilisateurs mockés sont affichés
    const userRows = page.locator('table tbody tr');
    await expect(userRows).toHaveCount(2);
  });

  test('Add user form interaction', async ({ page }) => {
    // Mock pour l'ajout
    await page.route(`${API_URL}/add`, route => route.fulfill({
      status: 200,
      body: JSON.stringify({ id: 3, name: 'Charlie', email: 'charlie@example.com' })
    }));

    // Ouvre le formulaire d'ajout
    await page.locator('button:has-text("+ ADD")').click();
    
    // Vérifie que le formulaire est visible
    await expect(page.locator('form')).toBeVisible();
  });

  test('User count updates correctly', async ({ page }) => {
    // Vérifie que le compteur affiche le bon nombre
    const countText = page.locator('.footer b');
    await expect(countText).toHaveText('2');
  });
});