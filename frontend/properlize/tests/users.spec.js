import { test, expect } from '@playwright/test';

const mockUserList = [
  { id: 101, name: 'Mock Imelda', email: 'imelda@mock.com', status: 'Actif' },
  { id: 102, name: 'Mock Ariane', email: 'ariane@mock.com', status: 'Inactif' },
];

test.describe('User Management Page with API Mocking', () => {
  // Le beforeEach est maintenant correct et gère intelligemment les requêtes.
  // On ne le modifie plus.
  test.beforeEach(async ({ page }) => {
    const apiHandler = async (route) => {
      const request = route.request();
      if (request.resourceType() === 'fetch') {
        if (request.url().includes('/users/add')) {
          console.log('Intercepted API call: POST /users/add');
          const requestBody = request.postDataJSON();
          const newUser = { id: Date.now(), name: requestBody.name, email: requestBody.email, status: 'Actif' };
          await route.fulfill({ status: 201, contentType: 'application/json', body: JSON.stringify(newUser) });
        } else if (request.url().includes('/users')) {
          console.log('Intercepted API call: GET /users');
          await route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify(mockUserList) });
        } else {
          await route.continue();
        }
      } else {
        await route.continue();
      }
    };
    await page.route('**/users**', apiHandler);
  });

  // Ce test est déjà robuste.
  test('should display a list of mocked users', async ({ page }) => {
    await page.goto('/users');
    
    // Attendre que la page soit prête en vérifiant un élément stable
    await expect(page.getByRole('heading', { name: 'UserList' })).toBeVisible();
    
    // Vérifier les données mockées
    await expect(page.getByTestId('user-name-101')).toBeVisible();
    await expect(page.getByText('ariane@mock.com')).toBeVisible();
  });

  // C'est ce test qui a été corrigé pour être plus patient.
  test('should allow adding a new user via mocked API', async ({ page }) => {
    // 1. Navigation
    await page.goto('/users');

    // 2. CORRECTION : Attendre que la page et sa structure principale (le conteneur de la table) soient bien visibles.
    // C'est notre point de contrôle pour s'assurer que l'application est prête.
    await expect(page.getByTestId('user-list-container')).toBeVisible({ timeout: 15000 });
    await page.pause();
    // 3. Maintenant qu'on est sûr que la page est chargée, on cherche et on clique sur le bouton.
    const addUserButton = page.getByTestId('show-add-user-form-button');
    await expect(addUserButton).toBeVisible();
    await addUserButton.click();
    
    // 4. On attend que le formulaire soit visible avant de le remplir.
    await expect(page.getByTestId('add-user-form')).toBeVisible();

    // 5. Remplir le formulaire
    const newUserName = 'Test Add User';
    const newUserEmail = 'testadd@mock.com';
    await page.getByTestId('new-user-name-input').fill(newUserName);
    await page.getByTestId('new-user-email-input').fill(newUserEmail);
    await page.getByTestId('new-user-password-input').fill('password123');
    
    // 6. Soumettre le formulaire
    await page.getByTestId('add-user-submit-button').click();
    
    // 7. Vérifier que le nouvel utilisateur apparaît dans la liste.
    // On peut cibler la ligne pour être plus précis.
    const newUserRow = page.locator(`tr:has-text("${newUserName}")`);
    await expect(newUserRow).toBeVisible();
  });
});