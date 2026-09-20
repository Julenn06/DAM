import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { LanguageService } from '../../services/language.service';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.html',
  standalone: true,
  imports: [CommonModule, RouterModule, TranslateModule]
})
export class HeaderComponent {
  constructor(
    public authService: AuthService,
    private router: Router,
    public languageService: LanguageService
  ) {}

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  changeLanguage(lang: string) {
    this.languageService.setLanguage(lang);
  }
}
