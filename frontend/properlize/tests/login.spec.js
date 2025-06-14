import { test, expect } from '@playwright/test';

test.describe('Page de Login', () => {

  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173/login'); 
  });

  test('Affichage initial de la page', async ({ page }) => {
    await expect(page.getByPlaceholder('Login')).toBeVisible();
    await expect(page.getByPlaceholder('Email')).toBeVisible();
    await expect(page.getByPlaceholder('Password')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Login' })).toBeVisible();
  });

  test('Fonctionnement du slider', async ({ page }) => {
    const slideText = page.locator('.slide-text');
    const firstSlide = await slideText.innerText();
    await page.waitForTimeout(4000);
    const secondSlide = await slideText.innerText();
    expect(secondSlide).not.toBe(firstSlide);
  });

  test('Basculer la visibilité du mot de passe', async ({ page }) => {
    const passwordInput = page.getByPlaceholder('Password');
    await expect(passwordInput).toHaveAttribute('type', 'password');

    await page.locator('.toggle-password').click();

    await expect(passwordInput).toHaveAttribute('type', 'text');
  });

  test('Validation du formulaire - champs vides', async ({ page }) => {
    await page.getByRole('button', { name: 'Login' }).click();

    const emailInput = page.getByPlaceholder('Email');
    await expect(emailInput).toHaveAttribute('required', '');
    await expect(page.locator('form:invalid')).toBeVisible(); // Confirme que le formulaire est bloqué

  });

  test('Soumission réussie du formulaire (mock)', async ({ page }) => {
    await page.route('**/users/login', route =>
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({ success: true }),
      })
    );

    await page.getByPlaceholder('Login').fill('testuser');
    await page.getByPlaceholder('Email').fill('test@example.com');
    await page.getByPlaceholder('Password').fill('testpass');
    await page.getByRole('button', { name: 'Login' }).click();

    await expect(page).toHaveURL(/.*home/);
  });

  test('Échec de la connexion', async ({ page }) => {
    await page.goto('/login');
  
    // 1. Préparer le listener d'alerte AVANT le clic
    page.on('dialog', async dialog => {
      expect(dialog.message()).toMatch(/incorrect/i); // Vérifie le contenu du message
      await dialog.dismiss(); // Ferme l'alerte
    });
  
    // 2. Remplir les champs avec de mauvaises infos
    await page.getByPlaceholder('Login').fill('wronguser');
    await page.getByPlaceholder('Email').fill('wrong@example.com');
    await page.getByPlaceholder('Password').fill('wrongpass');
  
    // 3. Cliquer sur le bouton de login
    await page.getByRole('button', { name: 'Login' }).click();
  
    // 4. Attendre un petit instant pour que l'alerte ait le temps d'apparaître
    await page.waitForTimeout(500); // (Optionnel mais prudent)
  });
  

  test('Navigation vers la page d\'inscription', async ({ page }) => {
    await page.getByRole('link', { name: 'Sign up' }).click();
    await expect(page).toHaveURL(/.*\/sign/);
  });

});
