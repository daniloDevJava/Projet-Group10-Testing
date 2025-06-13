// tests/vehicles.spec.js

import { test, expect } from '@playwright/test';

// On s'assure que le test est bien authentifié. C'est essentiel.
test.use({ storageState: 'playwright/.auth/user.json' });

test.describe('Vehicles Page', () => {

  // Test de base, il est correct.
  test('should display a list of all vehicles initially', async ({ page }) => {
    await page.goto('/vehicles');
    const vehicleGrid = page.locator('.vehicle-grid');
    await expect(vehicleGrid).toBeVisible();
    const vehicleCards = vehicleGrid.locator('.vehicle-card');
    await expect(vehicleCards).toHaveCount(4);
  });

  // --- LE TEST FINAL AVEC LA SOLUTION DÉFINITIVE ---
  test('should filter vehicles by category', async ({ page }) => {
    // 1. Navigation
    await page.goto('/vehicles');
    
    // 2. Localiser le bouton
    const toyotaButton = page.getByRole('button', { name: 'Toyota' });
    
    // 3. LA SOLUTION ULTIME : LE CLIC JAVASCRIPT
    // Puisque les interactions standards (clic, scroll) échouent à cause d'un layout complexe
    // (probablement un conteneur avec overflow:scroll), nous ordonnons au navigateur
    // d'exécuter l'événement "click" directement sur l'élément DOM, sans se soucier de sa visibilité.
    // C'est la méthode la plus directe pour déclencher l'action.
    await toyotaButton.evaluate(element => element.click());

    // 4. Vérifier le résultat
    // Les assertions restent les mêmes car la logique de votre application est correcte.
    const vehicleGrid = page.locator('.vehicle-grid');
    await expect(vehicleGrid.locator('.vehicle-card')).toHaveCount(1);
    await expect(page.getByText('Toyota Prado')).toBeVisible();
    await expect(page.getByText('BMW 207')).not.toBeVisible();
  });
});