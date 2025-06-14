import { test, expect } from '@playwright/test';

test.describe('Page d\'inscription', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173/sign');
  });

  test('Affichage initial de la page', async ({ page }) => {
    // Vérification des éléments visibles
    await expect(page.locator('.logo')).toBeVisible();
    await expect(page.locator('h2:has-text("Create an account")')).toBeVisible();
    await expect(page.locator('text=Already have an account?')).toBeVisible();
    await expect(page.locator('a:has-text("Login")')).toBeVisible();
    
    // Vérification des champs du formulaire
    await expect(page.locator('input[name="name"]')).toBeVisible();
    await expect(page.locator('input[name="email"]')).toBeVisible();
    await expect(page.locator('input[name="mdp"]')).toBeVisible();
    await expect(page.locator('button:has-text("Create an account")')).toBeVisible();
  });

  test('Fonctionnement du slider', async ({ page }) => {
    // Vérification que le slider change toutes les 3 secondes
    const firstSlideText = await page.locator('.slide-text').textContent();
    await page.waitForTimeout(3500); // Attendre un peu plus que l'intervalle
    const secondSlideText = await page.locator('.slide-text').textContent();
    
    expect(firstSlideText).not.toBe(secondSlideText);
    
    // Vérification des indicateurs du slider
    const indicators = page.locator('.indicator');
    await expect(indicators).toHaveCount(3);
  });

  test('Basculer la visibilité du mot de passe', async ({ page }) => {
    const passwordInput = page.locator('input[name="mdp"]');
    
    // Par défaut masqué
    await expect(passwordInput).toHaveAttribute('type', 'password');
    
    // Afficher le mot de passe
    await page.locator('text=👁️').click();
    await expect(passwordInput).toHaveAttribute('type', 'text');
    
    // Masquer à nouveau
    await page.locator('text=🙈').click();
    await expect(passwordInput).toHaveAttribute('type', 'password');
  });

  test('Validation du formulaire - champs vides', async ({ page }) => {
    await page.locator('button:has-text("Create an account")').click();
    
    // Vérifier que les champs sont marqués comme invalides
    const inputs = ['name', 'email', 'mdp'];
    for (const name of inputs) {
      const input = page.locator(`input[name="${name}"]`);
      const isInvalid = await input.evaluate((el) => !el.checkValidity());
      expect(isInvalid).toBeTruthy();
    }
  });

  test('Inscription réussie', async ({ page }) => {
    // Mock de la réponse API
    await page.route('http://localhost:9000/users/add', route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({ success: true })
      });
    });
    
    // Remplir le formulaire
    await page.locator('input[name="name"]').fill('newuser');
    await page.locator('input[name="email"]').fill('newuser@example.com');
    await page.locator('input[name="mdp"]').fill('securepassword123');
    
    // Intercepter la navigation
    const navigationPromise = page.waitForNavigation();
    await page.locator('button:has-text("Create an account")').click();
    await navigationPromise;
    
    // Vérifier la redirection vers /login
    await expect(page).toHaveURL('http://localhost:5173/login');
  });

  test('Échec de l\'inscription', async ({ page }) => {
    // Mock de la réponse API avec erreur
    await page.route('http://localhost:9000/users/add', route => {
      route.fulfill({
        status: 400,
        body: JSON.stringify({ error: 'Email already exists' })
      });
    });
  
    // Configurer le handler pour les alertes
    let alertMessage = '';
    page.on('dialog', async dialog => {
      alertMessage = dialog.message();
      await dialog.dismiss();
    });
  
    // Remplir le formulaire
    await page.locator('input[name="name"]').fill('existinguser');
    await page.locator('input[name="email"]').fill('existing@example.com');
    await page.locator('input[name="mdp"]').fill('password123');
  
    // Soumettre le formulaire
    await page.locator('button:has-text("Create an account")').click();
  
    // Vérifier l'alerte
    await page.waitForTimeout(1000); // Laisser le temps à l'alerte d'apparaître
    expect(alertMessage).toContain('Échec de la création du compte');
  });

  test('Navigation vers la page de login', async ({ page }) => {
    await page.locator('a:has-text("Login")').click();
    await expect(page).toHaveURL('http://localhost:5173/login');
  });

  test('Validation du format d\'email', async ({ page }) => {
    await page.locator('input[name="email"]').fill('invalid-email');
    await page.locator('button:has-text("Create an account")').click();
    
    const emailInput = page.locator('input[name="email"]');
    const isInvalid = await emailInput.evaluate((el) => !el.checkValidity());
    expect(isInvalid).toBeTruthy();
    
    // Vérifier le message de validation
    const validationMessage = await emailInput.evaluate((el) => el.validationMessage);
    expect(validationMessage).toContain('email');
  });
});