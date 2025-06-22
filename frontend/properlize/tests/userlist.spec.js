// Fichier : tests/userlist.spec.js

import { test, expect } from '@playwright/test';

const API_URL = 'http://localhost:9000/users';

// Données mockées pour simuler la base de données
const MOCK_USERS = [
  { id: 1, name: 'Alice', email: 'alice@example.com' },
  { id: 2, name: 'Bob', email: 'bob@example.com' },
];

test.describe('UserList Page Tests', () => {

  // Ce 'beforeEach' s'exécute avant chaque test dans ce bloc
  test.beforeEach(async ({ page }) => {
    // Mocker la route qui charge la liste initiale des utilisateurs
    await page.route(`${API_URL}/all`, async route => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(MOCK_USERS),
      });
    });
    
    // Naviguer vers la page
    await page.goto('/users');
  });

  test('should display a list of users on load', async ({ page }) => {
    // Attendre que la table soit visible, signe que les données sont chargées
    await expect(page.getByRole('table')).toBeVisible();

    // Vérifier des utilisateurs spécifiques
    await expect(page.getByRole('cell', { name: 'Alice', exact: true })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'bob@example.com', exact: true })).toBeVisible();
    
    // Vérifier le total
    await expect(page.getByText(`Total Number of users: ${MOCK_USERS.length}`)).toBeVisible();
  });

  test('should add a new user and update the list', async ({ page }) => {
    const newUser = { id: 3, name: 'Charlie', email: 'charlie@example.com' };

    // Mocker la réponse de l'API pour l'ajout
    await page.route(`${API_URL}/add`, async route => {
      await route.fulfill({ status: 201, contentType: 'application/json', body: JSON.stringify(newUser) });
    });

    // Ouvrir le formulaire
    await page.getByRole('button', { name: '+ ADD' }).click();
    
    // Remplir le formulaire (qui a maintenant un data-testid="add-user-form")
    const addUserForm = page.getByTestId('add-user-form');
    await expect(addUserForm).toBeVisible();
    await addUserForm.getByPlaceholder('Nom').fill(newUser.name);
    await addUserForm.getByPlaceholder('Email').fill(newUser.email);
    await addUserForm.getByPlaceholder('Password').fill('password123');

    // Soumettre et attendre la mise à jour de l'UI
    await addUserForm.getByRole('button', { name: 'Ajouter' }).click();

    // Attendre que le nouvel utilisateur apparaisse dans la table
    await expect(page.getByRole('cell', { name: newUser.name, exact: true })).toBeVisible();
    
    // Vérifier que le total a été mis à jour
    await expect(page.getByText(`Total Number of users: ${MOCK_USERS.length + 1}`)).toBeVisible();
  });

  test('should delete a user and update the list', async ({ page }) => {
    const userToDelete = MOCK_USERS[0]; // Alice

    // Mocker la réponse de l'API pour la suppression
    await page.route(`${API_URL}/${userToDelete.id}`, async route => {
      expect(route.request().method()).toBe('DELETE');
      await route.fulfill({ status: 204 }); // 204 No Content est une réponse typique pour un DELETE réussi
    });
    
    // Trouver la ligne de l'utilisateur à supprimer
    const userRow = page.getByRole('row', { name: new RegExp(userToDelete.name, 'i') });
    
    // Cliquer sur l'icône de suppression
    await userRow.locator('.icon.delete').click();
    
    // Attendre que la ligne de l'utilisateur ait disparu
    await expect(page.getByRole('cell', { name: userToDelete.name, exact: true })).not.toBeVisible();
    
    // Vérifier que le total a diminué
    await expect(page.getByText(`Total Number of users: ${MOCK_USERS.length - 1}`)).toBeVisible();
  });
});