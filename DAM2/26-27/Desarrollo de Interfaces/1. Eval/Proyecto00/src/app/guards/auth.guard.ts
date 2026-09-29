import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { PersonService } from '../services/users.service';

export const authGuard: CanActivateFn = () => {
  const personService = inject(PersonService);
  const router = inject(Router);

  if (personService.isLoggedIn()) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};
