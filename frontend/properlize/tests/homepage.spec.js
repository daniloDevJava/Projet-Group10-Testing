// tests/homepage.spec.js

import { test, expect } from '@playwright/test';

test.describe('HomePage Tests', () => {

  test.beforeEach(async ({ page }) => {
    // Navigue vers la page d'accueil avant chaque test
    await page.goto('/');
  });

  test('should display the main static sections: Hero, CarList, and Footer', async ({ page }) => {
    // CORRECTION : On cible des éléments qui ne changent pas.
    
    // 1. Le logo dans la barre de navigation du Hero. C'est un point d'ancrage très stable.
    await expect(page.locator('.navbar').getByRole('img', { name: /Properlize Logo/i })).toBeVisible();

    // 2. Le titre de la section des voitures populaires.
    await expect(page.getByRole('heading', { name: 'Most popular' })).toBeVisible();

    // 3. Un lien dans le footer.
    await expect(page.getByRole('link', { name: 'À propos' })).toBeVisible();
  });

  test('should navigate to the login page when login button is clicked', async ({ page }) => {
    // Ce test était déjà bien, on le garde.
    await page.getByRole('button', { name: 'Login' }).click();
    await expect(page).toHaveURL(/.*\/login/);
    await expect(page.getByRole('heading', { name: 'Login to your account' })).toBeVisible();
  });

  test('should toggle the like button on a car card', async ({ page }) => {
    // CORRECTION : On ajoute une attente pour s'assurer que la carte est bien là avant d'interagir.
    const firstCarCard = page.locator('.car-card').first();
    await expect(firstCarCard).toBeVisible();

    const heartIcon = firstCarCard.locator('.heart');

    // Le reste de la logique est bonne.
    await expect(heartIcon).toHaveText('♡');
    await expect(heartIcon).not.toHaveClass(/liked/);
    await heartIcon.click();
    await expect(heartIcon).toHaveText('♥');
    await expect(heartIcon).toHaveClass(/liked/);
  });
});