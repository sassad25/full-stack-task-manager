/// <reference types="cypress" />

// End-to-end flow: sign in, create a task, advance its status, and delete it.
// Run with: npm run dev (and the backend on :8085) then `npx cypress run`.
describe('Task Manager', () => {
  it('signs in and manages a task', () => {
    cy.visit('/');

    cy.contains('Sign in');
    cy.get('input').first().type('e2e-user');
    cy.get('input[type=password]').type('password');
    cy.contains('button', 'Sign in').click();

    // Create a task
    cy.get('.task-form__title').type('Ship the release');
    cy.contains('button', 'Add').click();
    cy.contains('.task__title', 'Ship the release').should('be.visible');

    // Advance status TODO -> IN_PROGRESS
    cy.contains('.task', 'Ship the release').within(() => {
      cy.contains('button', 'TODO').click();
    });
    cy.contains('.task', 'Ship the release').within(() => {
      cy.contains('button', 'IN_PROGRESS').should('exist');
    });

    // Delete it
    cy.contains('.task', 'Ship the release').within(() => {
      cy.contains('button', 'Delete').click();
    });
    cy.contains('.task__title', 'Ship the release').should('not.exist');
  });
});
