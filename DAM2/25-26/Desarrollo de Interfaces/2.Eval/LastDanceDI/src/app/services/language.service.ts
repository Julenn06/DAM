import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})
export class LanguageService {
  private langKey = 'lang';

  constructor(private translate: TranslateService) { }

  initLanguage() {
    const lang = localStorage.getItem(this.langKey) || 'es';
    this.translate.setDefaultLang(lang);
    this.setLanguage(lang);
  }

  setLanguage(lang: string) {
    this.translate.use(lang);
    localStorage.setItem(this.langKey, lang);
  }

  getCurrentLanguage(): string {
    return this.translate.currentLang;
  }
}
