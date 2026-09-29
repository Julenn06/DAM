import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { PersonService } from '../services/users.service';

export const guestGuard: CanActivateFn = () => {
  const personService = inject(PersonService);
  const router = inject(Router);

  if (personService.isLoggedIn()) {
    router.navigate(['/home']);
    return false;
  }

  return true;
};
