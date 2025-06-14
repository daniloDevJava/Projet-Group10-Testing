// Fichier : tests/vehicles.spec.js

import { test, expect } from '@playwright/test';

// --- DONNÉES MOCKÉES ---
// Simule la réponse de l'API pour la liste de tous les véhicules.
const MOCK_VEHICLES = [
  { id: 1, make: 'Toyota', model: 'Yaris', year: 2022, rentalPrice: 15000, cheminVersImage: 'path/to/yaris.jpg' },
  { id: 2, make: 'BMW', model: 'X5', year: 2023, rentalPrice: 45000, cheminVersImage: 'path/to/x5.jpg' },
  { id: 3, make: 'Mercedes', model: 'C-Class', year: 2021, rentalPrice: 35000, cheminVersImage: 'path/to/cclass.jpg' },
];

// Simule un utilisateur pour la connexion.
const MOCK_USER = {
  login: 'Imelda', // Correspond au `userLogin` que tu récupères du localStorage
};

// --- CONFIGURATION DU TEST ---
test.describe('Vehicles Dashboard (/home)', () => {

  // Cette étape s'exécute une fois avant tous les tests de ce fichier.
  // Elle simule la connexion et prépare l'environnement.
  test.beforeEach(async ({ page, context }) => {
    // 1. Sauvegarder le nom d'utilisateur dans le localStorage, comme le fait ton application.
    await context.addInitScript((userLogin) => {
      window.localStorage.setItem('userLogin', userLogin);
    }, MOCK_USER.login);

    // 2. Mocker la route API qui charge tous les véhicules.
    // Cela se déclenchera quand ton composant fera le `fetch` dans `useEffect`.
    await page.route('http://localhost:9000/vehicule/all', async route => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(MOCK_VEHICLES),
      });
    });

    // 3. Naviguer vers la page du tableau de bord.
    await page.goto('/home');
  });

  // --- LES TESTS ---

  test('should display welcome message and list of all vehicles on load', async ({ page }) => {
    // Vérifier le message de bienvenue avec le nom de l'utilisateur mocké.
    await expect(page.getByRole('heading', { name: `Bienvenue ${MOCK_USER.login}` })).toBeVisible();

    // Vérifier que le titre de la liste est présent.
    await expect(page.getByRole('heading', { name: 'Tous les véhicules disponibles' })).toBeVisible();

    // Vérifier que les 3 véhicules mockés sont bien affichés.
    // On peut compter le nombre de cartes de voiture.
    await expect(page.locator('.car-card')).toHaveCount(3);

    // Vérifier la présence d'un véhicule spécifique.
    await expect(page.getByText('Toyota Yaris')).toBeVisible();
    await expect(page.getByText('45000 FCFA')).toBeVisible();
  });

  test('should allow a user to like and unlike a vehicle', async ({ page }) => {
    // Cible la première carte de voiture (la Toyota Yaris).
    const firstCarCard = page.locator('.car-card').first();

    // Cible l'icône de cœur à l'intérieur de cette carte.
    const heartIcon = firstCarCard.locator('.heart');

    // Étape 1 : Vérifier l'état initial (non "liké").
    await expect(heartIcon).toHaveText('♡'); // Cœur vide
    await expect(heartIcon).not.toHaveClass(/liked/);

    // Étape 2 : Cliquer pour "liker".
    await heartIcon.click();

    // Étape 3 : Vérifier que l'état a changé (maintenant "liké").
    await expect(heartIcon).toHaveText('♥'); // Cœur plein
    await expect(heartIcon).toHaveClass(/liked/);

    // Étape 4 : Re-cliquer pour "unliker".
    await heartIcon.click();

    // Étape 5 : Vérifier que l'état est revenu à l'initial.
    await expect(heartIcon).toHaveText('♡');
    await expect(heartIcon).not.toHaveClass(/liked/);
  });

  test('should search for a vehicle by price and display results', async ({ page }) => {
    // Le véhicule avec le prix 35000 est la Mercedes.
    const searchPrice = '35000';
    const vehicleToFind = MOCK_VEHICLES.find(v => v.rentalPrice.toString() === searchPrice);

    // On ne mocke pas de nouvelle route ici, car la recherche par prix
    // réutilise la route 'vehicule/all' que nous avons déjà mockée.

    // 1. Remplir le champ de recherche.
    await page.getByPlaceholder('Rechercher un véhicule').fill(searchPrice);

    // 2. Sélectionner le critère de recherche "Par Prix".
    await page.getByRole('combobox').selectOption({ value: 'price' });

    // 3. Cliquer sur l'icône de recherche.
    await page.locator('.icone-recherche').click();

    // 4. Vérifier que la section des résultats de recherche apparaît.
    await expect(page.getByRole('heading', { name: 'Résultats de la recherche' })).toBeVisible();

    // 5. Vérifier qu'il n'y a qu'un seul résultat.
    const searchResultsSection = page.locator('.car-grid').nth(0); // Le premier car-grid est celui des résultats
    await expect(searchResultsSection.locator('.car-card')).toHaveCount(1);
    
    // 6. Vérifier que c'est bien le bon véhicule.
    await expect(searchResultsSection.getByText(`${vehicleToFind.make} ${vehicleToFind.model}`)).toBeVisible();
  });

  test('should log out the user and redirect to login page', async ({ page }) => {
    // 1. Cliquer sur le bouton de déconnexion.
    await page.getByRole('button', { name: /Logout/i }).click();

    // 2. Vérifier que l'on est bien redirigé vers la page de login.
    // On attend un élément clé de la page de login pour être sûr.
    await expect(page.getByRole('heading', { name: 'Login to your account' })).toBeVisible();
    await expect(page).toHaveURL(/.*\/login/);

    // Optionnel : Vérifier que le localStorage a été vidé.
    const userLogin = await page.evaluate(() => localStorage.getItem('userLogin'));
    expect(userLogin).toBeNull();
  });
});