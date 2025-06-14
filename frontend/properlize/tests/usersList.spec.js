import { test, expect } from '@playwright/test';

test.describe('Gestion des utilisateurs', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173', { waitUntil: 'networkidle' });

    // Vérifie que le titre principal est présent
    await expect(page.locator('h1')).toHaveText('UserList');
  });

  test('Chargement des utilisateurs', async ({ page }) => {
    // Vérifie que le titre principal est correct
    await expect(page.locator('h1')).toHaveText('UserList');

    // Vérifie qu'au moins une ligne de tableau est présente
    const rows = page.locator('tbody tr');
    await expect(rows).toHaveCountGreaterThan(0);
  });

  test('Création d’un nouvel utilisateur', async ({ page }) => {
    // Clique sur le bouton avec aria-label
    await page.locator('button[aria-label="Ajouter un utilisateur"]').click();

    // Remplit le formulaire
    await page.getByPlaceholder('Nom complet').fill('Test Nom');
    await page.getByPlaceholder('Email').fill('testutilisateur@example.com');
    await page.getByPlaceholder('Mot de passe').fill('test1234');

    // Soumet
    await page.getByRole('button', { name: 'Enregistrer' }).click();

    // Vérifie que l'utilisateur est bien ajouté dans le tableau
    const newUser = page.locator('tr', { hasText: 'testutilisateur@example.com' });
    await expect(newUser).toBeVisible();
  });

  test('Suppression d’un utilisateur test', async ({ page }) => {
    // Recherche l'utilisateur
    await page.getByPlaceholder('Rechercher par email').fill('testutilisateur@example.com');
    await page.getByRole('button', { name: 'Search' }).click();

    const userRow = page.locator('tr', { hasText: 'testutilisateur@example.com' });
    await expect(userRow).toBeVisible();

    // Clique sur le bouton de suppression dans cette ligne
    const deleteButton = userRow.locator('button:has(svg)');
    await deleteButton.click();

    // Vérifie que la ligne disparaît
    await expect(userRow).toHaveCount(0);
  });
});
