import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Auth } from '../services/auth';

export const authGuardGuard: CanActivateFn = (route, state) => {
  const authService = inject(Auth);
  const router = inject(Router);
    
  if (authService.isAuthenticated()) {
    return true; // Erabiltzailea logeatuta. Sarbidea BAIMENDUTA
  } else {
    // Erabiltzailea ez badago logeatuta, /home-ra birbideratzen da
    return router.createUrlTree(['/home']);
  }
};
