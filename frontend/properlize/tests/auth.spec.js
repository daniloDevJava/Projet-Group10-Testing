// Fichier : tests/auth.spec.js

import { test, expect } from '@playwright/test';

const API_URL = 'http://localhost:9000/users';
const MOCK_USER_NAME = 'Imelda'; // Le nom qui doit être stocké et affiché

test.describe('Authentication Flow', () => {

  // Test de connexion
  test('should allow a user to log in successfully and redirect to /home', async ({ page, context }) => {
    // Mock de l'API qui renvoie ce que le composant Login attend pour réussir
    await page.route(`${API_URL}/login`, async route => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({
          name: MOCK_USER_NAME,
          accessToken: 'fake-access-token-123',
          refreshToken: 'fake-refresh-token-456'
        }),
      });
    });

    // Ignorer les alertes qui peuvent bloquer le test
    page.on('dialog', dialog => dialog.accept());

    await page.goto('/login');

    await page.getByPlaceholder('Login').fill('imelda_user');
    await page.getByPlaceholder('Email').fill('imelda@test.com');
    await page.getByPlaceholder('Password').fill('password123');
    await page.getByRole('button', { name: 'Login' }).click();

    // Attendre que la navigation vers /home soit terminée
    await page.waitForURL('**/home', { timeout: 10000 });

    // Vérifier qu'on est sur la bonne page ET que le contenu est correct
    await expect(page).toHaveURL(/.*\/home/);
    await expect(page.getByRole('heading', { name: `Bienvenue ${MOCK_USER_NAME}` })).toBeVisible();
  });

  // Test d'inscription
  test('should allow a user to sign up successfully and redirect to /login', async ({ page }) => {
    // Mock de l'API qui renvoie un succès pour l'inscription
    await page.route(`${API_URL}/add`, async route => {
      await route.fulfill({
        status: 201,
        contentType: 'application/json',
        body: JSON.stringify({ id: 999, name: 'new_user', email: 'new@test.com' }),
      });
    });

    page.on('dialog', dialog => dialog.accept());

    await page.goto('/sign');

    await page.getByPlaceholder('Login').fill('new_user');
    await page.getByPlaceholder('Email').fill('new@test.com');
    await page.getByPlaceholder('Password').fill('new_password');
    await page.getByRole('button', { name: 'Create an account' }).click();

    // Attendre la redirection vers /login
    await page.waitForURL('**/login', { timeout: 10000 });

    // Vérifier qu'on est bien arrivé
    await expect(page).toHaveURL(/.*\/login/);
    await expect(page.getByRole('heading', { name: 'Login to your account' })).toBeVisible();
  });

  // Test d'échec de connexion (reste le même car il n'y a pas de redirection)
  test('should show an error on failed login', async ({ page }) => {
    await page.route(`${API_URL}/login`, async route => {
      await route.fulfill({ status: 401 });
    });

    page.on('dialog', async dialog => {
      expect(dialog.message()).toContain('incorrect');
      await dialog.accept();
    });

    await page.goto('/login');
    await page.getByPlaceholder('Login').fill('wronguser');
    await page.getByPlaceholder('Email').fill('wrong@example.com');
    await page.getByPlaceholder('Password').fill('wrongpassword');
    await page.getByRole('button', { name: 'Login' }).click();

    // S'assurer qu'on est toujours sur la page de login
    await expect(page).toHaveURL(/.*\/login/);
  });
});